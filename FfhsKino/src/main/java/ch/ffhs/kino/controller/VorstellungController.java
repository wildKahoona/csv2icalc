package ch.ffhs.kino.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Show;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import ch.ffhs.kino.model.TicketPrice;
import ch.ffhs.kino.component.Seat;
import ch.ffhs.kino.component.TicketRow;
import ch.ffhs.kino.model.Vorstellung;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class VorstellungController {
	/**
	 * Birgit
	 *  TODO: Fertigstellen
	 */
	Vorstellung vorstellung;
	//List<TicketPrice> ticketPrices;

	public Vorstellung getVorstellung() {
		return vorstellung;
	}

	public void setVorstellung(Vorstellung vorstellung, List<TicketPrice> ticketPrices) {
		this.vorstellung = vorstellung;
		initTitle();
		this.ticketPriceData = FXCollections.observableArrayList(ticketPrices);
		this.renderHallGrid();
	}

	/**
	 * Das GridPane des Kinosaals mit den Sitzen
	 */
	@FXML
	private GridPane hallGrid;
	
	@FXML
	private VBox ticketGrid;
	
	@FXML
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());
	}
	
	@FXML
	Label vorstellungChoice;
	
	@FXML
    Label timeLabel;
	
	DoubleProperty time = new SimpleDoubleProperty();
    
