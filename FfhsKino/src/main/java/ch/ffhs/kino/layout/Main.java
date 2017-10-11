package ch.ffhs.kino.layout;

import java.io.IOException;

import ch.ffhs.kino.model.Event;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.service.CinemaProgrammService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage primaryStage; // **Declare static Stage**
	private static final int STAGE_WIDTH = 800;
	private static final String CSS = "ch/ffhs/kino/layout/controlStyle.css";

	private void setPrimaryStage(Stage stage) {
		Main.primaryStage = stage;
	}

	static public Stage getPrimaryStage() {
		return Main.primaryStage;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setPrimaryStage(primaryStage);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/layout/icon/appicon.jpg")));

		Movie movie = new Movie();
		movie.setTitle("BladeRunner");
		startMovieDetail(movie);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void startKinoProgramm() throws IOException {
		primaryStage.setTitle("Kino REX - Programm");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/programm.fxml"));
		Scene scene = new Scene((Pane) loader.load());
		ProgrammController controller = loader.<ProgrammController>getController();
		CinemaProgrammService cinemaProgrammService = new CinemaProgrammService();
		controller.setProgramm(cinemaProgrammService.getProgramm());
		show(scene);

	}

	public static void startMovieDetail(Movie movie) throws IOException {
		primaryStage.setTitle("Kino REX - " + movie.getTitle());

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/movieDetail.fxml"));
		Scene scene = new Scene((TitledPane) loader.load());
		// Parameterübergabe
		MovieDetailController controller = loader.<MovieDetailController>getController();
		controller.setMovie(movie);
		show(scene);

	}

	public static void startVorstellung(Event event) throws IOException {
		primaryStage.setTitle("Kino REX - Vorstellung");

		Pane root = FXMLLoader.load(Main.class.getResource("/ch/ffhs/kino/layout/vorstellung.fxml"));
		Scene scene = new Scene(root);
		FXMLLoader fxmlLoader = new FXMLLoader();
		// Parameterübergabe
		VorstellungController controller = fxmlLoader.<VorstellungController>getController();
		controller.setEvent(event);
		show(scene);

	}

	public static void startTicketZahlen(Movie movie) throws IOException {
		primaryStage.setTitle("Kino REX - Ticket");

		Pane root = FXMLLoader.load(Main.class.getResource("/ch/ffhs/kino/layout/fxml_ticketsZahlen.fxml"));
		Scene scene = new Scene(root);
		FXMLLoader fxmlLoader = new FXMLLoader();
		// Parameterübergabe
		MovieDetailController controller = fxmlLoader.<MovieDetailController>getController();
		controller.setMovie(movie);
		show(scene);

	}

	private static void show(Scene scene) {
		primaryStage.setWidth(STAGE_WIDTH);
		scene.getStylesheets().add(CSS);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

}