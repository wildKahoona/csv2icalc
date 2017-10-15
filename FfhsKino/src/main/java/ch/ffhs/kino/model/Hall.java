package ch.ffhs.kino.model;

import ch.ffhs.kino.model.Cinema.CinemaPlaces;

public class Hall {

	private String hallName;
	private CinemaPlaces cinema;
	private int rows;
	private int columns;
	private Boolean[][] seatPlan;

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
		
	public Boolean[][] getSeatPlan() {
		return seatPlan;
	}

	public void configureSeatPlan(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		seatPlan = new Boolean[rows][columns];
		for (int row = 0; row < (rows ); row++) {
	    	for (int column = 0; column < (columns ); column++) {
	    		seatPlan[row][column] = true;
	    	}
		} 
	}
	
	public void setSeatNotAvailable(int row, int column){
		this.seatPlan[row][column] = false;
	}

	@Override
	public String toString() {
		return "Hall [hallName=" + hallName + ", cinema=" + cinema + "]";
	}
}
