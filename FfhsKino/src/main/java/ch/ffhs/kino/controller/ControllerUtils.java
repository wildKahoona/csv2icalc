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
						Main.startMovieShow(null);
					} else if (id.equals("bc_bezahlen")) {
						Main.startPayment(null);
					} else if (id.equals("bc_sitzplatz")) {
						Main.startMovieShow(null);

					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
