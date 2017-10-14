package ch.ffhs.kino.layout;

import java.io.IOException;

import ch.ffhs.kino.controller.MovieDetailController;
import ch.ffhs.kino.controller.ProgrammController;
import ch.ffhs.kino.controller.TicketZahlenController;
import ch.ffhs.kino.controller.VorstellungController;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.service.CinemaProgrammServiceMock;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage primaryStage; // **Declare static Stage**
	private static final int STAGE_WIDTH = 1100;
	private static final String CSS = "ch/ffhs/kino/layout/controlStyle.css";
	private static final CinemaProgrammServiceMock cinemaProgrammService = new CinemaProgrammServiceMock();

	private void setPrimaryStage(Stage stage) {
		Main.primaryStage = stage;
	}

	static public Stage getPrimaryStage() {
		return Main.primaryStage;
	}

	public static HostServices getHostServ() {
		Main m = new Main();
		return m.getHostServices();

	}

	static {
		Font.loadFont(Main.class.getResource("/ch/ffhs/kino/layout/font/fontawesome-webfont.ttf").toExternalForm(), 10);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setPrimaryStage(primaryStage);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/layout/icon/appicon.jpg")));

		// startMovieDetail(cinemaProgrammService.getMovie());
		startKinoProgramm();
		// startVorstellung(cinemaProgrammService.getVorstellung());
		// startTicketZahlen(cinemaProgrammService.getBooking());
	}

	public static void main(String[] args) {
		launch(args);

	}

	public static void startKinoProgramm() throws IOException {
		primaryStage.setTitle("Kino REX - Programm");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/programm.fxml"));
		Scene scene = new Scene((Pane) loader.load());
		ProgrammController controller = loader.<ProgrammController>getController();
		controller.setVorstellungen(cinemaProgrammService.getProgramm());
		show(scene);

	}

	public static void startMovieDetail(Movie movie) throws IOException {
		primaryStage.setTitle("Kino REX - " + movie.getTitle());

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/movieDetail.fxml"));
		Scene scene = new Scene((GridPane) loader.load());
		// Parameterübergabe
		MovieDetailController controller = loader.<MovieDetailController>getController();
		controller.setMovie(movie);
		show(scene);

	}

	public static void startVorstellung(Vorstellung vorstellung) throws IOException {
		primaryStage.setTitle("Kino REX - Vorstellung");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/vorstellung.fxml"));
		Scene scene = new Scene((GridPane) loader.load());

		// Parameterübergabe
		VorstellungController controller = loader.<VorstellungController>getController();
		controller.setVorstellung(vorstellung);
		show(scene);

	}

	public static void startTicketZahlen(Booking booking) throws IOException {
		primaryStage.setTitle("Kino REX - Ticket");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/ticketsZahlen.fxml"));
		Scene scene = new Scene((GridPane) loader.load());

		// Parameterübergabe
		TicketZahlenController controller = loader.<TicketZahlenController>getController();
		controller.setBooking(booking);
		show(scene);

	}

	private static void show(Scene scene) {
		primaryStage.setWidth(STAGE_WIDTH);
		scene.getStylesheets().add(CSS);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

}