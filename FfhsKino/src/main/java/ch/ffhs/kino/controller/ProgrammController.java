package ch.ffhs.kino.controller;

import java.util.ArrayList;
import java.util.List;

import ch.ffhs.kino.model.Vorstellung;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class ProgrammController {

	private List<Vorstellung> programm = new ArrayList<Vorstellung>();

	public void setProgramm(List<Vorstellung> programm) {
		this.programm = programm;
	}

	@FXML
	protected void initalize() {

		
		
	}

	@FXML public void search(KeyEvent event) {
		

		
	}

}
