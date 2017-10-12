package ch.ffhs.kino.table.model;

import javafx.beans.property.SimpleStringProperty;

public class ProgrammTableModel {

	private SimpleStringProperty film;
	private SimpleStringProperty sprache;
	private SimpleStringProperty saal;

	public ProgrammTableModel(String film, String sprache, String saal) {
		this.film = new SimpleStringProperty(film);
		this.sprache = new SimpleStringProperty(sprache);
		this.saal = new SimpleStringProperty(saal);
	}

	public String getFilm() {
		return film.get();
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

}
