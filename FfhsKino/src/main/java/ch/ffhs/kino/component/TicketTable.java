package ch.ffhs.kino.component;

import java.util.List;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TicketTable extends GridPane {

	private VBox gridTable;
	private Image imgTrash = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/trash.png"));
	
	private ObservableList<Ticket> ticketData;
	
	public TicketTable(ObservableList<Ticket> ticketData, VBox gridTable) {
		this.ticketData = ticketData;
		this.gridTable = gridTable;
	}

	public void createTicketListener(SeatView[][] seatView) {
		this.ticketData.addListener((ListChangeListener<Ticket>) change -> {
			gridTable.getChildren().clear();

			// Liste sortieren: zuerst Reihe dann Sitznummer
			List<Ticket> tickets = FXCollections.observableArrayList(this.ticketData);
			tickets.sort((x, y) -> {
		        if (x.getSeat().getSeatRow().equals(y.getSeat().getSeatRow())) {
		            return x.getSeat().getSeatColumn() - y.getSeat().getSeatColumn();
		        } else {
		            return x.getSeat().getSeatRow().compareTo(y.getSeat().getSeatRow());
		        }
		    }); 
			
			// Pro Ticket eine Zeile erstellen
			for(Ticket ticket : tickets) {	
				TicketRow ticketRow = new TicketRow(ticket);
				
				ticketRow.addSeatInfo();
				
				ticketRow.addTicketTypeChoice();
				// Add Listener for TicketType-Changes
				ticketRow.getCbTicketType().valueProperty().addListener(new ChangeListener<TicketType>() {
				    @Override
				    public void changed(ObservableValue<? extends TicketType> observable, TicketType oldValue, TicketType newValue) {
				        if(newValue != null){
				        	ticket.setTicketType(newValue);
				        	ticketData.remove(ticket);
				        	ticketData.add(ticket);
				        }
				    }
				});							
				
				ticketRow.addPriceInfo();
				
				ticketRow.addDeleteButton(imgTrash);				
				// Add Listener for delete Ticket
				ticketRow.getBtnDeleteTicket().setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	if(seatView == null) {
				    		ticketData.remove(ticket);
				    	}else {
				    		Seat seat = ticket.getSeat();
					    	SeatView view = seatView[seat.getSeatRow()][seat.getSeatColumn()];
					    	if(view != null) {
					    		view.deselect();
						    	ticketData.remove(ticket);
					    	}
				    	}
				    }
				});					

				gridTable.getChildren().add(ticketRow);	
			}
		});		
	}
}
