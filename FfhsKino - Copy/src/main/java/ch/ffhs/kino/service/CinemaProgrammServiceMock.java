
package ch.ffhs.kino.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Cinema.CinemaPlaces;
import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Movie.GenreType;
import ch.ffhs.kino.model.Movie.MovieLanguage;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Seat.SeatType;
import ch.ffhs.kino.model.Show;
import ch.ffhs.kino.model.Show.ShowType;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import ch.ffhs.kino.model.Vorstellung;

/**
 * Stellt einen Mock-Service zur verfügung um die Daten auf den verschiedenen
 * Views darstellen zu können.
 * 
 * @author Denis Bittante
 *
 */
public class CinemaProgrammServiceMock {

	//public static final long SESSION_TIME = 50 * 1000; // 600 * 1000; // 10 Minuten
	public static final long SESSION_TIME = 600 * 1000; // 10 Minuten
	
	// Säle
	private final static Hall hallRex1 = new Hall("Minisaal", CinemaPlaces.KINO_REX);
	private final static Hall hallRex2 = new Hall("Mittelsaal", CinemaPlaces.KINO_REX);
	private final static Hall hallRex3 = new Hall("Megasaal", CinemaPlaces.KINO_REX);
	private final static Hall hallRex4 = new Hall("Open-Air", CinemaPlaces.KINO_REX);

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

	private List<Vorstellung> vorstellungen = new ArrayList<Vorstellung>();

	public CinemaProgrammServiceMock() {
		init();
	}

	public List<Vorstellung> getProgramm() {
		return vorstellungen;
	}

	public List<Vorstellung> getProgrammByMovie(Movie movie) {

		List<Vorstellung> vListe = new ArrayList<Vorstellung>();

		for (Vorstellung vorstellung : vorstellungen) {
			if (vorstellung.getShow().getMovie().getTitle().equals(movie.getTitle())) {
				vListe.add(vorstellung);
			}
		}

		return vListe;
	}

	public Vorstellung getVorstellung() {
		return vorstellungen.get(0);
	}

	public void addBooking(Booking booking) {
		int eventId = booking.getEvent().getId();
		Optional<Vorstellung> event = vorstellungen.stream().
				filter(x -> x.getId() == eventId).findFirst(); 
		if(event.isPresent()) {
			List<Booking> bookings = event.get().getBookings();
			bookings.add(booking);
			event.get().setBookings(bookings);
		}
	}

