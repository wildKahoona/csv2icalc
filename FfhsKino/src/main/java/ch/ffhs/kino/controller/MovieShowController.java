package ch.ffhs.kino.controller;

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
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.model.Seat.SeatType;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

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
	private Label lbTimer;
	
	@FXML
	private Button btnDeleteTickets;
	
	@FXML
	private Button btnAddTicket;
	
	private Vorstellung movieShow;
	private Booking reservation;
	private Hall hall;
	private TimerAnimation timerAnimation;
	private SeatView seatView[][];
	private Boolean autoTicketOn = true;
	
	@FXML
	public void initialize() {		
		loadData();
		setTitle();
		
		timerAnimation = new TimerAnimation(selectedSeats);
		timerAnimation.initTimeline(lbTimer);
		
		btnDeleteTickets.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		//btnDeleteTickets.setOnAction(deleteTickets);
		
		//btnAddTicket.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		//btnAddTicket.setOnAction(addTicket);
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
		//Vorstellung event = booking.getEvent();
		Vorstellung event = Main.cinemaProgrammService.getVorstellung();
		String movieTitle = event.getShow().getMovie().getTitle();
		String movieLanguage = event.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		lbMovieShow.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(event.getDate()) + ", " + event.getHall().getHallName());
	}


	private void renderSeatView() {
		int rows = getHall().getRows();
	    int columns = getHall().getColumns();
	    SeatType[][] seatPlan = hall.getSeatPlan();

	    ReadOnlyDoubleProperty gridWidth = gridHall.widthProperty();
	    ReadOnlyDoubleProperty gridHeight = gridHall.heightProperty();
	    
	    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
	    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
	    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
	    
	    seatView = new SeatView[rows][];
	    
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
			        		  timerAnimation.startTimeAnimation();
			        	//StartTimeAnimation();
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
			            	timerAnimation.stopTimeAnimation();
			            	//StopTimeAnimation();
			            
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
	  
	
	@FXML
	protected void deleteTickets(ActionEvent event) {
		clearSelectedSeats();
	}
	
	@FXML
	protected void addTicket(ActionEvent event) {
		SeatView seat = getBestSeat();
	}	
	
	private SeatView getBestSeat() {
		// TODO Auto-generated method stub
		return null;
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
