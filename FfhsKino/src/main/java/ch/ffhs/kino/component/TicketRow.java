package ch.ffhs.kino.component;

import ch.ffhs.kino.model.Ticket;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class TicketRow extends HBox {

	private Ticket ticket;

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	@FXML
	protected void initialize() {
		//this.setAlignment(Pos.CENTER_LEFT);
		this.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
		        + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
	}
}
