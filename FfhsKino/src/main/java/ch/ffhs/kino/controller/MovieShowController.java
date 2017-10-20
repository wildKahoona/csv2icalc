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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

// TODO: Scrollbar: Summe soll nicht scrollen, sondern fix bleiben
// 		 TimerAnimation-Problem l�sen
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
	 * Die Liste der ausgew�hlten Sitze
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
	    
	    // Bilder der Sitzpl�tze laden
	    Image selectedSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSelected_small.png"));
	    Image normalSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seat_small.png"));
	    Image premiumSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatPremium_small.png"));
	    Image handicapSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatHandicap_small.png"));
	    
	    // Bild f�r den ausgew�hlten Sitz f�r alle Objekte setzen	    
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
			        	  
			        	  // Ticket f�r diesen Sitzplatz hinzuf�gen
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
	protected void addTicket(ActionEvent event) throws IOException {
		int maxRows = getHall().getRows();
		int maxCols = getHall().getColumns();
		
		SeatView bestSeat = null;
		if(selectedSeats.size() >= 1)
			bestSeat = selectedSeats.get(selectedSeats.size() - 1);
	
	    Boolean foundIt = false;
	    int countSearch = 0;
	    while(!foundIt) {	    	   	
	    	bestSeat = getBestSeat(bestSeat, maxRows, maxCols);
	    	if(bestSeat.getSeat().getSeatRow() == 13 && bestSeat.getSeat().getSeatColumn() == 13)
	    	{
	    		int i = 1;
	    	}
	    	if(isAvailable(bestSeat)) {
	    		foundIt = true;
	    		System.out.println("Reihe " + bestSeat.getSeat().getSeatRow() + "Platz " + bestSeat.getSeat().getSeatColumn());
	    	}
	    		
//	    	if(bestSeat == null)
//	    		break; // Sicherheitsausstieg
//	    	if(countSearch == 1000)
//	    		break; // Sicherheitsausstieg
	    }
	    
	    if(foundIt) {
	    	bestSeat.select();
	    } else {
	    	
//	    	int maxScope = maxRows + maxCols;
//	    	
//	    	List<Booking> bookings = getMovieShow().getBookings();
//	    	int countBookedTickets = 0;
//	    	for (Booking booking : bookings)
//		    	for(Ticket ticket : booking.getTickets())
//		    		countBookedTickets++;
	    	
	    	
	    	
	    	Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Hinweis");
			alert.setHeaderText("Es wurde kein Platz mehr gefunden.");
			alert.setContentText("Bitte w�hlen Sie einen Platz manuell aus.");
			alert.showAndWait();
	    }
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
	
	private SeatView getBestSeat(SeatView view, int maxRows, int maxCols) {
		// TODO Auto-generated method stub
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
		Boolean firstLeft = true;  // Links bevorzugen
		Boolean firstFront = true; // Vorne bevorzugen
		
		if(col < middleCol) 	// Rechts bevorzugen
			firstLeft = false;
		
		if(col == 0)			// Links nicht suchen (kein Sitz vorhanden)
			checkLeft = false;
		
		if(col == (maxCols-1)) 	// Rechts nicht suchen (kein Sitz vorhanden)
			checkRight = false;
		
		if(col < middleRow) 	// Hinten bevorzugen
			firstFront = false;
		
		if(row == 0) 			// Vorne nicht suchen (kein Sitz vorhanden)
			checkFront = false;

		if(row == (maxRows - 1)) // Hinten nicht suchen (kein Sitz vorhanden)
			checkBack = false;
		

		SeatView bestSeat;
		// Rechts oder Links?
		if(firstLeft)
		{
			if(checkLeft) {
				// Links
				bestSeat = seatView[row][col-1];
				if(checkRight || checkFront || checkBack){
					if(isAvailable(bestSeat))
						return bestSeat;
				} else {
					return bestSeat; // Kein besseren Sitz mehr m�glich
				}		
			}
			
			if(checkRight) {
				// Rechts
				bestSeat = seatView[row][col+1];						
				if(checkFront || checkBack) {
					if(isAvailable(bestSeat))
						return bestSeat;
				} else {
					return bestSeat; // Kein besseren Platz mehr m�glich
				}
											
			}
		} else {
			if(checkRight) {
				// Rechts
				bestSeat = seatView[row][col+1];
				if(checkLeft || checkFront || checkBack){
					if(isAvailable(bestSeat))
						return bestSeat;
				} else {
					return bestSeat; // Kein besseren Platz mehr m�glich
				}	
			}
			
			if(checkLeft) {
				// Links
				bestSeat = seatView[row][col-1];
				if(checkFront || checkBack) {
					if(isAvailable(bestSeat))
						return bestSeat;	
				} else {
					return bestSeat; // Kein besseren Sitz mehr m�glich
				}			
			}
		}
		
		// Vorne oder Hinten?
		if(firstFront)
		{
			if(checkFront) {
				// Vorne
				bestSeat = seatView[row-1][col];
				if(checkBack) {
					if(isAvailable(bestSeat))
						return bestSeat;	
				} else {
					return bestSeat; // Kein besseren Sitz mehr m�glich
				}				
			}
			
			if(checkBack) {
				// Hinten
				bestSeat = seatView[row+1][col];
				return bestSeat; // Kein besseren Sitz mehr m�glich
			}
			
		} else {
			if(checkBack) {
				// Hinten
				bestSeat = seatView[row+1][col];
				if(checkFront) {
					if(isAvailable(bestSeat)) {
						return bestSeat;
					}	
				} else {
					return bestSeat; // Kein besseren Sitz mehr m�glich
				}		
			}
				
			if(checkFront) {
				// Vorne
				bestSeat = seatView[row-1][col];				
				return bestSeat;		
			}
		}

		// Hier sollte das Programm nie hinkommen
		return null;
	}
	
	public Boolean isAvailable(SeatView seatView) {
		return (!seatView.isDisable() && !seatView.getSold() && ! seatView.getIsSelected());		
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
			    		    	  // TODO: wenn der Timer abgeleaufen ist, m�ssen
			    		    	  // - Alle ausgew�hlten Sitze deselektiert werden
			    		  		  // - Alle Tickets gel�scht werden
			    		  		  // - Eine Meldung an den Benutzer 
			    		  		  // - Vorhandene Reservierung gel�scht (oder aktualisiert) werden (beim PaymentController)
			    		  		  // - Die Buttons (Kaufen, Alle Tickets l�schen, hinzuf�gen) disabled werden
			    		  		
			    		    	  
			    		    	  // Ausgew�hlte Sitze freigeben (Tickets werden automatisch entfern)
			    		    	  clearSelectedSeats();
			    		    	  
			    		    	  // Reservierung l�schen
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
