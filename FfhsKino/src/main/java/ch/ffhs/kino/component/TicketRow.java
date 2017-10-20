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

public class TicketRow extends HBox {

	private Ticket ticket;
	private Button btnDelteTicket;
	private ComboBox<TicketType> cbTicketType;
  
	public TicketRow() {
		this.setAlignment(Pos.CENTER_LEFT);
		//this.setMaxWidth(350.00);
		this.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 2;" + "-fx-border-insets: -1;"
		        + "-fx-border-color: gray;-fx-background-color:wheat;-fx-min-width: 380px");
	}
	
	public TicketRow(Ticket ticket) {
		this.ticket = ticket;
		this.setAlignment(Pos.CENTER_LEFT);
		this.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 2;" + "-fx-border-insets: -1;"
		        + "-fx-border-color: gray;-fx-background-color:white;-fx-min-width: 350px");
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
		labelPrice.setStyle("-fx-background-color:lightblue");
		labelPrice.setMinWidth(100);
		labelPrice.setAlignment(Pos.CENTER);
		this.getChildren().add(labelPrice);
	}
	
	public void addTicketTypeChoice() {
		cbTicketType = new ComboBox<>();
		cbTicketType.setItems( FXCollections.observableArrayList( TicketType.values()));
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
	
	public void addDeleteButton(Image image) {
		btnDelteTicket = new Button();
		btnDelteTicket.setStyle("-fx-padding: 0; -fx-margin:10;-fx-background-color: lightgray;-fx-background-radius: 100;");		
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setFitWidth(23);
		imageView.setFitHeight(23);
		btnDelteTicket.setGraphic(imageView);
		this.getChildren().add(btnDelteTicket);
	}
	
	public ComboBox<TicketType> getCbTicketType() {
		return cbTicketType;
	}

	public void setCbTicketType(ComboBox<TicketType> cbTicketType) {
		this.cbTicketType = cbTicketType;
	}

	public Button getBtnDelteTicket() {
		return btnDelteTicket;
	}

	public void setBtnDelteTicket(Button btnDelteTicket) {
		this.btnDelteTicket = btnDelteTicket;
	}
}
