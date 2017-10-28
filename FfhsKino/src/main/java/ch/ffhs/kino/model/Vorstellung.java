package ch.ffhs.kino.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.ffhs.kino.model.Show.ShowType;

public class Vorstellung {

	private int id;
	private Show show;
	private Date date;
	private Hall hall;
	private ShowType type = ShowType.NORMAL;
	private List<Booking> bookings;
	// Boolean[][] soldList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
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
		if (bookings == null) {
			bookings = new ArrayList<Booking>();
		}
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	@Override
	public String toString() {
		return "Event [show=" + show + ", date=" + date + ", hall=" + hall + ", type=" + type + "]\n";
	}

	public String getFormatDay() {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(getDate());
	}
}
