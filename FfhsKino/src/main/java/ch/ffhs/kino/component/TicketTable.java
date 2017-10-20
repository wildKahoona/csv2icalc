package ch.ffhs.kino.component;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import ch.ffhs.kino.model.Ticket.TicketType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class TicketTable extends GridPane {

	private Image imgTrash = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/trash.png"));
	private Label labelCount;
	private Label labelSumPrice;
	private EnumMap<TicketType, Long> ticketTypeMap = new EnumMap<>(TicketType.class);
	
	private ObservableList<Ticket> ticketData;
	
	public TicketTable(ObservableList<Ticket> ticketData, VBox gridTickets) {
		this.ticketData = ticketData;
		
		TicketRow rowSum = new TicketRow();

		labelCount = new Label();
		labelCount.setWrapText(true);
		labelCount.setText("Keine Tickets ausgewählt");
		labelCount.setStyle("-fx-min-width: 200px;-fx-max-width: 200px");
		rowSum.getChildren().add(labelCount);
		
		labelSumPrice = new Label();
		labelSumPrice.setText("Total: 0.00 CHF");
		labelSumPrice.setStyle("-fx-min-width: 160px;-fx-font-weight: bold; -fx-font-size: 11pt;");
		rowSum.getChildren().add(labelSumPrice);
		
		gridTickets.getChildren().add(rowSum);	
	}

	public void createTicketListener(SeatView[][] seatView, VBox gridTickets) {
		this.ticketData.addListener((ListChangeListener<Ticket>) change -> {
			gridTickets.getChildren().clear();

			List<Ticket> tickets = FXCollections.observableArrayList(this.ticketData);
			tickets.sort((lhs, rhs) -> {
		        if (lhs.getSeat().getSeatRow().equals(rhs.getSeat().getSeatRow())) {
		            return lhs.getSeat().getSeatColumn() - rhs.getSeat().getSeatColumn();
		        } else {
		            return lhs.getSeat().getSeatRow().compareTo(rhs.getSeat().getSeatRow());
		        }
		    }); 
			
			// HeaderRow: Summe der Ticket-Typen und des Preises
			String countText = "Keine Tickets ausgewählt";
			Double sum = 0.00;
			if (!tickets.isEmpty()) {
				tickets.forEach(t->ticketTypeMap.merge(t.getTicketType(), 1L, Long::sum));
				countText = ticketTypeMap.entrySet().stream().map(c -> c.getKey().getTitle() + " " + c.getValue()).collect(Collectors.joining(", "));							
				sum = tickets.stream().mapToDouble(o -> o.getTicketType().getCost()).sum();
			}
			
			TicketRow rowSum = new TicketRow();
			
			labelCount.setText(countText);
			rowSum.getChildren().add(labelCount);
			
			labelSumPrice.setText("Total: " + String.format(Locale.ROOT, "%.2f", sum) + " CHF");
			rowSum.getChildren().add(labelSumPrice);				
			
			gridTickets.getChildren().add(rowSum);	
			
			// ChildRow: pro Ticket eine Zeile erstellen
			for(Ticket ticket : tickets) {	
				TicketRow ticketRow = new TicketRow(ticket);
				
				ticketRow.addSeatInfo();
				
				ticketRow.addTicketTypeChoice();
				// Add Listener for TicketType-Changes
				ticketRow.getCbTicketType().valueProperty().addListener(new ChangeListener<TicketType>() {
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
				
				ticketRow.addPriceInfo();
				
				ticketRow.addDeleteButton(imgTrash);				
				// Add Listener for delete Ticket
				ticketRow.getBtnDelteTicket().setOnAction(new EventHandler<ActionEvent>() {
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

				gridTickets.getChildren().add(ticketRow);	
			}
		});		
	}
}
