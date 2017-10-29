package ch.ffhs.kino.component;

import ch.ffhs.kino.model.Seat;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Die Darstellung eines Kinositzes.
 * Der Kinositz enthält seinen Zustand (selektiert oder nicht)
 * und zeigt entsprechend seinem Zustand das richtige Bild
 * 
 * @author Birgit Sturzenegger
 */
public class SeatView extends ImageView {
    
	static final double scale = 0.95;
    static DropShadow shadowhoover = new DropShadow(5, 4, 4, Color.rgb(50, 60, 50));
    static DropShadow shadowdown = new DropShadow(2, 2, 2, Color.rgb(50, 60, 50));
    static Image imageSelcectedSeat = null;
    
    private Image imageSeat = null;
    private Seat seat;
    private Boolean isSelected = false;
	
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private Boolean sold = false;
      
    public static void setImageSelcectedSeat(Image image) { 
    	imageSelcectedSeat = image; 
    }
    
    public SeatView(Seat seat) { 	
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
	    	if (this.sold)
	          return;
	    	
	    	// Status des Sitzes umschalten
	    	this.toggle();
        });
        
        
      
        this.setPreserveRatio(true);
    }

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}
	
	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public Boolean getSold() {
		return sold;
	}

	public void setSold() {
		sold = true;
	}
	
	public void setImageSeat(Image imageSeat) {
		setImage(imageSeat);
		this.imageSeat = imageSeat;
	}
	
	/**
	 * Einen Sitz selektieren
	*/
	public void select() {
	    if(!this.sold) {
	    	this.selected.set(true);
	    	setIsSelected(true);
		    setImage(imageSelcectedSeat);
	    }
	}

	/**
	 * Einen Sitz deselektieren
	 */
	public void deselect() {
		this.selected.set(false);
		setIsSelected(false);
		setImage(imageSeat);
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
}
