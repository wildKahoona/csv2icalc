package ch.ffhs.kino.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Movie {

	public enum MovieLanguage {
		DEUTSCH("D", "Deutsch"), ENGLISH("E", "Englisch"), FRANZOESISCH("F", "Französisch"), NONE("", "");

		private String text;
		private String longText;

		public String getText() {
			return text;
		}

		public String getLongText() {
			return longText;
		}

		public void setLongText(String longText) {
			this.longText = longText;
		}

		public void setText(String text) {
			this.text = text;
		}

		private MovieLanguage(String text, String longText) {
			this.text = text;
			this.longText = longText;

		}

	}

	public enum GenreType {
		DRAMA("Drama"), ACTION("Action"), COMEDY("Komödie"), MISTRERY("Mystery"), THRILLER("Thriller"), CRIMINAL(
				"Krimi"), HORROR("Horror"), SCIENCE_FICTION("Science Fiction"), ANIMATION("Trickfilm");
		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		private GenreType(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return getText();
		}
	}

	private MovieLanguage originalLanguage = MovieLanguage.NONE;
	private MovieLanguage subtitle = MovieLanguage.NONE;
	private String regie;
	private List<String> actors = new ArrayList<String>();
	private String title;
	private List<GenreType> genre;
	private String desc;
	private String imageRessource;
	private String altersfreigabe;
	private int laengeMin;
	private String webseite;
	private double criticsStar;
	private String trailer;

	public String getActorsAsString() {
		return StringUtils.join(getActors(), ", ");

	}

	public MovieLanguage getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(MovieLanguage originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public MovieLanguage getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(MovieLanguage subtitle) {
		this.subtitle = subtitle;
	}

	public String getRegie() {
		return regie;
	}

	public void setRegie(String regie) {
		this.regie = regie;
	}

	public List<String> getActors() {

		if (this.actors == null)
			this.actors = new ArrayList<String>();

		return actors;
	}

	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public double getCriticsStar() {
		return criticsStar;
	}

	public void setCriticsStar(double criticsStar) {
		this.criticsStar = criticsStar;
	}

	public String getWebseite() {
		return webseite;
	}

	public void setWebseite(String webseite) {
		this.webseite = webseite;
	}

	public int getLaengeMin() {
		return laengeMin;
	}

	public void setLaengeMin(int laengeMin) {
		this.laengeMin = laengeMin;
	}

	public void setGenre(List<GenreType> genre) {
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenreText() {
		return StringUtils.join(this.genre, ", ");

	}

	public void setGenre(GenreType... genreTypes) {
		this.genre = Arrays.asList(genreTypes);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImageRessource() {
		return imageRessource;
	}

	public void setImageRessource(String imageRessource) {
		this.imageRessource = imageRessource;
	}

	public String getAltersfreigabe() {
		return altersfreigabe;
	}

	public void setAltersfreigabe(String altersfreigabe) {
		this.altersfreigabe = altersfreigabe;
	}

	@Override
	public String toString() {
		return "Movie [originalLanguage=" + originalLanguage + ", subtitle=" + subtitle + ", regie=" + regie
				+ ", actors=" + actors + ", title=" + title + ", genre=" + genre + ", desc=" + desc
				+ ", imageRessource=" + imageRessource + ", altersfreigabe=" + altersfreigabe + ", laengeMin="
				+ laengeMin + ", webseite=" + webseite + ", criticsStar=" + criticsStar + ", trailer=" + trailer + "]";
	}

	public Movie addActors(String actor) {

		getActors().add(actor);
		return this;
	}

}
