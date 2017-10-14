package ch.ffhs.kino.controller;

import java.io.IOException;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class MovieDetailController {

	/**
	 * Denis TODO: +Navigation TODO: Fertigstellen
	 */

	private Movie movie;

	public static final String CHROME_41_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

	@FXML
	WebView movieTrailerView;

	@FXML
	Label movieTitle;

	@FXML
	Label movieLenght;

	

	@FXML
	ImageView movieImage;

	@FXML
	Label movieCriticsValue;

	@FXML
	ImageView movieCriticsStarImage;

	@FXML
	TableView vorstellungenTable;

	@FXML
	Label movieGenre;

	@FXML
	Label movieOriginalsprache;

	@FXML
	Label movieUntertitel;

	@FXML
	Label movieRegie;

	@FXML
	Label movieActors;

	@FXML
	VBox trailerContainer;

	@FXML
	VBox detailContainer;

	@FXML
	Label movieDescription;

	@FXML
	Hyperlink movieLink;

	@FXML Label moviefskTitle;

	@FXML ImageView moviefskImage;

	@FXML
	public void startTrailer(MouseEvent event) {

		detailContainer.setVisible(false);
		String content_Url = "http://www.youtube.com/watch?v=qWxNb17aArw";
		content_Url = movie.getTrailer();
		movieTrailerView.getEngine().setUserAgent(CHROME_41_USER_AGENT);
		movieTrailerView.getEngine().load(content_Url);
		movieTrailerView.setPrefSize(640, 390);

		trailerContainer.setVisible(true);

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

		String imageRessource = movie.getImageRessource();
		if (imageRessource != null) {
			movieImage.setImage(new Image(getClass().getResourceAsStream(imageRessource)));
		}
		movieTitle.setText(movie.getTitle());
		movieLenght.setText(String.format("Länge: %S min.", movie.getLaengeMin()));

		// FIXME:USE FSK PICTURE
		// fskImage.setImage(new
		// Image(getClass().getResourceAsStream(movie.getAltersfreigabe())));
		movieGenre.setText(movie.getGenreText().toString());
		movieDescription.setText(movie.getDesc());

		if (movie.getWebseite() == null || movie.getWebseite().isEmpty()) {
			movieLink.setVisible(false);

		}
		movieActors.setText(movie.getActorsAsString());
		movieRegie.setText(movie.getRegie());
		movieUntertitel.setText(movie.getSubtitle().getLongText());
		movieOriginalsprache.setText(movie.getOriginalLanguage().getLongText());

		WritableImage newImage = getCropedCriticsStars();

		movieCriticsStarImage.setImage(newImage);
		movieCriticsValue.setText(String.valueOf(movie.getCriticsStar()));

		setAlterfreigabe();
		moviefskImage.setAccessibleText(movie.getAltersfreigabe());
		moviefskTitle.setText("FSK: ab "+movie.getAltersfreigabe()+" freigegeben");


	}

	private void setAlterfreigabe() {
		String fskPath = "/ch/ffhs/kino/images/FSK_ab_%s.png";
		try {

			String format = String.format(fskPath, movie.getAltersfreigabe());
			moviefskImage.setImage(
					new Image(getClass().getResourceAsStream(format)));
		} catch (Exception e) {
			moviefskImage.setImage(new Image(getClass().getResourceAsStream(String.format(fskPath, 0))));

		}
	}

	private WritableImage getCropedCriticsStars() {
		try {

			Image value = new Image(getClass().getResourceAsStream("/ch/ffhs/kino/layout/images/five-stars.png"));
			int getCriticsWidth = (int) (value.getWidth() * 0.2 * movie.getCriticsStar());
			PixelReader reader = value.getPixelReader();
			WritableImage newImage = new WritableImage(reader, 0, 0, getCriticsWidth, (int) value.getHeight());
			return newImage;
		} catch (Exception e) {
			return null;
		}
	}

	@FXML
	public void closeTrailer() {
		trailerContainer.setVisible(false);
		detailContainer.setVisible(true);
		movieTrailerView.getEngine().load("about:blank");

	}

	@FXML
	public void webseiteStart() {

		Main.getHostServ().showDocument(movie.getWebseite());

	}

	@FXML
	public void breadcrumbAction(MouseEvent event) {
		Object source = event.getSource();
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
						Main.startVorstellung(null);

					}

				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