	private void init() {
		// MockData

		initMovies();

		initShows();

		initHalls();

		for (int i = 0; i < 200; i++) {

			Vorstellung event1 = new Vorstellung();
			event1.setId(i+1);

			// this week

			GregorianCalendar gregorianCalendar = new GregorianCalendar();

			switch (getRandomInt() % 3) {
			case 0:
				gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 21);
				gregorianCalendar.set(GregorianCalendar.MINUTE, 15);
				break;
			case 1:
				gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 15);
				gregorianCalendar.set(GregorianCalendar.MINUTE, 45);
				break;
			case 2:
				gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, 18);
				gregorianCalendar.set(GregorianCalendar.MINUTE, 30);
				break;
			}

			gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, i % 8);

			event1.setDate(gregorianCalendar.getTime());

			System.out.println(event1.getDate());

			switch (i % 7) {
			case 0:
				event1.setHall(hallRex1);
				break;
			case 1:
				event1.setHall(hallRex2);
				break;

			default:
				event1.setHall(hallRex4);
				break;
			}

			switch (getRandomInt() % 6) {
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
			switch (getRandomInt() % 3) {
			case 0:
				event1.setType(ShowType.THREE_D);
				break;
			default:
				event1.setType(ShowType.NORMAL);
				break;
			}
			vorstellungen.add(event1);
			
//			if(i == 3 || i == 5 || i == 20) {
//				addBooking(event1);
//				System.out.println(event1.getDate() + " " + event1.getShow().getMovie().getTitle());
//			}
		}
	}

	private int getRandomInt() {
		double random = Math.random();
		return (int) (random * 10);
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

		String imgPath = "/ch/ffhs/kino/movies/mov%s.jpg";

		// Hereinspaziert
		movie1 = new Movie();
		movie1.setTitle("Hereinspaziert");
		movie1.setGenre(GenreType.COMEDY);
		movie1.setDesc(
				"Die neunköpfige Romafamilie im Garten des Linksintellektuellen Jean-Etienne stellt seine Überzeugungen auf die Probe.");
		movie1.setImageRessource(String.format(imgPath, "20"));
		movie1.setAltersfreigabe("12J");
		String codemov1 = "bw1TdXyXT8Y";
		String urlmovie1 = "http://www.youtube.com/embed/" + codemov1 + "?rel=0;3&amp;autohide=1&amp;showinfo=0";
		movie1.setTrailer(urlmovie1);


		// Barry Seal - Only in America
		movie2 = new Movie();
		movie2.setTitle("Barry Seal - Only in America");
		movie2.setGenre(GenreType.ACTION, GenreType.COMEDY, GenreType.DRAMA, GenreType.THRILLER);
		movie2.setDesc(
				"Einige Waisenkinder finden ein Zuhause bei einem Puppenmacher. Schon bald geraten sie ins Visier einer seiner Kreationen.");
		movie2.setImageRessource(String.format(imgPath, "4"));
		movie2.setAltersfreigabe("14/12J");
		String codemov2 = "SiV3XA-sC8k";
		String urlmovie2 = "http://www.youtube.com/embed/" + codemov2 + "?rel=0;3&amp;autohide=1&amp;showinfo=0";
		movie2.setTrailer(urlmovie2);

		// Blade Runner 2049
		movie3 = new Movie();
		movie3.setTitle("Blade Runner 2049");
		movie3.setGenre(GenreType.SCIENCE_FICTION, GenreType.THRILLER);
		movie3.setDesc(
				"30 Jahre nach dem ersten Film fördert ein neuer Blade Runner ein lange unter Verschluss gehaltenes Geheimnis zu Tage.");
		movie3.setImageRessource(String.format(imgPath, "23"));
		movie3.setAltersfreigabe("12");
		movie3.setLaengeMin(163);
		final String code = "gCcx85zbxz4";
		String url = "http://www.youtube.com/embed/" + code + "?rel=0;3&amp;autohide=1&amp;showinfo=0";
		movie3.setTrailer(url);
		movie3.setWebseite("http://www.imdb.com/title/tt1856101/");
		movie3.setCriticsStar(4.3);
		movie3.setOriginalLanguage(MovieLanguage.ENGLISH);
		movie3.setSubtitle(MovieLanguage.FRANZOESISCH);
		movie3.setRegie("Denis Vileneuve");
		movie3.addActors("Ana de Armas").addActors("Dave Bautista").addActors("Edward James Olmos")
				.addActors("Harrison Ford");
	}

	private void initHalls() {
		// Kinosaal konfigurieren
		
		// Minisaal
		hallRex1.configureSeatPlan(10, 10);
		// Nicht verfügbar
		hallRex1.setSeatType(5, 0, SeatType.NONE);
		hallRex1.setSeatType(5, 1, SeatType.NONE);
		hallRex1.setSeatType(5, 8, SeatType.NONE);
		hallRex1.setSeatType(5, 9, SeatType.NONE);
		// Handicap-Sitze
		hallRex1.setSeatType(6, 0, SeatType.HANDYCAP);
		hallRex1.setSeatType(6, 1, SeatType.HANDYCAP);
		hallRex1.setSeatType(6, 8, SeatType.HANDYCAP);
		hallRex1.setSeatType(6, 9, SeatType.HANDYCAP);
		// Premium-Sitze
		hallRex1.setSeatType(8, 5, SeatType.PREMIUM);
		hallRex1.setSeatType(8, 6, SeatType.PREMIUM);
		hallRex1.setSeatType(8, 7, SeatType.PREMIUM);
		hallRex1.setSeatType(8, 8, SeatType.PREMIUM);

		// Mittelsaal
		hallRex2.configureSeatPlan(14, 12);
		// Nicht verfügbar
		hallRex2.setSeatType(10, 2, SeatType.NONE);
		hallRex2.setSeatType(10, 3, SeatType.NONE);		
		hallRex2.setSeatType(10, 4, SeatType.NONE);
		hallRex2.setSeatType(10, 5, SeatType.NONE);
		hallRex2.setSeatType(10, 6, SeatType.NONE);
		hallRex2.setSeatType(10, 7, SeatType.NONE);
		hallRex2.setSeatType(10, 8, SeatType.NONE);
		hallRex2.setSeatType(10, 9, SeatType.NONE);
		// Premium-Sitze		
		hallRex2.setSeatType(11, 2, SeatType.HANDYCAP);
		hallRex2.setSeatType(11, 3, SeatType.HANDYCAP);
		hallRex2.setSeatType(11, 4, SeatType.PREMIUM);
		hallRex2.setSeatType(11, 5, SeatType.PREMIUM);
		hallRex2.setSeatType(11, 6, SeatType.PREMIUM);
		hallRex2.setSeatType(11, 7, SeatType.PREMIUM);
		hallRex2.setSeatType(11, 8, SeatType.HANDYCAP);
		hallRex2.setSeatType(11, 9, SeatType.HANDYCAP);
		
		// Megasaal
		hallRex3.configureSeatPlan(15, 15);
		
		// Open-Air
		hallRex4.configureSeatPlan(10, 15);
	}

	private void addBooking(Vorstellung event){
		Booking booking = new Booking();
		booking.setEvent(event);

		// Tickets von dieser Buchung
		Ticket ticket1 = new Ticket(new Seat(3, 5));
		ticket1.setTicketType(TicketType.ADULT);
		Ticket ticket2 = new Ticket(new Seat(3, 6));
		ticket2.setTicketType(TicketType.KIND);
		Ticket ticket3 = new Ticket(new Seat(3, 7));
		ticket3.setTicketType(TicketType.KIND);

		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(ticket1);
		tickets.add(ticket2);
		tickets.add(ticket3);

		List<Booking> bookings = event.getBookings();
		bookings.add(booking);
		event.setBookings(bookings);		
	}

	public Movie getMovie() {
		return movie3;
	}
<<<<<<< HEAD
=======

>>>>>>> d30bcf3fefb4aeca4bd63d21d0e1dffe81ad507b
}
