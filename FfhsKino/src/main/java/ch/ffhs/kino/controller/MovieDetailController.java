package ch.ffhs.kino.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.service.CinemaProgrammServiceMock;
import ch.ffhs.kino.table.model.ProgrammTableColumnFactory;
import ch.ffhs.kino.table.model.ProgrammTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	 * TODO: Denis Vorstellungsübersicht laden
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

	@FXML
	Label moviefskTitle;

	@FXML
	ImageView moviefskImage;

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

	private List<Vorstellung> programm;

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
		initMovieInfo();
		CinemaProgrammServiceMock mock = new CinemaProgrammServiceMock();
		programm = mock.getProgrammByMovie(movie);

		initRows();

	}

	private final ObservableList<ProgrammTableModel> data = FXCollections.observableArrayList();

	protected void initRows() {
		data.clear();
		for (Vorstellung vorstellung : programm) {

			ProgrammTableModel e = new ProgrammTableModel(vorstellung.getShow().getMovie().getTitle(),
					vorstellung.getShow().getLanguage().getText(), vorstellung.getHall().getHallName(),
					vorstellung.getShow().getMovie().getGenreText(), vorstellung.getType().isThreeD(),
					vorstellung.getShow().getMovie());
			if (!data.contains(e)) {
				data.add(e);
			} else {

				for (int i = 0; i < data.size(); i++) {

					if (data.get(i).equals(e)) {

						data.get(i).addShowTime(vorstellung);
					}
				}
			}

		}
		Collections.sort(data, new Comparator<ProgrammTableModel>() {

			@Override
			public int compare(ProgrammTableModel o1, ProgrammTableModel o2) {
				return o1.getConcatID().compareTo(o2.getConcatID());
			}
		});

		if (data.size() > 0) {
			data.get(0).setOdd(true);
			for (int i = 1; i < data.size(); i++) {
				if (data.get(i).getFilm().equals(data.get(i - 1).getFilm())) {
					data.get(i).setOdd(data.get(i - 1).isOdd());
				} else {

					if (data.get(i - 1).isOdd()) {
						data.get(i).setOdd(false);
					} else {
						data.get(i).setOdd(true);
					}

				}
			}
		}
		vorstellungenTable.setItems(data);
		vorstellungenTable.refresh();

	}

	@FXML
	protected void initialize() {
		initTable();
	}

	private void initTable() {

		ProgrammTableColumnFactory programmTableColumnFactory = new ProgrammTableColumnFactory();
		vorstellungenTable.getColumns().add(programmTableColumnFactory.createColumnThreeD());
		vorstellungenTable.getColumns().add(programmTableColumnFactory.createColumnLanguage());
		vorstellungenTable.getColumns().add(programmTableColumnFactory.createColumnHall());

		GregorianCalendar heute = new GregorianCalendar();

		for (int i = 0; i < 7; i++) {
			heute.add(GregorianCalendar.DAY_OF_YEAR, 1);

			SimpleDateFormat fmt = new SimpleDateFormat("EEE dd.MM");

			String string = fmt.format(heute.getTime());

			vorstellungenTable.getColumns().add(programmTableColumnFactory.createColumnTime(string, heute.getTime()));

		}

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
		moviefskTitle.setText("FSK: ab " + movie.getAltersfreigabe() + "J freigegeben");

	}

	private void setAlterfreigabe() {
		String fskPath = "/ch/ffhs/kino/images/FSK_ab_%s.png";
		try {

			String format = String.format(fskPath, movie.getAltersfreigabe());
			moviefskImage.setImage(new Image(getClass().getResourceAsStream(format)));
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
		ControllerUtils.breadcrumbAction(event.getSource());

	}

}
