package ch.ffhs.kino.controller;

import ch.ffhs.kino.model.Movie;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class MovieDetailController {

	/**
	 * Denis TODO: +Navigation TODO: Fertigstellen
	 */

	private Movie movie;

	public static final String CHROME_41_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

	@FXML
	WebView trailerView;

	@FXML
	Label movieTitle;

	@FXML
	Label movieLenght;

	@FXML
	ImageView fskImage;

	@FXML
	ImageView movieImage;

	@FXML
	Label criticsValue;

	@FXML
	ImageView criticsStarImage;

	@FXML
	TableView vorstellungenTable;

	@FXML
	Label genre;

	@FXML
	Label originalsprache;

	@FXML
	Label untertitel;

	@FXML
	Label regie;

	@FXML
	Label schausspieler;

	@FXML
	public void startTrailer(MouseEvent event) {

		String content_Url = "http://www.youtube.com/watch?v=qWxNb17aArw";
		content_Url = movie.getTrailer();
		trailerView.getEngine().setUserAgent(CHROME_41_USER_AGENT);
		trailerView.getEngine().load(content_Url);
		trailerView.setPrefSize(640, 390);

	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
		initMovieInfo();
	}

	@FXML
	protected void initialize() {

	}

	private void initMovieInfo() {
		movieImage.maxHeight(20);
		movieImage.maxWidth(20);
		movieImage.setImage(new Image(getClass().getResourceAsStream(movie.getImageRessource())));

		movieTitle.setText(movie.getTitle());
		movieLenght.setText(String.format("Lönge: %s min.", movie.getLaengeMin()));
		fskImage.setImage(new Image(getClass().getResourceAsStream(movie.getAltersfreigabe())));

	}

	@FXML
	public void closeTrailer() {
	}

	@FXML
	public void webseiteStart() {
	}

}
