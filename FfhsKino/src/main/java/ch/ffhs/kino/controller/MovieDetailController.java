package ch.ffhs.kino.controller;

import ch.ffhs.kino.model.Movie;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

public class MovieDetailController {

	Movie movie;

	 public static final String CHROME_41_USER_AGENT =
	            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

	
	@FXML
	WebView trailer = new WebView();

	@FXML
	public void startTrailer(MouseEvent event) {

		String content_Url = "http://www.youtube.com/watch?v=qWxNb17aArw";
		trailer.getEngine().setUserAgent(
	                CHROME_41_USER_AGENT
	        );
		trailer.getEngine().load(content_Url);
		trailer.setPrefSize(640, 390);
		
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@FXML
	protected void initialize() {
	}

}
