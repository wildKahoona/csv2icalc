package ch.ffhs.kino.model;

public class Cinema {

	public enum CinemaPlaces {
		KINO_REX("St. Gallen", "KITAG CINEMAS Rex"), KINO_SCALA("Z�rich", "KITAG CINEMAS Abaton"), KINO_ARENA("Z�rich",
				"Arena Cinemas");

		private String place;
		private String name;

		CinemaPlaces(String place, String name) {

			this.place = place;
			this.name = name;

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPlace() {
			return place;
		}

		public void setPlace(String place) {
			this.place = place;
		}

	}

}
