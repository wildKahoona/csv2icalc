package ch.ffhs.kino.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Rectangle;

public class Seat extends Rectangle {

	public enum SeatType {
		NORMAL, PREMIUM, HANDYCAP, NONE;
	}

	private Integer row;
	private Integer seatnumber;
	private SeatType type = SeatType.NORMAL;

	/**
	 * Indikator, ob der Sitz reserviert ist oder nicht.
	 */
	private BooleanProperty reserved = new SimpleBooleanProperty(false);
	  
	/**
	 * Indikator, ob der Sitz zur Reservierung ausgewählt ist oder nicht
	 */
	private BooleanProperty selected = new SimpleBooleanProperty(false);

	/**
	 * Indikator, ob  der Sitz gekauft wurde oder nicht.
	 */
	private BooleanProperty bought = new SimpleBooleanProperty(false);
	
	public Seat(Integer row, Integer seatnumber) {
		new Seat(row, seatnumber, SeatType.NORMAL);
	}

	public Seat(Integer row, Integer seatnumber, SeatType type) {
		this.row = row;
		this.seatnumber = seatnumber;
		this.type = type;
		
//		this.reserved.addListener((ob, ov, nv) -> {
//			if (nv) {
//	    this.getStyleClass().add("seat-reserved");
//	  }
//	  else {
//	    this.getStyleClass().remove("seat-reserved");
//	  }
//	});
		
		this.setOnMouseClicked(e -> {
			// Wenn dieser Sitz entweder reserviert oder gekauft wurde, darf er nicht mehr ausgewählt werde.
			if (this.reserved.get() || this.bought.get()) {
				return;
			}
			this.toggle();
		});
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

	/**
	 * Einen Sitz selektieren
	*/
	public void select() {
		this.reserved.set(false);
	    this.selected.set(true);
	}

	/**
	 * Einen Sitz de-selktieren
	 */
	public void deselect() {
		if (!this.selected.get()) {
	      return;
		}
		this.selected.set(false);
	}
	  
	/**
	 * Schaltet den ausgewählten Status eines Sitzes um.
	 */
	public void toggle() {
		if (!this.selected.get()) {
	      this.select();
	    }
	    else {
	      this.deselect();
	    }
	}

	/**
	 * Einen Sitz reservieren
	 */
	public void reserve() {
	    this.selected.set(false);
	    this.reserved.set(true);
	}
	  
	@Override
	public String toString() {
		return "Reihe " + getRow() + ", Platz " + getSeatnumber();
	}

}
