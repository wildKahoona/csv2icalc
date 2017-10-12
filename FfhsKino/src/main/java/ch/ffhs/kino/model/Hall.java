package ch.ffhs.kino.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import ch.ffhs.kino.model.Cinema.CinemaPlaces;

public class Hall {

	private String hallName;
	private CinemaPlaces cinema;

	private int row;
	private int columns;

	List<Seat> seatlist = new ArrayList<Seat>();

	public Hall() {

		seatlist.add(new Seat(1, 1));

	}

	public Hall(String hallName, CinemaPlaces cinema) {
		this.hallName = hallName;
		this.cinema = cinema;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public CinemaPlaces getCinema() {
		return cinema;
	}

	public void setCinema(CinemaPlaces cinema) {
		this.cinema = cinema;
	}

	@Override
	public String toString() {
		return "Hall [hallName=" + hallName + ", cinema=" + cinema + "]";
	}

}
