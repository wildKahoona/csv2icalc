package ch.ffhs.kino.model;

import java.util.List;

import ch.ffhs.kino.service.CinemaProgrammServiceMock;

public class Booking {

	private List<Ticket> tickets;
	private Vorstellung event;
	private long sessionRemainTime;

	public Booking() {
		sessionRemainTime = CinemaProgrammServiceMock.SESSION_TIME;
	}
	
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

	public long getSessionRemainTime() {
		return sessionRemainTime;
	}

	public void setSessionRemainTime(long sessionRemainTime) {
		this.sessionRemainTime = sessionRemainTime;
	}
}
