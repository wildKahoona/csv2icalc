package ch.ffhs.kino.component;

import java.util.Locale;

import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

public class TicketRow extends HBox {

	static Image imgTrash = null;
	private Ticket ticket;
	private ObservableList<Ticket> ticketData;

    public static void setTrashImage(Image image) { 
    	imgTrash = image; 
    }
    
    public TicketRow(ObservableList<Ticket> ticketData) {
    	this.ticketData = ticketData;
    }
    
	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	@FXML
	protected void initialize() {
		//this.setAlignment(Pos.CENTER_LEFT);
	}

	public void addTicket(SeatView[][] seatView) {
		
		this.setAlignment(Pos.CENTER_LEFT);

		// Information zum Sitz (Reihe, Nummer)
		Label label = new Label();
		label.setText(ticket.getSeat().toString());
		label.setMinWidth(120);
		//label.setTextAlignment(TextAlignment.CENTER);	
		this.getChildren().add(label);
		
		// Auswahl für den Ticket-Typ
		ComboBox<TicketType> cbxTicketType = new ComboBox<>();
		cbxTicketType.setItems( FXCollections.observableArrayList( TicketType.values()));
		cbxTicketType.setConverter(new StringConverter<TicketType>() {
		    @Override
		    public String toString(TicketType object) {
		        return object.getTitle();
		    }

		    @Override
		    public TicketType fromString(String string) {
		        return null;
		    }
		});
		cbxTicketType.getSelectionModel().select(ticket.getTicketType());
		cbxTicketType.valueProperty().addListener(new ChangeListener<TicketType>() {
		    @Override
		    public void changed(ObservableValue<? extends TicketType> observable, TicketType oldValue, TicketType newValue) {
		        if(newValue != null){
		        	// unschön, aber es funktioniert (vergeblich mit eigenem UserControl und fxml rumgedoktert)
		        	ticket.setTicketType(newValue);
		        	ticketData.remove(ticket);
		        	ticketData.add(ticket);
		        }
		    }
		});							
		this.getChildren().add(cbxTicketType);
		
		// Preis
		String price = String.format(Locale.ROOT, "%.2f", ticket.getTicketType().getCost());
		Label labelPrice = new Label();
		labelPrice.setText(price + " CHF");
		labelPrice.setStyle("-fx-background-color:lightblue");
		labelPrice.setMinWidth(100);
		labelPrice.setAlignment(Pos.CENTER);
		this.getChildren().add(labelPrice);
		
		// Delete Button mit Bild
		Button deleteButton = new Button();
		deleteButton.setStyle("-fx-padding: 0; -fx-margin:10;-fx-background-color: lightgray;-fx-background-radius: 100;");		
		ImageView imageView = new ImageView();
		imageView.setImage(imgTrash);
		imageView.setFitWidth(23);
		imageView.setFitHeight(23);
		deleteButton.setGraphic(imageView);
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Seat seat = ticket.getSeat();
		    	SeatView view = seatView[seat.getSeatRow()][seat.getSeatColumn()];
		    	if(view != null) {
		    		view.deselect();
			    	ticketData.remove(ticket);
		    	}
		    }
		});
		this.getChildren().add(deleteButton);
	}
}
