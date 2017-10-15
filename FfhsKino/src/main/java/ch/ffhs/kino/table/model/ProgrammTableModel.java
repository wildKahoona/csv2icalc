package ch.ffhs.kino.table.model;

import java.util.ArrayList;
import java.util.List;

import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Vorstellung;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProgrammTableModel {

	private SimpleStringProperty film;
	private SimpleStringProperty sprache;
	private SimpleStringProperty saal;
	private SimpleStringProperty genre;
	private SimpleBooleanProperty threeD;
	private Movie movie;

	public ProgrammTableModel(String film, String sprache, String saal, String genre, Boolean threeD, Movie movie) {
		this.film = new SimpleStringProperty(film);
		this.sprache = new SimpleStringProperty(sprache);
		this.saal = new SimpleStringProperty(saal);
		this.genre = new SimpleStringProperty(genre);
		this.threeD = new SimpleBooleanProperty(threeD);
		this.movie = movie;
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

}
