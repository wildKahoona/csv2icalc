package ch.ffhs.kino.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import ch.ffhs.kino.component.TicketTable;
import ch.ffhs.kino.component.TicketTableHeader;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Der BookingConfirmController behandelt die Bestätigung der Ticket-Bestellung
 * 
 * @author Birgit Sturzenegger
 */
public class BookingConfirmController {
	
	@FXML
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());
	}
	
	@FXML
	private Label vorstellungChoice;
	
	@FXML
	private VBox gridTickets;
	
	@FXML
	private	VBox gridSumTickets;
	
	@FXML
	public void initialize() {
		renderTicketTable();
	}
	
	private Booking booking;
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	
	public void setBooking(Booking booking) {
		this.booking = booking;
		setTitle();
		if(this.booking == null) return;
		List<Ticket> tickets = this.booking.getTickets();
		if(tickets == null) return;
		for(Ticket ticket : tickets){
			ticketData.add(ticket);
		}
	}
	
	private void setTitle() {
		Vorstellung event = booking.getEvent();
		String movieTitle = event.getShow().getMovie().getTitle();
		String movieLanguage = event.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		vorstellungChoice.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(event.getDate()) + ", " + event.getHall().getHallName());
	}
	
	private void renderTicketTable() {			
		TicketTableHeader ticketHeader = new TicketTableHeader(ticketData, gridSumTickets);
		ticketHeader.createTicketListener();
		TicketTable table = new TicketTable(ticketData, gridTickets);
		table.createTicketListener(null, true);
	}
}
