package ch.ffhs.kino.table.model;

import java.util.Locale;

import ch.ffhs.kino.model.Ticket.TicketType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TicketTableModel {

	private TicketType ticketType;
	private SimpleStringProperty sitz = new SimpleStringProperty("");
	private ObjectProperty<TicketType> tickettype;
	private SimpleStringProperty price = new SimpleStringProperty("");

	public TicketTableModel() {
		this("", "", "");
	}

	public TicketTableModel(String sitz, String tickettype, String price) {
		setSitz(sitz);
	}

	public String getSitz() {
		return sitz.get();
	}

	public void setSitz(String fSitz) {
		sitz.set(fSitz);
	}

	public ObjectProperty getTickettype() {
		return tickettype;
	}

	public void setTickettype(ObjectProperty fTickettype) {
		tickettype = fTickettype;
	}

	public SimpleStringProperty getPrice() {
		return price;
	}

	public void setPrice(SimpleStringProperty fPrice) {
		price = fPrice;
	}

	
	public TicketType getTicketType() {
		return ticketType;
	}
	

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;	
		String priceT = String.format(Locale.ROOT, "%.2f", ticketType.getCost());
		price.set(priceT);
//		
//		setPrice.set((price + " CHF"));
	}

}
