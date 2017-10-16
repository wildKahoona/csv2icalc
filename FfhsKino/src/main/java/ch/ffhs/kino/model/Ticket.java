package ch.ffhs.kino.model;

import java.text.DecimalFormat;

public class Ticket {

	public enum TicketType {
		KIND(8.5, "Kind"), ADULT(19.0, "Erwachsene"), STUDENT(11.5, "Student"), SENIOR(9, "Senior");

		private double cost;
		private String title;

		public double getCost() {
			return cost;
		}

		public void setCost(double cost) {
			this.cost = cost;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		TicketType(double cost, String title) {
			this.cost = cost;
			this.title = title;
		}
	}

	private Seat seat;
	private TicketType ticketType;
	private TicketPrice ticketPrice;
	private Booking booking;

	public Ticket(Seat seat) {
		this.seat = seat;
		this.ticketType = TicketType.ADULT; // Default
	}
	
	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	private String getCost() {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(getTicketType().getCost());

	}

	public TicketPrice getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(TicketPrice ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}
}
