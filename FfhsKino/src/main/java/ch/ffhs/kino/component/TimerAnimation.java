package ch.ffhs.kino.component;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import ch.ffhs.kino.layout.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerAnimation {

	private Timeline timeline;
	private long endTime;
	private ObservableList<SeatView> selectedSeats;
	
	public TimerAnimation(ObservableList<SeatView> selectedSeats) {
		this.selectedSeats = selectedSeats;
	}
	
	// TODO: wenn der Timer abgleaufen ist, müssen
	// - Alle ausgewählten Sitze deselektiert werden
	// - Alle Tickets gelöscht werden
	// - Eine Meldung an den Benutzer 
	// - Vorhandene Reservierung gelöscht (oder aktualisiert) werden (beim PaymentController)
	// - Die Buttons (Kaufen, Alle Tickets löschen, hinzufügen) disabled werden
	public void initTimeline(Label lbTimer) {
		timeline = new Timeline(
			new KeyFrame(Duration.seconds(1), e -> {
		    	long diff = endTime - System.currentTimeMillis();
		    	if(diff <= (long)0) {
		    		timeline.stop();
		    		Platform.runLater(new Runnable() {
		    		      @Override public void run() {
		    		    	  // Ausgewählte Sitze freigeben
		    		    	  clearSelectedSeats();
		    		    	  // Reservierung löschen
		    		    	  Main.cinemaProgrammService.setCurrentReservation(null);
		    		    	  
		    		    	  // Meldung an den Benutzer
			  	    		  Alert alert = new Alert(AlertType.WARNING);
			  	    		  alert.setTitle("Reservierungszeit abgelaufen");
			  	    		  alert.setHeaderText("Bitte wählen Sie neue Plätze");
			  	    		  alert.setContentText("Die Reservierungszeit ist abgelaufen, daher wurden Ihre Plätze freigegeben.");
			  	    		  alert.showAndWait();
		    		      }
		    		});			    					    		
		    	}else {
		    		SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
		    		lbTimer.setText(fmt.format(diff));
		    	} 
		    })
		);
		timeline.setCycleCount( Animation.INDEFINITE );
}

	public void startTimeAnimation(){
		setEndTime(System.currentTimeMillis() + (600 * 1000)); // 10 Minuten
		//endTime = System.currentTimeMillis() + (6 * 1000);
		timeline.play();
	}
	
	public void stopTimeAnimation(){
		timeline.stop();
	}
	
	private void clearSelectedSeats() {
		// ACHTUNG: NICHT THREAD-SICHER (hat mich viele Stunden gekostet)!!!
//		selectedSeats.forEach((seat) -> { 
//			seat.deselect();
//		});
//		selectedSeats.clear();
		
		// THREAD-SICHER
		Iterator<SeatView> seats = this.selectedSeats.iterator();
		while (seats.hasNext()) {
			SeatView seat = seats.next();
			seats.remove();
			seat.deselect();
		}
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
