package ch.ffhs.kino.model;

import ch.ffhs.kino.model.Movie.MovieLanguage;

public class Show {

	private Movie movie;
	private MovieLanguage language;

	public enum ShowType {
		NORMAL, THREE_D;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public MovieLanguage getLanguage() {
		return language;
	}

	public void setLanguage(MovieLanguage language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Show [movie=" + movie + ", language=" + language + "]";
	}

}
