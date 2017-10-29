package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import ch.ffhs.kino.component.SeatView;
import ch.ffhs.kino.component.TimerAnimation;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Seat.SeatType;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.table.model.TicketTableModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class VorstellungController {
	/**
	 * Birgit
	 *  TODO: Fertigstellen
	 */
	private TimerAnimation timerAnimation;
	
	private Vorstellung vorstellung;
	
	private SeatView views[][];
	
	private Booking reservation;
	
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
	
	@FXML
	private Button deleteTicketsButton;
	
	@FXML
	private Button addTicketButton;
	
	@FXML
	private TableView<TicketTableModel> ticketTable;
	
	@FXML
	private Label labelNoTickets;
	
	@FXML
	private HBox boxTimer;

	
	/**
	 * Die Liste der ausgewählten Sitze
	 */
	private ObservableList<SeatView> selectedSeats = FXCollections.observableArrayList();
	
	/**
     * Die Liste der Reservierungen
     */
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	private ObservableList<TicketTableModel> ticketTableData = FXCollections.observableArrayList();
	

	private long endTime ;
	private Timeline timeline;
	private Boolean addTicketOn = true;

	// Zelle mit Delete-Button
	private class ButtonCell extends TableCell<TicketTableModel, Boolean> {
	    final Button cellButton = new Button("Action");	     
	    ButtonCell(){         
	        cellButton.setOnAction(new EventHandler<ActionEvent>(){

	            @Override
	            public void handle(ActionEvent t) {
	                // do something when button clicked
	            	int selectdIndex = getTableRow().getIndex();
	            	 //delete the selected item in data
	            	ticketTableData.remove(selectdIndex);
	            }
	        });
	    }

	    //Display button if the row is not empty
	    @Override
	    protected void updateItem(Boolean t, boolean empty) {
	        super.updateItem(t, empty);
	        if(!empty){
	            setGraphic(cellButton);
	        }
	    }
	}
	
	@FXML
	protected void initialize() {	
		
		initTicketControl();
		
		buyButton.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		buyButton.setTooltip(new Tooltip("Zur Zahlungsabwicklung"));
		buyButton.setOnAction(commandBuyHandler);
		
		deleteTicketsButton.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		deleteTicketsButton.setOnAction(commandDeleteTicketsHandler);
		
		addTicketButton.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		addTicketButton.setOnAction(commandAddTicketHandler);
	}
	
	public Vorstellung getVorstellung() {
		return vorstellung;
	}

	public void setVorstellung(Vorstellung vorstellung) {
		this.vorstellung = vorstellung;
		initTitle();
		renderSeatView();
		renderReservedSeats();
		renderBookedSeats();
	}
	
	public void setVorstellung(Booking reservation) {
		this.reservation = reservation;
		this.vorstellung = this.reservation.getEvent();
		setVorstellung(this.reservation.getEvent());
	}
	
	private void initTitle() {
		String movieTitle = vorstellung.getShow().getMovie().getTitle();
		String movieLanguage = vorstellung.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		vorstellungChoice.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(vorstellung.getDate()) + ", " + vorstellung.getHall().getHallName());
	}
	
	private void initTicketControl() {

	}

	private void clearSelectedSeats() {
		// ACHTUNG: NICHT THREAD-SICHER (hat mich viele Stunden gekostet)!!!
//		selectedSeats.forEach((seat) -> { 
//			seat.deselect();
//		});
//		selectedSeats.clear();
		
		// THREAD-SICHER
		Iterator<SeatView> seats = this.selectedSeats.iterator();
		while (seats.hasNext()) {
			SeatView seat = seats.next();
			seats.remove();
			seat.deselect();
		}
	}
	  
	/**
	 * Die Sitze für den entsprechenden Kinosaal zeichnen
	*/
	private void renderSeatView() {
		Hall hall = vorstellung.getHall();
		int rows = hall.getRows();
	    int columns = hall.getColumns();
	    SeatType[][] seatPlan = hall.getSeatPlan();

	    ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
	    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
	    
	    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
	    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
	    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
	    
	    views = new SeatView[rows][];
	    
	    // Bilder der Sitzplätze laden
	    Image selectedSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSelected_small.png"));
	    Image normalSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seat_small.png"));
	    Image premiumSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatPremium_small.png"));
	    Image handicapSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatHandicap_small.png"));
	    
	    // Bild für den ausgewählten Sitz für alle Objekte setzen	    
	    SeatView.setImageSelcectedSeat(selectedSeat);
	   	    
        for (int r = 0; r < rows; r++) {
            views[r] = new SeatView[columns];          
            for (int c = 0; c < columns; c++) {
                Seat seat = new Seat(r, c);
            	SeatView seatView = new SeatView(seat);         	
            	views[r][c] = seatView;
            	
            	// Je nach Sitz-Typ ein anderes Standardbild nehmen            	
            	if(seatPlan[r][c] == SeatType.NONE){
            		seatView.setDisable(true);
            		seatView.setOpacity(0);
            		continue;
    	    	}else if (seatPlan[r][c] == SeatType.HANDYCAP){
    	    		seatView.setImageSeat(handicapSeat);
    	    	}else if (seatPlan[r][c] == SeatType.PREMIUM){
    	    		seatView.setImageSeat(premiumSeat);
    	    	}else {
    	    		seatView.setImageSeat(normalSeat);
    	    	}

            	// ImageViews are not resizeable, but you could use a parent that is resizeable and bind the fitWidth and fitHeight properties 
            	// to the size of the parent using expression binding          	    	
            	seatView.fitWidthProperty().bind(size);
            	seatView.fitHeightProperty().bind(size);
            	  	           	
            	seatView.getState().addListener((e, oldValue, newValue) -> {
		        	if (newValue) {
//			        	  if(selectedSeats.size() == 0)
//			        		  timerAnimation.startTimeAnimation();
			        	//StartTimeAnimation();
			        	  selectedSeats.add(seatView);
			        	  // Ticket für diesen Sitzplatz hinzufügen
			        	  if(addTicketOn)
			        	  {
			        		  Ticket ticket = new Ticket(seat);
			        		  ticketData.add(ticket);
			        	  }
			          }
			          else {
			            this.selectedSeats.remove(seatView);
			            if(selectedSeats.size() == 0)
			            	timerAnimation.stopTimeAnimation();
			            	//StopTimeAnimation();
			            
			            // Ticket aus der Liste entfernen
			            Optional<Ticket> removeTicket = ticketData.stream().
			            		filter(x -> x.getSeat().equals(seat)).findFirst();  			            
			            if(removeTicket.isPresent())
			            	ticketData.remove(removeTicket.get());
			          }		        		
    	        });
            	              
//                HBox box = new HBox(5);
//                box.getChildren().add(views[r][c]);
//                hallGrid.add(box, c, r);
            	this.hallGrid.add(views[r][c], c, r);
            }
        }
	}
	
	private void renderReservedSeats() {
		addTicketOn = false;
	    if(this.reservation != null) {
	    	for(Ticket ticket : reservation.getTickets()) {
	    		ticketData.add(ticket);
	    		Seat seat = ticket.getSeat();
	    		SeatView seatView = views[seat.getSeatRow()][seat.getSeatColumn()];
	    		if (seatView != null)
	    			seatView.select();
	    	}
	    }
	    addTicketOn = true;
	}
	  
	private void renderBookedSeats() {
	    List<Booking> bookings = vorstellung.getBookings();
	    if(bookings.isEmpty()) return;
	    Image soldSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSold_small.png"));
	    for (Booking booking : bookings) {
	    	for(Ticket ticket : booking.getTickets()) {
	    		Seat seat = ticket.getSeat();
	    		SeatView seatView = views[seat.getSeatRow()][seat.getSeatColumn()];
	    		if (seatView != null) {
	    			seatView.setSold();
            		seatView.setImageSeat(soldSeat);
	    		}
	    	}
	    }
	}
	
	private EventHandler<ActionEvent> commandDeleteTicketsHandler = (evt) -> {
		clearSelectedSeats();
	};
	
	private SeatView checkNeibourhut(SeatView seat, int rows, int columns) {
		// TODO Auto-generated method stub
		int row = seat.getSeat().getSeatRow();
		int col = seat.getSeat().getSeatColumn();
		Boolean checkLeft = true;
		Boolean checkRight = true;
		Boolean checkFront = true;
		Boolean checkBack = true;
		Boolean firstLeft = true;
		
		int middleCol = rows / 2;
		
		if(col < middleCol)
			firstLeft = false;
		
		if(row == 0) {
			checkFront = false;
		}
		
		if(col == 0) {
			checkLeft = false;
		}
		
		if(row == (rows - 1)){
			checkBack = false;
		}
		
		if(col == (columns-1)) {
			checkRight = false;
		}
		
		// Rechts oder Links?
//		int middleRow = rows % 2;
//		int middleCol = columns % 2;
		SeatView seatView;

		if(firstLeft)
		{
			if(checkLeft) {
				// Links
				seatView = views[row][col-1];
				if(!seatView.isDisable() && !seatView.getSold() && ! seatView.getIsSelected()) {
					return seatView;
				}			
			}
			
			if(checkRight) {
				// Rechts
				seatView = views[row][col+1];
				if(!seatView.isDisable() && !seatView.getSold() && ! seatView.getIsSelected()) {
					return seatView;
				}		
			}
		}else {
			if(checkRight) {
				// Rechts
				seatView = views[row][col+1];
				if(!seatView.isDisable() && !seatView.getSold() && ! seatView.getIsSelected()) {
					return seatView;
				}		
			}
			
			if(checkLeft) {
				// Links
				seatView = views[row][col-1];
				if(!seatView.isDisable() && !seatView.getSold() && ! seatView.getIsSelected()) {
					return seatView;
				}			
			}
		}
		
		if(checkBack) {
			// Hinten
			seatView = views[row+1][col];
			if(!seatView.isDisable() && !seatView.getSold() && ! seatView.getIsSelected()) {
				return seatView;
			}			
		}
			
		if(checkFront) {
			// Vorne
			seatView = views[row-1][col];
			if(!seatView.isDisable() && !seatView.getSold() && ! seatView.getIsSelected()) {
				return seatView;
			}			
		}
		
		// Alles rundherum besetzt - DEADLOCK nächsten Sitz nehmen
		return views[row][col-1];
	}
	
    private EventHandler<ActionEvent> commandAddTicketHandler = (evt) -> {
    	// Ticket hinzufügen
    	SeatView lastSeat = selectedSeats.get(selectedSeats.size() - 1);
    	Hall hall = vorstellung.getHall();
		int rows = hall.getRows();
	    int columns = hall.getColumns();
	    
	    SeatView bestSeat = checkNeibourhut(lastSeat, rows, columns);
	    Boolean ok = false;
	    while(!ok) {
	    	bestSeat = checkNeibourhut(lastSeat, rows, columns);
		    if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
		    	ok = true;
		    }
	    }
	    bestSeat.select();
	    
	   
    };
    
    private EventHandler<ActionEvent> commandBuyHandler = (evt) -> {
    	// Booking erstellen und weiter zu zahlen
    	Booking booking = new Booking();
    	booking.setEvent(vorstellung);
    	booking.setTickets(ticketData);    	
    	// Aktuelle Reservierung setzten
    	//Main.cinemaProgrammService.setCurrentReservation(booking);
    	try {
			Main.startPayment(booking);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    };

}
