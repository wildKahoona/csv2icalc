package ch.ffhs.kino.controller;

import java.util.List;

import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import ch.ffhs.kino.model.TicketPrice;
import ch.ffhs.kino.component.Seat;
import ch.ffhs.kino.component.TicketRow;
import ch.ffhs.kino.model.Vorstellung;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

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
	
	@FXML
	protected void initialize() {	
		
//  	//!!! Tabelle geht leider nicht, da ich keine Button in die Zelle reintung kann !!!
//		//Die Spalten der Tabelle definieren
//		colSeatDescription.setCellValueFactory(new PropertyValueFactory<Ticket, String>("seatDescription"));
//		colPrice.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
//		colPrice.setCellFactory(ComboBoxTableCell.<Ticket, TicketType>forTableColumn(TicketType.values()));
//		
//		//Die Tabelle anzeigen.
//		ticketTable.setItems(ticketData);
//		ticketTable.setEditable(true);
		
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
	
	/**
	 * Die Sitze für den entsprechenden Kinosaal zeichnen
	*/
	private void renderHallGrid() {
		Hall hall = vorstellung.getHall();
	    Boolean[][] seatPlan = hall.getSeatPlan();   
	    int rows = hall.getRows();
	    int columns = hall.getColumns();
	            
		this.selectedSeats.clear();
	
	    ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
	    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
	
	    double x = gridWidth.get();
	    double y = gridHeight.get();
	    
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
	    	if(row == 4 && (column == 2 || column==3 || column==4)) // TEST
	    		seat.setSold();
	
	        seat.widthProperty().bind(size);
	        seat.heightProperty().bind(size);
	
	        seat.getState().addListener((e, oldValue, newValue) -> {
	          if (newValue) {
	            this.selectedSeats.addAll(seat);
	         	// Ticket für diesen Sitzplatz hinzufügen
	            Ticket ticket = new Ticket(seat);
	            ticketData.add(ticket);
	          }
	          else {
	            this.selectedSeats.removeAll(seat);
	            for(Ticket ticket : ticketData) {
	                if(ticket.getSeat().equals(seat)) {
	                	// Ticket für diesen Sitzplatz entfernen
	                	ticketData.remove(ticket);
	                }
	            }
	          }
	        });
	        
	    	if(seatPlan[row][column] == false)
	    	{
	    		seat.setDisable(true);
	    		seat.setOpacity(0);
	    	}
	        this.hallGrid.add(seat, column, row);   	
	      }
	    }	
	}
}
