package ch.ffhs.kino.layout;

import java.io.IOException;
import java.util.ArrayList;

import ch.ffhs.kino.controller.BookingConfirmController;
import ch.ffhs.kino.controller.MovieDetailController;
import ch.ffhs.kino.controller.MovieShowController;
import ch.ffhs.kino.controller.PaymentController;
import ch.ffhs.kino.controller.ProgrammController;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.service.CinemaProgrammServiceMock;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage primaryStage; // **Declare static Stage**
	private static double stageWidth;
	private static double stageHeight;
	private static final String CSS = "ch/ffhs/kino/layout/controlStyle.css";
	public static final CinemaProgrammServiceMock cinemaProgrammService = new CinemaProgrammServiceMock();

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

	@Override
	public void start(Stage primaryStage) throws Exception {
		setPrimaryStage(primaryStage);
				
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		stageWidth = visualBounds.getWidth();
		stageHeight = visualBounds.getHeight();
//		primaryStage.setMinWidth(480.0);
//		primaryStage.setMinHeight(600.0);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/layout/icon/appicon.jpg")));

		// startMovieDetail(cinemaProgrammService.getMovie());
		// startKinoProgramm();
		startMovieShow(cinemaProgrammService.getVorstellung());
		// startPayment(cinemaProgrammService.getBooking());
		// startBookingConfirm(cinemaProgrammService.getBooking());		
	}

	public static void main(String[] args) {
		launch(args);

	}

	public static void startKinoProgramm() throws IOException {
		primaryStage.setTitle("Kino REX - Programm");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/programm.fxml"));
		Scene scene = new Scene((GridPane) loader.load(), stageWidth, stageHeight);
		
		ProgrammController controller = loader.<ProgrammController>getController();
		controller.initVorstellungen(cinemaProgrammService.getProgramm());
		show(scene);

	}

	public static void startMovieDetail(Movie movie) throws IOException {
		primaryStage.setTitle("Kino REX - " + movie.getTitle());

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/movieDetail.fxml"));
		Scene scene = new Scene((GridPane) loader.load(), stageWidth, stageHeight);

		// Parameterübergabe
		MovieDetailController controller = loader.<MovieDetailController>getController();
		controller.setMovie(movie);
		show(scene);

	}

	public static void startMovieShow(Vorstellung vorstellung) throws IOException {
		primaryStage.setTitle("Kino REX - Vorstellung");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/movieShow.fxml"));
		Scene scene = new Scene((GridPane) loader.load(), stageWidth, stageHeight);
		
		// Parameterübergabe
		MovieShowController controller = loader.<MovieShowController>getController();
		Booking reservation = new Booking();
		reservation.setEvent(vorstellung);
		reservation.setTickets(new ArrayList<Ticket>());
		controller.setReservation(reservation);
		
		show(scene);
	}

	public static void startMovieShow(Booking booking) throws IOException {
		primaryStage.setTitle("Kino REX - Vorstellung");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/movieShow.fxml"));
		Scene scene = new Scene((GridPane) loader.load(), stageWidth, stageHeight);
		
		// Parameterübergabe
		MovieShowController controller = loader.<MovieShowController>getController();
		controller.setReservation(booking);	
		show(scene);
	}
	
	public static void startPayment(Booking booking) throws IOException {
		primaryStage.setTitle("Kino REX - Ticket");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/payment.fxml"));
		Scene scene = new Scene((GridPane) loader.load(), stageWidth, stageHeight);
		
		// Parameterübergabe
		PaymentController controller = loader.<PaymentController>getController();
		controller.setBooking(booking);
		show(scene);
	}

	public static void startBookingConfirm(Booking booking) throws IOException {
		primaryStage.setTitle("Kino REX - Ticket");
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/ch/ffhs/kino/layout/bookingConfirm.fxml"));
		Scene scene = new Scene((GridPane) loader.load(), stageWidth, stageHeight);

		// Parameterübergabe
		BookingConfirmController controller = loader.<BookingConfirmController>getController();
		Main.cinemaProgrammService.addBooking(booking); // Speichern
		controller.setBooking(booking);
		show(scene);
	}

	private static void show(Scene scene) {
		scene.getStylesheets().add(CSS);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}