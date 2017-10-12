package ch.ffhs.kino.controller;

import ch.ffhs.kino.model.Vorstellung;
import javafx.fxml.FXML;

public class VorstellungController {

	Vorstellung vorstellung;

	public Vorstellung getVorstellung() {
		return vorstellung;
	}

	public void setVorstellung(Vorstellung vorstellung) {
		this.vorstellung = vorstellung;
	}

	@FXML
	protected void initialize() {
	}

}
