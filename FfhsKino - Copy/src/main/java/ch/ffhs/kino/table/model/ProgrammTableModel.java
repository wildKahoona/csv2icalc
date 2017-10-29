package ch.ffhs.kino.table.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Vorstellung;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProgrammTableModel {

	public HashMap<String, List<Vorstellung>> getTimes() {
		return times;
	}

	public void setTimes(HashMap<String, List<Vorstellung>> times) {
		this.times = times;
	}

	private SimpleStringProperty film;
	private SimpleStringProperty sprache;
	private SimpleStringProperty saal;
	private SimpleStringProperty genre;
	private SimpleBooleanProperty threeD;
	private Movie movie;
	private HashMap<String, List<Vorstellung>> times = new HashMap<String, List<Vorstellung>>();

	private boolean odd;

	public boolean isOdd() {
		return odd;
	}

	public void setOdd(boolean odd) {
		this.odd = odd;
	}

	public ProgrammTableModel(String film, String sprache, String saal, String genre, Boolean threeD, Movie movie) {
		this.film = new SimpleStringProperty(film);
		this.sprache = new SimpleStringProperty(sprache);
		this.saal = new SimpleStringProperty(saal);
		this.genre = new SimpleStringProperty(genre);
		this.threeD = new SimpleBooleanProperty(threeD);
		this.movie = movie;
	}

	public String getConcatID() {

		return getMovie().getTitle() + getThreeD() + getSprache() + getSaal();
	}

	public String getFilm() {
		return film.get();
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public void setFilm(String film) {
		this.film.set(film);
	}

	public String getSprache() {
		return sprache.get();
	}

	public void setSprache(String sprache) {
		this.sprache.set(sprache);
	}

	public String getSaal() {
		return saal.get();
	}

	public void setSaal(String saal) {
		this.saal.set(saal);
	}

	public String getGenre() {
		return genre.get();
	}

	public void setGenre(String genre) {
		this.genre.set(genre);
	}

	public Boolean getThreeD() {
		return threeD.get();
	}

	public void setThreeD(Boolean threeD) {
		this.threeD.set(threeD);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ProgrammTableModel) {
			ProgrammTableModel obj1 = (ProgrammTableModel) obj;
			return obj1.getConcatID().equals(this.getConcatID());
		}

		return false;
	}

	public List<Vorstellung> getVorstellungByDate(Date date) {

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return times.get(fmt.format(date));

	}

	public void addShowTime(Vorstellung vorstellung) {
		Set<String> keySet = times.keySet();

		if (keySet.contains(vorstellung.getFormatDay())) {
			for (String string : keySet) {
				if (string.equals(vorstellung.getFormatDay())) {
					List<Vorstellung> list = times.get(string);
					if (list == null) {
						list = new ArrayList<>();
					}
					list.add(vorstellung);
				}
			}
		} else {

			ArrayList<Vorstellung> list = new ArrayList<Vorstellung>();
			list.add(vorstellung);
			times.put(vorstellung.getFormatDay(), list);

		}

	}

}
