package ch.ffhs.kino.controller;

import java.io.IOException;

import ch.ffhs.kino.layout.Main;
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
					} else if (id.equals("bc_vorstellung")) {
						Main.startVorstellung(null);
					} else if (id.equals("bc_bezahlen")) {
						Main.startTicketZahlen(null);
					} else if (id.equals("bc_sitzplatz")) {
						Main.startVorstellung();

					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
