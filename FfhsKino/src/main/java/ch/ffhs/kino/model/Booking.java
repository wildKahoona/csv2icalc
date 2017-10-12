package ch.ffhs.kino.model;

import java.util.List;

public class Booking {

	private List<Ticket> tickets;
	private Vorstellung event;

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Vorstellung getEvent() {
		return event;
	}

	public void setEvent(Vorstellung event) {
		this.event = event;
	}

}