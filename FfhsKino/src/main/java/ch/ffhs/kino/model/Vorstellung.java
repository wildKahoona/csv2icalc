package ch.ffhs.kino.model;

import java.util.Date;
import java.util.List;

import ch.ffhs.kino.model.Show.ShowType;

public class Vorstellung {

	private Show show;
	private Date date;
	private Hall hall;
	private ShowType type = ShowType.NORMAL;
	private List<Booking> bookings;
	//Boolean[][] soldList;

	public ShowType getType() {
		return type;
	}

	public void setType(ShowType type) {
		this.type = type;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	@Override
	public String toString() {
		return "Event [show=" + show + ", date=" + date + ", hall=" + hall + ", type=" + type + "]\n";
	}
}
