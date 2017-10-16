package ch.ffhs.kino.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import ch.ffhs.kino.model.TicketPrice;
import ch.ffhs.kino.component.SeatView;
import ch.ffhs.kino.component.TicketRow;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.model.Seat.SeatType;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class VorstellungController {
	/**
	 * Birgit
	 *  TODO: Fertigstellen
	 */
	private Vorstellung vorstellung;
	//List<TicketPrice> ticketPrices;
	
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
	private Label vorstellungChoice;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private Button buyButton;
    
//	@FXML
//	private TableView<Ticket> ticketTable;
//	
//	@FXML
//	private TableColumn<Ticket, String> colSeatDescription;
//	
//	@FXML
//	private TableColumn<Ticket, TicketType> colPrice;
	
//	@FXML
//	private ImageView imageView;
	/**
	 * Die Liste der ausgewählten Sitze
	 */
	// ObservableList: damit ich Änderungen nachverfolgen kann
	//private ObservableList<SeatView> selectedSeats = FXCollections.observableArrayList();
	
	private List<SeatView> selectedSeats = new ArrayList();
	
	//private CardView views[][];
	/**
     * Die Liste der Reservierungen
     */
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	
	//private ObservableList<TicketPrice> ticketPriceData;	
	private long endTime ;
	private Timeline timeline;
	
	@FXML
	protected void initialize() {	
		
		initTimeline();
	
		initTicketControl();
		
		buyButton.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		buyButton.setTooltip(new Tooltip("Zur Zahlungsabwicklung"));
		buyButton.setOnAction(commandBuyHandler);
		
//		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//
//		     @Override
//		     public void handle(MouseEvent event) {
//		    	 // Click Mouse Event for Image...
//		         event.consume();
//		     }
//		});
		
		//imageView.setStyle("-fx-background-color:transparent;");
		//imageView.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("JavafxSm.gif"))));

//		imageView.setOnMouseEntered(new EventHandler<MouseEvent>
//	    () {
//
//	        @Override
//	        public void handle(MouseEvent t) {
//	        	imageView.setStyle("-fx-effect: dropshadow(gaussian, #54acd2, 1, 1, 0, 0);");
//	        }
//	    });
//
//		imageView.setOnMouseExited(new EventHandler<MouseEvent>
//	    () {
//
//	        @Override
//	        public void handle(MouseEvent t) {
//	        	imageView.setStyle("-fx-background-color:transparent;");
//	        }
//	    });
	    
//		buyButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//                try {
//                	// Booking erstellen
//                	Booking booking = new Booking();
//                	booking.setEvent(vorstellung);
//                	booking.setTickets(ticketData);
//                	Main.startTicketZahlen(booking);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//            }
//        });
		
		
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
	
	public Vorstellung getVorstellung() {
		return vorstellung;
	}

	public void setVorstellung(Vorstellung vorstellung, List<TicketPrice> ticketPrices) {
		this.vorstellung = vorstellung;
		initTitle();
		//this.ticketPriceData = FXCollections.observableArrayList(ticketPrices);
		//this.renderHallGrid();
		this.renderSeatView();
	}
	
	private void initTitle() {
		String movieTitle = vorstellung.getShow().getMovie().getTitle();
		String movieLanguage = vorstellung.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		vorstellungChoice.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(vorstellung.getDate()) + ", " + vorstellung.getHall().getHallName());
	}
	
	private void initTicketControl() {
		// Eigenes Control definiert, da TableView nicht so viel kann:
		ticketData.addListener((ListChangeListener<Ticket>) change -> {
			ticketGrid.getChildren().clear();
			for(Ticket ticket : ticketData) {
				
				// ToDo: Styles auslagern ins css
				TicketRow row = new TicketRow();
				row.setStyle("-fx-padding: 5;");
				row.setTicket(ticket);
				
				Label label = new Label();
				label.setText(ticket.getSeat().toString());
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
			        int seatNumber = ticket.getSeat().getSeatNumber();
			        SeatView seatView =  selectedSeats.stream().filter(x -> x.getSeat().getSeatNumber() == seatNumber).findFirst().get();
			        seatView.deselect();
			        //ticket.getSeat().deselect();
			        ticketData.remove(ticket);
			    });				
				
				row.getChildren().add(icon);
				ticketGrid.getChildren().add(row);
			}
		});
	}

	//private Boolean isTimeOver = false;
	private BooleanProperty isTimeOver = new SimpleBooleanProperty(false);
	
	private void initTimeline() {
		
		this.isTimeOver.addListener((value, oldValue, newValue) -> {
	    	if (newValue == true) {
				
	    	  	// Selektierte Sitze freigeben
	    		selectedSeats.forEach((seat) -> { 
	    			seat.deselect();
	    		});
	    		selectedSeats.clear();
	    		
	    		// Ticket entfernen
	    		//ticketData.clear();
	    		
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Information Dialog");
	    		alert.setHeaderText("Look, an Information Dialog");
	    		alert.setContentText("I have a great message for you!");

	    		alert.showAndWait();
	    			    		
	    		isTimeOver.set(false);
	    	}
	    	});
		
		timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> {
			    	long diff = endTime - System.currentTimeMillis();
			    	if(diff <= (long)0) {
			    		timeline.stop();
			    		
			    		
			    		
			    		// Meldung an den Benutzer
			    		Platform.runLater(new Runnable() {
			    		      @Override public void run() {
			    		    	  isTimeOver.set(true); 
			    		      }
			    		});			    					    		
			    	}else {
			    		SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
				        timeLabel.setText(fmt.format(diff));
			    	} 
			    })
			);
		timeline.setCycleCount( Animation.INDEFINITE );
	}

	public void StartTimeAnimation(){
		endTime = System.currentTimeMillis() + (600 * 1000); // 10 Minuten
		//isTimeOver.set(false);
		//endTime = System.currentTimeMillis() + (6 * 1000);
		timeline.play();
	}
	
	public void StopTimeAnimation(){
		timeline.stop();
	}
	
