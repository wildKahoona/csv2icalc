package ch.ffhs.kino.model;

import java.text.DecimalFormat;

public class Ticket {

	public enum TicketType {
		KIND(8.50, "Kind"), ADULT(19.00, "Erwachsene"), STUDENT(11.50, "Student"), SENIOR(9.00, "Senior");

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

	public String getCost() {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(getTicketType().getCost());

	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}
}
