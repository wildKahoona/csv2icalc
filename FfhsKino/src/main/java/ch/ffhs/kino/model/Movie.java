package ch.ffhs.kino.model;

import java.util.Arrays;
import java.util.List;

public class Movie {

	public enum MovieLanguage {
		DEUTSCH("D"), ENGLISH("E"), FRANZOESISCH("F");

		private String text;

		private MovieLanguage(String text) {
			this.text = text;

		}

	}

	public enum GenreType {
		DRAMA("Drama"), ACTION("Action"), COMEDY("Komödie"), MISTRERY("Mystery"), THRILLER("Thriller"), CRIMINAL(
				"Krimi"), HORROR("Horror"), SCIENCE_FICTION("Science Fiction"), ANIMATION("Trickfilm");
		private String text;

		private GenreType(String text) {
			this.text = text;
		}
	}

	private String title;
	private List<GenreType> genre;
	private String desc;
	private String imageRessource;
	private String altersfreigabe;
	private int laengeMin;
	private String webseite;
	private double criticsStar;
	private String trailer;

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

	public List<GenreType> getGenre() {
		return genre;
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
		return "Movie [title=" + title + ", genre=" + genre + ", imageRessource=" + imageRessource + ", altersfreigabe="
				+ altersfreigabe + "]";
	}

}
