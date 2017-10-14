package ch.ffhs.kino.model;

public class TicketPrice {
	private String description;
	private Double price;
	
	public TicketPrice(String description, Double price) {
		this.description = description;
		this.price = price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
}
