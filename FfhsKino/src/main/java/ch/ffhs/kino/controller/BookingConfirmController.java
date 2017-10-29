package ch.ffhs.kino.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class BookingConfirmController {

	@FXML
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());
	}

	@FXML
	private Label vorstellungChoice;

	private Booking booking;

	@FXML
	public void initialize() {

	}

	public void setBooking(Booking booking) {
		this.booking = booking;
		setTitle();
		if (this.booking == null)
			return;
		List<Ticket> tickets = this.booking.getTickets();
		if (tickets == null || tickets.isEmpty())
			return;
	}

	private void setTitle() {
		Vorstellung event = booking.getEvent();
		String movieTitle = event.getShow().getMovie().getTitle();
		String movieLanguage = event.getShow().getLanguage().getText();

		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");
		vorstellungChoice.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(event.getDate()) + ", "
				+ event.getHall().getHallName());
	}
}
