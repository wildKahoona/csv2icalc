package ch.ffhs.kino.component;

import ch.ffhs.kino.model.Seat;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class SeatView extends ImageView {
    
	static final double scale = 0.95;
    static DropShadow shadowhoover = new DropShadow(5, 4, 4, Color.rgb(50, 60, 50));
    static DropShadow shadowdown = new DropShadow(2, 2, 2, Color.rgb(50, 60, 50));
    
    static Image seatPic = null;
    static Image seatSoldPic = null;
    static Image seatSelectedPic = null;
    
    private Seat seat;
	
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private BooleanProperty sold = new SimpleBooleanProperty(false);
      
    public static void setSeatPic(Image image) { 
    	seatPic = image; 
    }
    
	public static void soldSeatPic(Image image) {
		seatSoldPic = image; 
	}
    
    public static void setSeatSelectedPic(Image image) { 
    	seatSelectedPic = image;
    }
    
    public SeatView(Seat seat) { 
    	super(seatPic); 
    	
    	this.seat = seat;
    	
        setScaleX(scale);
        setScaleY(scale);
        setEffect(shadowdown);
        setOnMouseEntered(m -> {
            setEffect(shadowhoover);
            setScaleX(scale*1.01);
            setScaleY(scale*1.01);
        });
        setOnMouseExited(m -> {
            setEffect(shadowdown);
            setScaleX(scale);
            setScaleY(scale);
        });
        setOnMouseClicked(m -> {             	
        	// Wenn dieses Sitz verkauft wurde, darf er nicht ausgewählt werden
	    	if (this.sold.get())
	          return;
	    	
	    	// Status des Sitzes umschalten
	    	this.toggle();
        });
    }

//	public void deselect() {
//		if (!this.selected.get()) {
//	      return;
//		}
//		this.selected.set(false);
//	}

	/**
	 * Einen Sitz selektieren
	*/
	public void select() {
	    this.selected.set(true);
	    setImage(seatSelectedPic);
	}

	/**
	 * Einen Sitz deselektieren
	 */
	public void deselect() {
		this.selected.set(false);
		setImage(seatPic);
	}
	
	public void setSold() {
		this.sold.set(true);
		setImage(seatSoldPic);
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
	
	public BooleanProperty getState() {
		return this.selected;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}
}
