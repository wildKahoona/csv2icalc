package ch.ffhs.kino.model;

public class Seat {

	public enum SeatType {
		NORMAL, PREMIUM, HANDYCAP, NONE;
	}
	
	private Integer seatRow;
	private Integer seatColumn;
	private Integer seatNumber;
	
	public Seat(Integer seatRow, Integer seatColumn, Integer seatNumber) {
		this.seatRow = seatRow;
		this.seatColumn = seatColumn;
		this.seatNumber = seatNumber;
	}
	
	public Integer getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(Integer seatRow) {
		this.seatRow = seatRow;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
	}

	public Integer getSeatColumn() {
		return seatColumn;
	}

	public void setSeatColumn(Integer seatColumn) {
		this.seatColumn = seatColumn;
	}
	
	@Override
	public String toString() {
		return "Reihe " + (getSeatRow() + 1) + ", Platz " + (getSeatColumn() + 1);
	}
}
