package ch.ffhs.kino.model;

import ch.ffhs.kino.model.Cinema.CinemaPlaces;
import ch.ffhs.kino.model.Seat.SeatType;

public class Hall {

	private String hallName;
	private CinemaPlaces cinema;
	private int rows;
	private int columns;
	private SeatType[][] seatPlan;

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

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
		
	public SeatType[][] getSeatPlan() {
		return seatPlan;
	}

	public void setSeatPlan(SeatType[][] seatPlan) {
		this.seatPlan = seatPlan;
	}
	
	public void configureSeatPlan(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		seatPlan = new SeatType[rows][columns];
		for (int row = 0; row < (rows ); row++) {
	    	for (int column = 0; column < (columns ); column++) {
	    		seatPlan[row][column] = SeatType.NORMAL;
	    	}
		} 
	}

	public void setSeatType(int row, int column, SeatType seatType){
		this.seatPlan[row][column] = seatType;
	}
	
	@Override
	public String toString() {
		return "Hall [hallName=" + hallName + ", cinema=" + cinema + "]";
	}
}
