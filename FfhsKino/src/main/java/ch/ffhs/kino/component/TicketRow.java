package ch.ffhs.kino.component;

import java.util.Locale;

import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

/**
 * Beinhaltet ein ausgewählten Tickets.
 * Die TicketRow wird in der TicketTable angezeigt
 * Man kann das Ticket löschen und/oder einen anderen Typ (Erwachsener, Kind, ...) wählen.
 * 
 * @author Birgit Sturzenegger
 */
public class TicketRow extends HBox {

	private Ticket ticket;
	private Button btnDeleteTicket;
	private ComboBox<TicketType> cbTicketType;

	public TicketRow() {
		this.setAlignment(Pos.CENTER_LEFT);
		this.getStyleClass().add("ticket-header");

	}

	public TicketRow(Ticket ticket) {
		this.ticket = ticket;
		this.setAlignment(Pos.CENTER_LEFT);
		this.getStyleClass().add("ticket-row");

		this.setAlignment(Pos.CENTER_LEFT);
	}

	@FXML
	protected void initialize() {
	}

	public void addSeatInfo() {
		Label label = new Label();
		label.setText(ticket.getSeat().toString());
		label.setMinWidth(120);
		this.getChildren().add(label);
	}

	public void addPriceInfo() {
		String price = String.format(Locale.ROOT, "%.2f", ticket.getTicketType().getCost());
		Label labelPrice = new Label();
		labelPrice.setText(price + " CHF");
		labelPrice.setMinWidth(100);
		labelPrice.setAlignment(Pos.CENTER);
		this.getChildren().add(labelPrice);
	}

	public void addTicketTypeChoice() {
		cbTicketType = new ComboBox<>();
		cbTicketType.setItems(FXCollections.observableArrayList(TicketType.values()));
		cbTicketType.setConverter(new StringConverter<TicketType>() {
			@Override
			public String toString(TicketType object) {
				return object.getTitle();
			}

			@Override
			public TicketType fromString(String string) {
				return null;
			}
		});
		cbTicketType.getSelectionModel().select(ticket.getTicketType());
		this.getChildren().add(cbTicketType);
	}
	
	public void addTicketTypeLabel() {
		Label labelType = new Label();
		labelType.setText(ticket.getTicketType().getTitle());
		this.getChildren().add(labelType);
	}
	
	public void addDeleteButton(Image image) {
		btnDeleteTicket = new Button();
		btnDeleteTicket.setStyle("-fx-padding: 0; -fx-margin:10;-fx-background-radius: 100;");
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setFitWidth(23);
		imageView.setFitHeight(23);
		btnDeleteTicket.setGraphic(imageView);
		this.getChildren().add(btnDeleteTicket);
	}

	public ComboBox<TicketType> getCbTicketType() {
		return cbTicketType;
	}

	public void setCbTicketType(ComboBox<TicketType> cbTicketType) {
		this.cbTicketType = cbTicketType;
	}

	public Button getBtnDeleteTicket() {
		return btnDeleteTicket;
	}

	public void setBtnDeleteTicket(Button btnDeleteTicket) {
		this.btnDeleteTicket = btnDeleteTicket;
	}
}
