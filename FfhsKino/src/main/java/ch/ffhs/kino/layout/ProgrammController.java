package ch.ffhs.kino.layout;

import java.util.ArrayList;
import java.util.List;

import ch.ffhs.kino.model.Event;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class ProgrammController {

	private List<Event> programm = new ArrayList<Event>();

	public void setProgramm(List<Event> programm) {
		this.programm = programm;
	}

	@FXML
	protected void initalize() {

		
		
	}

	@FXML public void search(KeyEvent event) {
		

		
	}

}
