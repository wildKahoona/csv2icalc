package ch.ffhs.kino.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.ffhs.kino.model.Cinema.CinemaPlaces;
import ch.ffhs.kino.model.Event;
import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Movie.GenreType;
import ch.ffhs.kino.model.Movie.MovieLanguage;
import ch.ffhs.kino.model.Show;

public class CinemaProgrammService {

	// Säle
	private final static Hall hallRex1 = new Hall("Saal 1", CinemaPlaces.KINO_REX);
	private final static Hall hallRex2 = new Hall("Saal 2", CinemaPlaces.KINO_REX);
	private final static Hall hallRex3 = new Hall("Saal 3", CinemaPlaces.KINO_REX);
	private final static Hall hallRex4 = new Hall("Saal 4", CinemaPlaces.KINO_REX);

	// Movies
	private Movie movie1;
	private Movie movie2;
	private Movie movie3;

	// Shows
	private Show show1;
	private Show show2;
	private Show show3;
	private Show show4;
	private Show show5;
	private Show show6;
	private Show show7;

	public List<Event> getProgramm() {

		// MockData

		initMovies();

		initShows();

		List<Event> events = new ArrayList<Event>();

		for (int i = 0; i < 200; i++) {

			double random = Math.random();
			double random2 = Math.random();
			Event event1 = new Event();
			event1.setDate(new Date(2017, Integer.valueOf((int) (random * 10)), Integer.valueOf((int) (random2 * 10)),
					21, 15));

			switch (i % 4) {
			case 0:
				event1.setHall(hallRex1);
				break;
			case 1:
				event1.setHall(hallRex2);
				break;
			case 2:
				event1.setHall(hallRex3);
				break;

			default:
				event1.setHall(hallRex4);
				break;
			}

			double random4 = Math.random() * 10;

			switch (i % 6) {
			case 0:
				event1.setShow(show1);
				break;
			case 1:
				event1.setShow(show2);
				break;
			case 2:
				event1.setShow(show3);
				break;
			case 3:
				event1.setShow(show4);
				break;
			case 4:
				event1.setShow(show5);
				break;
			case 5:
				event1.setShow(show6);
				break;
			default:
				event1.setShow(show7);
				break;
			}

			events.add(event1);

		}

		return events;
	}

	private void initShows() {
		show1 = new Show();
		show1.setLanguage(MovieLanguage.DEUTSCH);
		show1.setMovie(movie1);

		show2 = new Show();
		show2.setLanguage(MovieLanguage.ENGLISH);
		show2.setMovie(movie1);

		show3 = new Show();
		show3.setLanguage(MovieLanguage.DEUTSCH);
		show3.setMovie(movie2);

		show4 = new Show();
		show4.setLanguage(MovieLanguage.ENGLISH);
		show4.setMovie(movie2);

		show5 = new Show();
		show5.setLanguage(MovieLanguage.DEUTSCH);
		show5.setMovie(movie3);

		show6 = new Show();
		show6.setLanguage(MovieLanguage.FRANZOESISCH);
		show6.setMovie(movie3);

		show7 = new Show();
		show7.setLanguage(MovieLanguage.ENGLISH);
		show7.setMovie(movie3);
	}

	private void initMovies() {

		// Hereinspaziert
		movie1 = new Movie();
		movie1.setTitle("Hereinspaziert");
		movie1.setGenre(GenreType.COMEDY);
		movie1.setDesc(
				"Die neunköpfige Romafamilie im Garten des Linksintellektuellen Jean-Etienne stellt seine Überzeugungen auf die Probe.");
		movie1.setImageRessource("movies/mov20");
		movie1.setAltersfreigabe("12J");

		// Barry Seal - Only in America
		movie2 = new Movie();
		movie2.setTitle("Barry Seal - Only in America");
		movie2.setGenre(GenreType.ACTION, GenreType.COMEDY, GenreType.DRAMA, GenreType.THRILLER);
		movie2.setDesc(
				"Einige Waisenkinder finden ein Zuhause bei einem Puppenmacher. Schon bald geraten sie ins Visier einer seiner Kreationen.");
		movie2.setImageRessource("movies/mov4");
		movie2.setAltersfreigabe("14/12J");

		// Blade Runner 2049
		movie3 = new Movie();
		movie3.setTitle("Blade Runner 2049");
		movie3.setGenre(GenreType.SCIENCE_FICTION, GenreType.THRILLER);
		movie3.setDesc(
				"30 Jahre nach dem ersten Film fördert ein neuer Blade Runner ein lange unter Verschluss gehaltenes Geheimnis zu Tage.");
		movie3.setImageRessource("movies/mov23");
		movie3.setAltersfreigabe("14/12J");
		movie3.setTrailer("https://youtu.be/WQU8yrI6o9Q");
	}
}