//	@FXML
//	private TableView<Ticket> ticketTable;
//	
//	@FXML
//	private TableColumn<Ticket, String> colSeatDescription;
//	
//	@FXML
//	private TableColumn<Ticket, TicketType> colPrice;
	
    
	/**
	 * Die Liste der ausgewählten Sitze
	 */
	// ObservableList: damit ich Änderungen nachverfolgen kann
	private ObservableList<Seat> selectedSeats = FXCollections.observableArrayList();
	
	/**
     * Die Liste der Reservierungen
     */
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	
	private ObservableList<TicketPrice> ticketPriceData;
	
	private AnimationTimer timer;
	private long endTime ;
	private Timer timer1;
	
	Timeline timeline;
	
	@FXML
	protected void initialize() {	
		
		initTimeline();
	
		initTicketControl();
		
//		timer = new AnimationTimer() {
//		    
//			@Override
//		    public void start() {
//				startTime = 600 * 1000; // 10 Minuten
//		        super.start();
//		    }
//
//		    @Override
//		    public void handle(long timestamp) {
////		        long now = System.currentTimeMillis();
////		        time.set((now - startTime) / 1000.0);
//		    	startTime = timestamp;
//		        
//		        SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
//		        timeLabel.setText(fmt.format(startTime));
//		    }
//		};
//		timer.start();
		
//		hallGrid.widthProperty().addListener((ov, oldValue, newValue) -> {
//			resizeSeat(newValue);
//        });
//		hallGrid.heightProperty().addListener((ov, oldValue, newValue) -> {
//			resizeSeat(newValue);
//        });
		
//  	//!!! Tabelle geht leider nicht, da ich keine Button in die Zelle reintung kann !!!
//		//Die Spalten der Tabelle definieren
//		colSeatDescription.setCellValueFactory(new PropertyValueFactory<Ticket, String>("seatDescription"));
//		colPrice.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
//		colPrice.setCellFactory(ComboBoxTableCell.<Ticket, TicketType>forTableColumn(TicketType.values()));
//		
//		//Die Tabelle anzeigen.
//		ticketTable.setItems(ticketData);
//		ticketTable.setEditable(true);
	}
	
	private void initTitle() {
		String movieTitle = vorstellung.getShow().getMovie().getTitle();
		String movieLanguage = vorstellung.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		vorstellungChoice.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(vorstellung.getDate()) + ", " + vorstellung.getHall().getHallName());
	}
	
	private void initTicketControl() {
		// Eigenes Control definiert, da TableView nicht so viel kann:
		ticketData.addListener(new ListChangeListener<Ticket>() {
			@Override
			public void onChanged(ListChangeListener.Change change) {
				ticketGrid.getChildren().clear();
				for(Ticket ticket : ticketData) {
					
					// ToDo: Styles auslagern ins css
					TicketRow row = new TicketRow();
					row.setStyle("-fx-padding: 5;");
					row.setTicket(ticket);
					
					Label label = new Label();
					label.setText(ticket.getSeatDescription());
					label.setStyle("-fx-padding: 5;");
					label.setTextAlignment(TextAlignment.CENTER);	
					row.getChildren().add(label);
					
					ComboBox<TicketType> cbxTicketType = new ComboBox<>();
					cbxTicketType.setStyle("-fx-padding: 0,5;");
					cbxTicketType.setItems( FXCollections.observableArrayList( TicketType.values()));					
					cbxTicketType.getSelectionModel().select(ticket.getTicketType());
					cbxTicketType.valueProperty().addListener(new ChangeListener<TicketType>() {
					    @Override
					    public void changed(ObservableValue<? extends TicketType> observable, TicketType oldValue, TicketType newValue) {
					        if(newValue != null){
					        	ticket.setTicketType(newValue);
					        }
					    }
					});					
					row.getChildren().add(cbxTicketType);

					FontAwesomeIcon icon = new FontAwesomeIcon();
					icon.setIconName("TRASH");
					icon.setSize("1.5em");
					//icon.setStyle("-fx-padding: 5;");
					//icon.setTextAlignment(TextAlignment.CENTER);
					icon.setOnMouseClicked(e -> {
				        ticket.getSeat().deselect();
				        ticketData.remove(ticket);
				    });				
					
					row.getChildren().add(icon);
					ticketGrid.getChildren().add(row);
				}
			}
		});
	}

	private void initTimeline() {
		timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> {
			    	long diff = endTime - System.currentTimeMillis();
			        SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
			        timeLabel.setText(fmt.format(diff));
			    })
			);
		timeline.setCycleCount( Animation.INDEFINITE );
	}

	public void StartTimeAnimation(){
		endTime = System.currentTimeMillis() + (600 * 1000); // 10 Minuten
		timeline.play();
	}
	
	public void StopTimeAnimation(){
		timeline.stop();
	}
	
	/**
	 * Die Sitze für den entsprechenden Kinosaal zeichnen
	*/
	private void renderHallGrid() {
		Hall hall = vorstellung.getHall();
		int rows = hall.getRows();
	    int columns = hall.getColumns();
	    Boolean[][] seatPlan = hall.getSeatPlan();   
	    Boolean[][] soldList = hall.getSoldList();
       
		this.selectedSeats.clear();
	
	    ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
	    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
	
//	    double x = gridWidth.get();
//	    double y = gridHeight.get();
//	    
//	    NumberBinding size1 = gridWidth.subtract(220).divide(columns).subtract(0);
//	    NumberBinding size2 = gridHeight.subtract(20).divide(rows).subtract(2);
	    
	    // Für den Zoom-Effekt
	    // High-Level Binding (Fluent API)
	    // Man braucht nun keine zusätzlichen Listener für Variablen-Änderungen mehr zu erstellen 
	    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
	    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
	    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
		
	    // Hier werden die Controls für die Sitzplätze entsprechend der Sitzplatz-Definition der Halle erstelllt 
	    for (int row = 0; row < (rows ); row++) {
	      for (int column = 0; column < (columns ); column++) {
	        
	    	Seat seat = new Seat(row , column);
	        seat.widthProperty().bind(size);
	        seat.heightProperty().bind(size);
	
	    	if(seatPlan[row][column] == false){
	    		seat.setDisable(true);
	    		seat.setOpacity(0);
	    	}
	    	
	    	if(soldList[row][column] == true){
	    		seat.setSold();
	    	}
	    	
	        seat.getState().addListener((e, oldValue, newValue) -> {
	          if (newValue) {
	        	  if(selectedSeats.size() == 0)
	        		  StartTimeAnimation();	        	  
	        	  this.selectedSeats.addAll(seat);
	        	  // Ticket für diesen Sitzplatz hinzufügen
	        	  Ticket ticket = new Ticket(seat);
	        	  ticketData.add(ticket);
	          }
	          else {
	            this.selectedSeats.removeAll(seat);
	            if(selectedSeats.size() == 0)
	            	StopTimeAnimation();
	            for(Ticket ticket : ticketData) {
	                if(ticket.getSeat().equals(seat)) {
	                	// Ticket für diesen Sitzplatz entfernen
	                	ticketData.remove(ticket);
	                }
	            } 
	          }
	        });
	        
	        this.hallGrid.add(seat, column, row);   	
	      }
	    }	
	}

//	private void resizeSeat(Number newValue) {
//	Hall hall = vorstellung.getHall(); 
//    int rows = hall.getRows();
//    int columns = hall.getColumns();
//    
//	ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
//    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
//    
//	NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
//    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
//    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
//	
//	for(Node node: this.hallGrid.getChildren()) {
//		Rectangle seat = (Rectangle)node;
//		seat.widthProperty().unbind();
//		seat.heightProperty().unbind();
//		seat.widthProperty().bind(size);
//    	seat.heightProperty().bind(size);
//	}
//}
}