//	private void renderHallGrid() {
//		Hall hall = vorstellung.getHall();
//		int rows = hall.getRows();
//	    int columns = hall.getColumns();
//	    Boolean[][] seatPlan = hall.getSeatPlan();
//       
//		this.selectedSeats.clear();
//	
//	    ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
//	    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
//	
////	    double x = gridWidth.get();
////	    double y = gridHeight.get();
////	    
////	    NumberBinding size1 = gridWidth.subtract(220).divide(columns).subtract(0);
////	    NumberBinding size2 = gridHeight.subtract(20).divide(rows).subtract(2);
//	    
//	    // Für den Zoom-Effekt
//	    // High-Level Binding (Fluent API)
//	    // Man braucht nun keine zusätzlichen Listener für Variablen-Änderungen mehr zu erstellen 
//	    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
//	    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
//	    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
//		
//	    // Hier werden die Controls für die Sitzplätze entsprechend der Sitzplatz-Definition der Halle erstelllt 
//	    int seatCounter = 1;
//	    for (int row = 0; row < (rows ); row++) {
//	      for (int column = 0; column < (columns ); column++) {
//	        
//	    	Seat seat = new Seat(row , column, seatCounter);
//	    	seatCounter++;
//	        seat.widthProperty().bind(size);
//	        seat.heightProperty().bind(size);
//	
//	    	if(seatPlan[row][column] == false){
//	    		seat.setDisable(true);
//	    		seat.setOpacity(0);
//	    	}
//	    	
//	        seat.getState().addListener((e, oldValue, newValue) -> {
//	        	if(isTimeOver.get() == false) {
//		        	if (newValue) {
//			        	  if(selectedSeats.size() == 0)
//			        		  StartTimeAnimation();	        	  
//			        	  selectedSeats.add(seat);
//			        	  // Ticket für diesen Sitzplatz hinzufügen
//			        	  Ticket ticket = new Ticket(seat);
//		        	  ticketData.add(ticket);
//			          }
//			          else {
//			            this.selectedSeats.remove(seat);
//			            if(selectedSeats.size() == 0)
//			            	StopTimeAnimation();
//			            for(Ticket ticket : ticketData) {
//			                if(ticket.getSeat().equals(seat)) {
//			                	// Ticket für diesen Sitzplatz entfernen
//			                	ticketData.remove(ticket);
//			                }
//			            } 
//			          }		        		
//	        	}
//        	  
//
//
//	        });
//	        
//	        this.hallGrid.add(seat, column, row);   	
//	      }
//	    }
//	    
//	    // Verkaufte Plätze markieren
//	    List<Booking> bookings = vorstellung.getBookings();
//	    for (Booking booking : bookings) {
//	    	List<Ticket> tickets = booking.getTickets();
//	    	for(Ticket ticket : tickets) {
//	    		Seat bookedSeat = ticket.getSeat();
//	    		for(Node node : hallGrid.getChildren()) {
//	    			Seat seat = (Seat)node;
//	    			if(seat.getSeatNumber() == bookedSeat.getSeatNumber()) {
//	    				seat.setSold();
//	    			}
//	    		}
//	    	}
//	    }
//	}

	/**
	 * Die Sitze für den entsprechenden Kinosaal zeichnen
	*/
	private void renderSeatView() {
		Hall hall = vorstellung.getHall();
		int rows = hall.getRows();
	    int columns = hall.getColumns();
	    SeatType[][] seatPlan = hall.getSeatPlan();
	    List<Integer> bookedSeatNumbers = new ArrayList<Integer>();
	    List<Booking> bookings = vorstellung.getBookings();
	    for (Booking booking : bookings) {
	    	List<Ticket> tickets = booking.getTickets();
	    	for(Ticket ticket : tickets)
	    		bookedSeatNumbers.add(ticket.getSeat().getSeatNumber());
	    }	    
	    
	    ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
	    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
	    
	    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
	    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
	    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
	    
	    SeatView views[][] = new SeatView[rows][];
	    	    
	    //Image back = new Image(Main.class.getResourceAsStream("ch/ffhs/kino/movies/FSK_ab_16.png"));
	    Image seatPic = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seat_small.png"));
	    Image soldPic = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSold_small.png"));
	    Image selectedPic = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSelected_small.png"));
	    
	    SeatView.setSeatPic(seatPic);
	    SeatView.soldSeatPic(soldPic);
	    SeatView.setSeatSelectedPic(selectedPic);
	    
	    int seatCounter = 1;
	    
        for (int r = 0; r < rows; r++) {
            views[r] = new SeatView[columns];          
            for (int c = 0; c < columns; c++) {
                Seat seat = new Seat(r, c, seatCounter);
            	SeatView seatView = new SeatView(seat); // different front images of course...
            	views[r][c] = seatView;
            	
            	// Prüfe, ob der Platz verkauft ist
            	if(bookedSeatNumbers.contains(seatCounter))
            		seatView.setSold();
            	
            	// ImageViews are not resizeable, but you could use a parent that is resizeable and bind the fitWidth and fitHeight properties 
            	// to the size of the parent using expression binding          	    	
            	seatView.fitWidthProperty().bind(size);
            	seatView.fitHeightProperty().bind(size);
            	
            	
            	if(seatPlan[r][c] == SeatType.NONE){
            		seatView.setDisable(true);
            		seatView.setOpacity(0);
            		continue;
    	    	}
            	
            	seatCounter++;
            	         	
            	seatView.getState().addListener((e, oldValue, newValue) -> {
    	        	if(isTimeOver.get() == false) {
    		        	if (newValue) {
    			        	  if(selectedSeats.size() == 0)
    			        		  StartTimeAnimation();	        	  
    			        	  selectedSeats.add(seatView);
    			        	  // Ticket für diesen Sitzplatz hinzufügen
    			        	  Ticket ticket = new Ticket(seat);
    			        	  ticketData.add(ticket);
    			          }
    			          else {
    			            this.selectedSeats.remove(seatView);
    			            if(selectedSeats.size() == 0)
    			            	StopTimeAnimation();
    			            
    			            // Ticket aus der Liste entfernen
    			            Optional<Ticket> removeTicket = ticketData.stream().
    			            		filter(x -> x.getSeat().equals(seat)).findFirst();  			            
    			            if(removeTicket.isPresent())
    			            	ticketData.remove(removeTicket.get());
    			          }		        		
    	        	}
    	        });
            	              
                HBox box = new HBox(5);
                box.getChildren().add(views[r][c]);
                hallGrid.add(box, c, r);
            }
        }
	}
	
    private EventHandler<ActionEvent> commandBuyHandler = (evt) -> {
    	// Booking erstellen
    	Booking booking = new Booking();
    	booking.setEvent(vorstellung);
    	booking.setTickets(ticketData);
    	try {
			Main.startTicketZahlen(booking);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    };
    
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
