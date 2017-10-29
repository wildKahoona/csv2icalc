package ch.ffhs.kino.component;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;

public class TimerAnimation {

	private Timeline timeline;
	private long maxTime;
	
    private BooleanProperty timeElapsed = new SimpleBooleanProperty();
 
    public final Boolean getTimeElapsed(){return timeElapsed.get();}
 
    public final void setTimeElapsed(boolean value){timeElapsed.set(value);}
 
    public BooleanProperty timeElapsedProperty() {return timeElapsed;}
	 
    private LongProperty remainTime = new SimpleLongProperty();
 
    public final long getRemainTime(){return remainTime.get();}
 
    public final void setRemainTime(long value){remainTime.set(value);}
 
    public LongProperty remainTimeProperty() {return remainTime;}
    
	public TimerAnimation() {
		initTimeline();
	}
	
	public void initTimeline() {
		timeline = new Timeline(
			new KeyFrame(Duration.seconds(1), e -> {
		    	long remainTime = maxTime - System.currentTimeMillis();
		    	setRemainTime(remainTime);
		    	if(remainTime <= (long)0) {
		    		timeline.stop();
		    		Platform.runLater(new Runnable() {
		    		      @Override public void run() {
		    		    	  setTimeElapsed(true);
		    		      }
		    		});			    					    		
		    	} 
		    })
		);
		timeline.setCycleCount( Animation.INDEFINITE );
}

	public void startTimeAnimation(long remainTime){
		setTimeElapsed(false);
		maxTime = System.currentTimeMillis() + remainTime;
		timeline.play();
	}
	
	public void stopTimeAnimation(){
		timeline.stop();
	}
}
