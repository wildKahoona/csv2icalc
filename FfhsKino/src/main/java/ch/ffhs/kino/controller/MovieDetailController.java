package ch.ffhs.kino.controller;

import ch.ffhs.kino.model.Movie;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

public class MovieDetailController {

	@FXML
	WebView trailer = new WebView();

	@FXML
	public void startTrailer(MouseEvent event) {
		trailer.getEngine().load("http://www.youtube.com/embed/utUPth77L_o?autoplay=1");

	}

	public void setMovie(Movie movie) {
		
	}

}
