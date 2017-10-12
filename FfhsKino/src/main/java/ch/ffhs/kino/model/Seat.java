package ch.ffhs.kino.model;

public class Seat {

	public enum SeatType {
		NORMAL, PREMIUM, HANDYCAP, NONE;
	}

	private Integer row;
	private Integer seatnumber;
	private SeatType type = SeatType.NORMAL;

	public Seat(Integer row, Integer seatnumber) {
		new Seat(row, seatnumber, SeatType.NORMAL);
	}

	public Seat(Integer row, Integer seatnumber, SeatType type) {
		this.row = row;
		this.seatnumber = seatnumber;
		this.type = type;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getSeatnumber() {
		return seatnumber;
	}

	public void setSeatnumber(Integer seatnumber) {
		this.seatnumber = seatnumber;
	}

	@Override
	public String toString() {
		return "Reihe " + getRow() + ", Platz " + getSeatnumber();
	}

}
