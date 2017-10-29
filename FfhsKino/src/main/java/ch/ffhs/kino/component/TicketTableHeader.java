package ch.ffhs.kino.component;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Beinhaltet die Summierung aller ausgewählten Tickets.
 * Es wird die Summe von jedem Ticket-Typ (Erwachsener, Kind, ...) sowie
 * die Summe des Preises von allen ausgewählten Tickets angezeigt
 * 
 * @author Birgit Sturzenegger
 */
public class TicketTableHeader  extends GridPane {

	//private StringProperty sumPrice = new SimpleStringProperty();
	private VBox gridTable;
	private Label labelCount;
	private Label labelSumPrice;
	
	private ObservableList<Ticket> ticketData;
	
	public TicketTableHeader(ObservableList<Ticket> ticketData, VBox gridTable) {
		this.ticketData = ticketData;
		this.gridTable = gridTable;
		
		this.setAlignment(Pos.CENTER_LEFT);
		//this.setMaxWidth(350.00);
		this.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 2;" + "-fx-border-insets: -1;"
		        + "-fx-border-color: gray;-fx-background-color:wheat;-fx-min-width: 380px");
		
		TicketRow rowSum = new TicketRow();

		labelCount = new Label();
		labelCount.setWrapText(true);
		labelCount.setText("Keine Tickets ausgewählt");
		labelCount.setStyle("-fx-min-width: 210px;-fx-max-width: 210px");
		rowSum.getChildren().add(labelCount);
		
		labelSumPrice = new Label();
		labelSumPrice.setText("Total: 0.00 CHF");
		labelSumPrice.setStyle("-fx-min-width: 160px;-fx-font-weight: bold; -fx-font-size: 11pt;");
		rowSum.getChildren().add(labelSumPrice);
		
		gridTable.getChildren().add(rowSum);	
	}
	
	public void createTicketListener() {
		this.ticketData.addListener((ListChangeListener<Ticket>) change -> {
			gridTable.getChildren().clear();
			
			List<Ticket> tickets = FXCollections.observableArrayList(this.ticketData);
		
			String countText = "Keine Tickets ausgewählt";
			Double sum = 0.00;
			if (!tickets.isEmpty()) {
				EnumMap<TicketType, Long> map = new EnumMap<>(TicketType.class);
				
				tickets.forEach(t->map.merge(t.getTicketType(), 1L, Long::sum));
				countText = map.entrySet().stream().map(c -> c.getKey().getTitle() + " " + c.getValue()).collect(Collectors.joining(", "));							
				sum = tickets.stream().mapToDouble(o -> o.getTicketType().getCost()).sum();
			}
			
			System.out.println("Liste: " + tickets.size() + " Summe: " + countText);
			
			TicketRow rowSum = new TicketRow();
			
			labelCount.setText(countText);
			rowSum.getChildren().add(labelCount);
			
			labelSumPrice.setText("Total: " + String.format(Locale.ROOT, "%.2f", sum) + " CHF");
			rowSum.getChildren().add(labelSumPrice);				
			
			gridTable.getChildren().add(rowSum);
		});		
	}
}
