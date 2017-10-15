package ch.ffhs.kino.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Rectangle;

public class Seat extends Rectangle {

	public enum SeatType {
		NORMAL, PREMIUM, HANDYCAP, NONE;
	}
	
	private Integer seatRow;
	private Integer seatColumn;
	private Integer seatNumber;
	//private SeatType type = SeatType.NORMAL;

	/**
	 * Indikator, ob der Sitz zur Reservierung ausgewählt ist oder nicht
	 */
	private BooleanProperty selected = new SimpleBooleanProperty(false);
	
	/**
	 * Indikator, ob  der Sitz bereits verkauft wurde oder nicht.
	 */
	private BooleanProperty sold = new SimpleBooleanProperty(false);
	
	public Seat(Integer seatRow, Integer seatColumn, Integer seatNumber) {
		this.seatRow = seatRow + 1;
		this.setSeatColumn(seatColumn + 1);
		this.seatNumber = seatNumber;
		
		this.getStyleClass().add("seat");
		
	    this.selected.addListener((value, oldValue, newValue) -> {
	    	if (newValue)
	          this.getStyleClass().add("seat-selected");
	        else
	          this.getStyleClass().remove("seat-selected");
	    	});

	      this.sold.addListener((value, oldValue, newValue) -> {
	        if (newValue)
				this.getStyleClass().add("seat-sold");
	        else
	          this.getStyleClass().remove("seat-sold");
	      });

	      this.setOnMouseClicked(e -> {
	        // Wenn dieses Sitz verkauft wurde, darf er nicht ausgewählt werden
	    	if (this.sold.get())
	          return;
	        this.toggle();
	      });
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
	
	/**
	 * Einen Sitz selektieren
	*/
	public void select() {
	    this.selected.set(true);
	}

	/**
	 * Einen Sitz deselektieren
	 */
	public void deselect() {
		if (!this.selected.get()) {
	      return;
		}
		this.selected.set(false);
	}
	  
	public void setSold() {
		this.sold.set(true);
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
	 * Status des Sitzes (ausgewählt oder nicht)
	 *
	 * @return Boolean Indikator, ob der Sitz ausgewählt ist oder nicht.
	 */
	public BooleanProperty getState() {
		return this.selected;
	}
	  
	@Override
	public String toString() {
		return "Reihe " + getSeatRow() + ", Platz " + getSeatColumn();
	}
}
