package ch.ffhs.kino.table.model;

import javafx.beans.property.SimpleStringProperty;

public class TicketTableModel {

	private final SimpleStringProperty sitz = new SimpleStringProperty("");
	private final SimpleStringProperty tickettype = new SimpleStringProperty("");
	private final SimpleStringProperty price = new SimpleStringProperty("");

	public TicketTableModel() {
		this("", "", "");
	}

	public TicketTableModel(String sitz, String tickettype, String price) {
		setSitz(sitz);
		setTickettype(tickettype);
		setPrice(price);
	}

	public String getSitz() {
		return sitz.get();
	}

	public void setSitz(String fSitz) {
		sitz.set(fSitz);
	}

	public String getTickettype() {
		return tickettype.get();
	}

	public void setTickettype(String fTickettype) {
		tickettype.set(fTickettype);
	}

	public String getPrice() {
		return price.get();
	}

	public void setPrice(String fPrice) {
		price.set(fPrice);
	}

}
