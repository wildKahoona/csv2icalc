package ch.ffhs.kino.model;

import java.util.List;

public class Booking {

	private List<Ticket> tickets;
	private Event event;

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
