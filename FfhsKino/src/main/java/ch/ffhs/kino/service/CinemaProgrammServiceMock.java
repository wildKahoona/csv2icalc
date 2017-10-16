package ch.ffhs.kino.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.ffhs.kino.model.Cinema.CinemaPlaces;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Movie.GenreType;
import ch.ffhs.kino.model.Movie.MovieLanguage;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Show;
import ch.ffhs.kino.model.TicketPrice;
import ch.ffhs.kino.model.Show.ShowType;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;

/**
 * Stellt einen Mock-Service zur verfügung um die Daten auf den verschiedenen
 * Views darstellen zu können.
 * 
 * @author Denis Bittante
 *
 */
public class CinemaProgrammServiceMock {

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

	private List<Vorstellung> vorstellungen = new ArrayList<Vorstellung>();
	private List<TicketPrice> ticketPrices = new ArrayList<TicketPrice>();
	private List<Booking> bookings = new ArrayList<Booking>();
	
	public CinemaProgrammServiceMock() {

		init();
	}

	public List<Vorstellung> getProgramm() {
		return vorstellungen;
	}

	public Vorstellung getVorstellung() {
		return vorstellungen.get(0);
	}

	public List<TicketPrice> getTicketPrices() {
		return ticketPrices;
	}
	
	public Booking getBooking() {

		Booking booking = new Booking();

		booking.setEvent(getVorstellung());

		return booking;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	private void init() {
		// MockData

		initMovies();

		initShows();
		
		initHalls();
		
		initPrices();

		for (int i = 0; i < 200; i++) {

			double random = Math.random();
			double random2 = Math.random();
			Vorstellung event1 = new Vorstellung();
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
			switch (i % 4) {
			case 0:
				event1.setType(ShowType.THREE_D);
				break;
			default:
				event1.setType(ShowType.NORMAL);
				break;
			}
			vorstellungen.add(event1);
		}

		initBookings();
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

		String imgPath= "/ch/ffhs/kino/movies/mov%s.jpg";
		
		
		// Hereinspaziert
		movie1 = new Movie();
		movie1.setTitle("Hereinspaziert");
		movie1.setGenre(GenreType.COMEDY);
		movie1.setDesc(
				"Die neunköpfige Romafamilie im Garten des Linksintellektuellen Jean-Etienne stellt seine Überzeugungen auf die Probe.");
		movie1.setImageRessource(String.format(imgPath, "20"));
		movie1.setAltersfreigabe("12J");

		// Barry Seal - Only in America
		movie2 = new Movie();
		movie2.setTitle("Barry Seal - Only in America");
		movie2.setGenre(GenreType.ACTION, GenreType.COMEDY, GenreType.DRAMA, GenreType.THRILLER);
		movie2.setDesc(
				"Einige Waisenkinder finden ein Zuhause bei einem Puppenmacher. Schon bald geraten sie ins Visier einer seiner Kreationen.");
		movie2.setImageRessource(String.format(imgPath, "4"));
		movie2.setAltersfreigabe("14/12J");

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
		movie3.addActors("Ana de Armas").addActors("Dave Bautista").addActors("Edward James Olmos").addActors("Harrison Ford");
	}

	private void initHalls()
	{
		// Kinosaal konfigurieren
		hallRex1.configureSeatPlan(14, 10);
		hallRex1.setSeatNotAvailable(5, 0);
		hallRex1.setSeatNotAvailable(5, 1);
		hallRex1.setSeatNotAvailable(5, 8);
		hallRex1.setSeatNotAvailable(5, 9);

		hallRex2.configureSeatPlan(15, 8);
		hallRex3.configureSeatPlan(25, 15);
	}
	
	private void initPrices()
	{
		// Preise
		ticketPrices.add(new TicketPrice("Erwachsener", 19.00));
		ticketPrices.add(new TicketPrice("Kind", 12.00));
	}
	
	private void initBookings() {
		Booking booking = new Booking();
		Vorstellung vorstellung = getVorstellung();
		booking.setEvent(vorstellung);
		
		// Tickets von dieser Buchung
		Ticket ticket1 = new Ticket(new Seat(3, 5, 35));
		ticket1.setTicketType(TicketType.ADULT);
		Ticket ticket2 = new Ticket(new Seat(3, 6, 36));
		ticket2.setTicketType(TicketType.KIND);
		Ticket ticket3 = new Ticket(new Seat(3, 7, 37));
		ticket3.setTicketType(TicketType.KIND);
		
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(ticket1);
		tickets.add(ticket2);
		tickets.add(ticket3);
		
		booking.setTickets(tickets);
		bookings.add(booking);
		vorstellung.setBookings(bookings);
	}
	
	public Movie getMovie() {
		return movie3;
	}


	
	
	
}
