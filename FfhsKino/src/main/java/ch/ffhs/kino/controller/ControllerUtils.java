package ch.ffhs.kino.controller;

import java.io.IOException;

import ch.ffhs.kino.layout.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerUtils {

	public static void breadcrumbAction(Object source) {

		try {
			if (source instanceof Label) {
				Label label = (Label) source;
				if (!label.isDisable()) {
					String id = label.getId();
					if (id.equals("bc_programm")) {
						Main.startKinoProgramm();
						// } else if (id.equals("bc_vorstellung")) {
						// Main.startMovieShow(null);
					} else if (id.equals("bc_bezahlen")) {
						Main.startPayment(null);
						// } else if (id.equals("bc_sitzplatz")) {
						// Main.startMovieShow(null);
					}
				}
			} else if (source instanceof Button) {
				Main.startKinoProgramm();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Alert getSessionTimeOverMsg() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Reservierungszeit abgelaufen");
		alert.setHeaderText("Bitte w�hlen Sie neue Pl�tze");
		alert.setContentText("Die Reservierungszeit ist abgelaufen, daher wurden Ihre Pl�tze freigegeben.");
		return alert;
	}
}
