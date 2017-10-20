package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import ch.ffhs.kino.component.SeatView;
import ch.ffhs.kino.component.TicketTable;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.model.Seat.SeatType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

// TODO: Scrollbar: Summe soll nicht scrollen, sondern fix bleiben
// 		 TimerAnimation-Problem lösen
public class MovieShowController {

	@FXML
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());
	}
	
	@FXML
	private Label lbMovieShow;
	
	@FXML
	private GridPane gridHall;
	
	@FXML
	private GridPane gridTimer;
	
	@FXML
	private VBox gridTickets;	
	
	@FXML
	private VBox gridSum;
		
	@FXML
	private Label lbTimer;
	
	@FXML
	private Button btnDeleteTickets;
	
	@FXML
	private Button btnAddTicket;
	
	@FXML
	private Button btnBuy;
	
	private Vorstellung movieShow;
	private Booking reservation;
	private Hall hall;
	private SeatView seatView[][];
	private Boolean autoTicketOn = true;
	private Timeline timeline;
	private long sessionRemainTime;

	@FXML
	public void initialize() {		
		loadData();
		setTitle();
		renderTicketTable();	
		initTimerAnimation();
		
		btnDeleteTickets.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		//btnAddTicket.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		btnBuy.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		GridPane.setHalignment(btnBuy, HPos.RIGHT);
		GridPane.setHgrow(btnBuy, Priority.ALWAYS);
	}

	/**
	 * Die Liste der ausgewählten Sitze
	 */
	private ObservableList<SeatView> selectedSeats = FXCollections.observableArrayList();
	
	/**
     * Die Liste der Tickets
     */
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	
	private void loadData() {
		Booking reservation = Main.cinemaProgrammService.getCurrentReservation();
		setReservation(reservation);
		
		Vorstellung movieShow;		
		if(reservation != null)
			movieShow = this.reservation.getEvent();
		else
			movieShow = Main.cinemaProgrammService.getVorstellung();
		
		setMovieShow(movieShow);
		setHall(movieShow.getHall());
		renderSeatView();
		renderReservedSeats();
		renderBookedSeats();
	}
	
	private void setTitle() {
		String movieTitle = movieShow.getShow().getMovie().getTitle();
		String movieLanguage = movieShow.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		lbMovieShow.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(movieShow.getDate()) + ", " + getHall().getHallName());
	}

	private void renderSeatView() {
		int rows = getHall().getRows();
	    int columns = getHall().getColumns();
	    seatView = new SeatView[rows][];
	    SeatType[][] seatPlan = hall.getSeatPlan();

	    ReadOnlyDoubleProperty gridWidth = gridHall.widthProperty();
	    ReadOnlyDoubleProperty gridHeight = gridHall.heightProperty();
	    
	    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
	    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
	    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
	    
	    // Bilder der Sitzplätze laden
	    Image selectedSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSelected_small.png"));
	    Image normalSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seat_small.png"));
	    Image premiumSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatPremium_small.png"));
	    Image handicapSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatHandicap_small.png"));
	    
	    // Bild für den ausgewählten Sitz für alle Objekte setzen	    
	    SeatView.setImageSelcectedSeat(selectedSeat);
	   	    
        for (int r = 0; r < rows; r++) {
        	seatView[r] = new SeatView[columns];          
            for (int c = 0; c < columns; c++) {
                Seat seat = new Seat(r, c);
            	SeatView view = new SeatView(seat);         	
            	seatView[r][c] = view;
            	
            	// Je nach Sitz-Typ ein anderes Standardbild nehmen            	
            	if(seatPlan[r][c] == SeatType.NONE){
            		view.setDisable(true);
            		view.setOpacity(0);
            		continue;
    	    	}else if (seatPlan[r][c] == SeatType.HANDYCAP){
    	    		view.setImageSeat(handicapSeat);
    	    	}else if (seatPlan[r][c] == SeatType.PREMIUM){
    	    		view.setImageSeat(premiumSeat);
    	    	}else {
    	    		view.setImageSeat(normalSeat);
    	    	}

            	// ImageViews are not resizeable, but you could use a parent that is resizeable and bind the fitWidth and fitHeight properties 
            	// to the size of the parent using expression binding          	    	
            	view.fitWidthProperty().bind(size);
            	view.fitHeightProperty().bind(size);
            	  	           	
            	view.getState().addListener((e, oldValue, newValue) -> {
		        	if (newValue) {
			        	  if(selectedSeats.size() == 0)
			        		  timeline.play();
			        	  selectedSeats.add(view);
			        	  
			        	  // Ticket für diesen Sitzplatz hinzufügen
			        	  if(autoTicketOn)
			        	  {
			        		  Ticket ticket = new Ticket(seat);
			        		  ticketData.add(ticket);
			        	  }
			          }
			          else {
			            this.selectedSeats.remove(view);
			            if(selectedSeats.size() == 0)
			            	timeline.stop();
			            
			            // Ticket aus der Liste entfernen
			            Optional<Ticket> removeTicket = ticketData.stream().
			            		filter(x -> x.getSeat().equals(seat)).findFirst();  			            
			            if(removeTicket.isPresent())
			            	ticketData.remove(removeTicket.get());
			          }		        		
    	        });
            	              
            	gridHall.add(seatView[r][c], c, r);
            }
        }
	}
	
	private void renderReservedSeats() {
		autoTicketOn = false;
	    if(this.reservation != null) {
	    	for(Ticket ticket : reservation.getTickets()) {
	    		ticketData.add(ticket);
	    		Seat seat = ticket.getSeat();
	    		SeatView view = seatView[seat.getSeatRow()][seat.getSeatColumn()];
	    		if (view != null)
	    			view.select();
	    	}
	    }
	    autoTicketOn = true;
	}
	  
	private void renderBookedSeats() {
	    List<Booking> bookings = movieShow.getBookings();
	    if(bookings.isEmpty()) return;
	    Image soldSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSold_small.png"));
	    for (Booking booking : bookings) {
	    	for(Ticket ticket : booking.getTickets()) {
	    		Seat seat = ticket.getSeat();
	    		SeatView view = seatView[seat.getSeatRow()][seat.getSeatColumn()];
	    		if (view != null) {
	    			view.setSold();
	    			view.setImageSeat(soldSeat);
	    		}
	    	}
	    }
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
	  
	private void renderTicketTable() {			
		TicketTable table = new TicketTable(ticketData, gridTickets);
		table.createTicketListener(seatView, gridTickets);
	}
	
	@FXML
	protected void deleteTickets(ActionEvent event) {
		clearSelectedSeats();
	}
	
	@FXML
	protected void addTicket(ActionEvent event) {
		SeatView lastSeat = null;
		
		if(selectedSeats.size() > 1)
			lastSeat = selectedSeats.get(selectedSeats.size() - 1);
		
		SeatView bestSeat = getBestSeat(lastSeat);
	    Boolean ok = false;
	    while(!ok) {
	    	bestSeat = getBestSeat(lastSeat);
		    if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
		    	ok = true;
		    }
	    }
	    bestSeat.select();
	}	
	
	@FXML
	protected void buyTickets(ActionEvent event) {
		Main.cinemaProgrammService.setSessionRemainTime(sessionRemainTime);
		timeline.stop();
		// Booking erstellen und weiter zu zahlen
    	Booking booking = new Booking();
    	booking.setEvent(getMovieShow());
    	booking.setTickets(ticketData);    	
    	// Aktuelle Reservierung setzen
    	Main.cinemaProgrammService.setCurrentReservation(booking);
    	try {
			Main.startPayment(booking);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private SeatView getBestSeat(SeatView view) {
		// TODO Auto-generated method stub
		int maxRows = getHall().getRows();
		int maxCols = getHall().getColumns();
		int middleRow = maxRows / 2;
		int middleCol = maxCols / 2;
		if(view == null)
			return seatView[middleRow][middleCol];
		
		int row = view.getSeat().getSeatRow();
		int col = view.getSeat().getSeatColumn();
		Boolean checkLeft = true;
		Boolean checkRight = true;
		Boolean checkFront = true;
		Boolean checkBack = true;
		Boolean firstLeft = true;
		
		
		
		if(col < middleCol)
			firstLeft = false;
		
		if(row == 0)
			checkFront = false;
		
		if(col == 0)
			checkLeft = false;
		
		if(row == (maxRows - 1))
			checkBack = false;
		
		if(col == (maxCols-1))
			checkRight = false;
		
		// Rechts oder Links?
		SeatView bestSeat;

		if(firstLeft)
		{
			if(checkLeft) {
				// Links
				bestSeat = seatView[row][col-1];
				if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
					return bestSeat;
				}			
			}
			
			if(checkRight) {
				// Rechts
				bestSeat = seatView[row][col+1];
				if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
					return bestSeat;
				}		
			}
		}else {
			if(checkRight) {
				// Rechts
				bestSeat = seatView[row][col+1];
				if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
					return bestSeat;
				}		
			}
			
			if(checkLeft) {
				// Links
				bestSeat = seatView[row][col-1];
				if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
					return bestSeat;
				}			
			}
		}
		
		if(checkBack) {
			// Hinten
			bestSeat = seatView[row+1][col];
			if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
				return bestSeat;
			}			
		}
			
		if(checkFront) {
			// Vorne
			bestSeat = seatView[row-1][col];
			if(!bestSeat.isDisable() && !bestSeat.getSold() && ! bestSeat.getIsSelected()) {
				return bestSeat;
			}			
		}
		
		// Alles rundherum besetzt - DEADLOCK nächsten Sitz nehmen (oder besser Zufallssitz)
		return seatView[row][col-1];
	}

	public void initTimerAnimation() {
		long sessionTime = Main.cinemaProgrammService.SESSION_TIME;	
		timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> {
					sessionRemainTime = sessionTime - System.currentTimeMillis();
			    	if(sessionRemainTime <= (long)0) {
			    		timeline.stop();
			    		Platform.runLater(new Runnable() {
			    		      @Override public void run() {
			    		    	  // TODO: wenn der Timer abgeleaufen ist, müssen
			    		    	  // - Alle ausgewählten Sitze deselektiert werden
			    		  		  // - Alle Tickets gelöscht werden
			    		  		  // - Eine Meldung an den Benutzer 
			    		  		  // - Vorhandene Reservierung gelöscht (oder aktualisiert) werden (beim PaymentController)
			    		  		  // - Die Buttons (Kaufen, Alle Tickets löschen, hinzufügen) disabled werden
			    		  		
			    		    	  
			    		    	  // Ausgewählte Sitze freigeben (Tickets werden automatisch entfern)
			    		    	  clearSelectedSeats();
			    		    	  
			    		    	  // Reservierung löschen
			    		    	  Main.cinemaProgrammService.setCurrentReservation(null);
			    		    	  
			    		    	  // Meldung an den Benutzer
			    		    	  Alert alert = ControllerUtils.getSessionTimeOverMsg();
			    		    	  alert.showAndWait();
			    		      }
			    		});			    					    		
			    	}else {
			    		SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
			    		lbTimer.setText(fmt.format(sessionRemainTime));
			    	} 
			    })
			);
			timeline.setCycleCount( Animation.INDEFINITE );		
	}
	
	public Vorstellung getMovieShow() {
		return movieShow;
	}
	
	public void setMovieShow(Vorstellung movieShow) {
		this.movieShow = movieShow;
	}

	public Booking getReservation() {
		return reservation;
	}

	public void setReservation(Booking reservation) {
		this.reservation = reservation;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}
}
