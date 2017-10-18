package ch.ffhs.kino.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Hall;
import ch.ffhs.kino.model.Seat;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Ticket.TicketType;
import ch.ffhs.kino.component.SeatView;
import ch.ffhs.kino.component.TicketRow;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.table.model.TicketTableModel;
import ch.ffhs.kino.model.Seat.SeatType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.util.StringConverter;



public class VorstellungController {
	/**
	 * Birgit
	 *  TODO: Fertigstellen
	 */
	private Vorstellung vorstellung;
	
	private SeatView views[][];
	
	private Booking reservation;
	
	/**
	 * Das GridPane des Kinosaals mit den Sitzen
	 */
	@FXML
	private GridPane hallGrid;
	
	@FXML
	private VBox ticketGrid;
	
	@FXML
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());
	}
	
	@FXML
	private Label vorstellungChoice;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private Button buyButton;
	
	@FXML
	private Button deleteTicketsButton;
	
	@FXML
	private Button addTicketButton;
	
	@FXML
	private TableView<TicketTableModel> ticketTable;
	
	@FXML
	private Label labelNoTickets;
	
	@FXML
	private HBox boxTimer;
//	
//	@FXML
//	private TableColumn<Ticket, String> colSeatDescription;
//	
//	@FXML
//	private TableColumn<Ticket, TicketType> colPrice;
	
	/**
	 * Die Liste der ausgewählten Sitze
	 */
	private ObservableList<SeatView> selectedSeats = FXCollections.observableArrayList();
	
	/**
     * Die Liste der Reservierungen
     */
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	private ObservableList<TicketTableModel> ticketTableData = FXCollections.observableArrayList();
	

	private long endTime ;
	private Timeline timeline;

	// Zelle mit Delete-Button
	private class ButtonCell extends TableCell<TicketTableModel, Boolean> {
	    final Button cellButton = new Button("Action");	     
	    ButtonCell(){         
	        cellButton.setOnAction(new EventHandler<ActionEvent>(){

	            @Override
	            public void handle(ActionEvent t) {
	                // do something when button clicked
	            	int selectdIndex = getTableRow().getIndex();
	            	 //delete the selected item in data
	            	ticketTableData.remove(selectdIndex);
	            }
	        });
	    }

	    //Display button if the row is not empty
	    @Override
	    protected void updateItem(Boolean t, boolean empty) {
	        super.updateItem(t, empty);
	        if(!empty){
	            setGraphic(cellButton);
	        }
	    }
	}
	
	@FXML
	protected void initialize() {	
		
		initTimeline();
	
		initTicketControl();
		
		buyButton.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		buyButton.setTooltip(new Tooltip("Zur Zahlungsabwicklung"));
		buyButton.setOnAction(commandBuyHandler);
		
		deleteTicketsButton.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		deleteTicketsButton.setOnAction(commandDeleteTicketsHandler);
		
		addTicketButton.disableProperty().bind(Bindings.size(ticketData).isEqualTo(0));
		addTicketButton.setOnAction(commandAddTicketHandler);
		
		
//		// TableView ist sehr gruusig
//		TableColumn<TicketTableModel, String> col_id = new TableColumn<TicketTableModel, String>();
//        col_id.setCellValueFactory(new PropertyValueFactory<TicketTableModel, String>("ticketType"));
//        ticketTable.getColumns().add(col_id);
//        
//        TableColumn<TicketTableModel, String> col_price = new TableColumn<TicketTableModel, String>();
//        col_price.setCellValueFactory(new PropertyValueFactory<TicketTableModel, String>("price"));
//        ticketTable.getColumns().add(col_price);
//            
//		//Insert Button
//        TableColumn col_action = new TableColumn<>("Action");
//        col_action.setSortable(false);
//         
//        col_action.setCellValueFactory(
//                new Callback<TableColumn.CellDataFeatures<TicketTableModel, Boolean>, 
//                ObservableValue<Boolean>>() {
// 
//            @Override
//            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TicketTableModel, Boolean> p) {
//                return new SimpleBooleanProperty(p.getValue() != null);
//            }
//        });
// 
//        col_action.setCellFactory(
//                new Callback<TableColumn<TicketTableModel, Boolean>, TableCell<TicketTableModel, Boolean>>() {
// 
//            @Override
//            public TableCell<TicketTableModel, Boolean> call(TableColumn<TicketTableModel, Boolean> p) {
//                return new ButtonCell();
//            }
//         
//        });
//        ticketTable.getColumns().add(col_action);     
//        ticketTable.setItems(ticketTableData);
//		
//        ticketTable.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                // Get the table header
//                Pane header = (Pane)ticketTable.lookup("TableHeaderRow");
//                if(header!=null && header.isVisible()) {
//                  header.setMaxHeight(0);
//                  header.setMinHeight(0);
//                  header.setPrefHeight(0);
//                  header.setVisible(false);
//                  header.setManaged(false);
//                }
//            }
//        });
        
        
        
        
//        TableColumn<Ticket, String> firstName = new TableColumn<Ticket, String>("ticketType");
//        firstName.setCellValueFactory(new PropertyValueFactory<Ticket, String>("ticketType"));
//        ticketTable.getColumns().add(firstName);
//      //Insert Button
//        TableColumn<Ticket, Boolean> actionCol = new TableColumn<>("Action");
//        actionCol.setSortable(false);
//        ticketTable.getColumns().add(actionCol);
//        
//        actionCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, Boolean>, ObservableValue<Boolean>>() {
//        	@Override
//        	public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Ticket, Boolean> p) {
//        	return new SimpleBooleanProperty(p.getValue() != null);
//        	}
//        	});
//
//        actionCol.setCellFactory(new Callback<TableColumn<Ticket, Boolean>, TableCell<Ticket, Boolean>>() {
//        	@Override
//        	public TableCell<Ticket, Boolean> call(TableColumn<Ticket, Boolean> p) {
//        	return new ButtonCell(ticketTable);
//        	}
//        	});
//        
//        TableColumn<Ticket, TicketType> colPrice = new TableColumn<>("Action");
//        colPrice.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
//		colPrice.setCellFactory(ComboBoxTableCell.<Ticket, TicketType>forTableColumn(TicketType.values()));
//		ticketTable.getColumns().add(colPrice);
//		
//        ticketTable.setItems(ticketData);
//        ticketTable.setEditable(true);
    
		
		
		
//		imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//
//		     @Override
//		     public void handle(MouseEvent event) {
//		    	 // Click Mouse Event for Image...
//		         event.consume();
//		     }
//		});
		
		//imageView.setStyle("-fx-background-color:transparent;");
		//imageView.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("JavafxSm.gif"))));

//		imageView.setOnMouseEntered(new EventHandler<MouseEvent>
//	    () {
//
//	        @Override
//	        public void handle(MouseEvent t) {
//	        	imageView.setStyle("-fx-effect: dropshadow(gaussian, #54acd2, 1, 1, 0, 0);");
//	        }
//	    });
//
//		imageView.setOnMouseExited(new EventHandler<MouseEvent>
//	    () {
//
//	        @Override
//	        public void handle(MouseEvent t) {
//	        	imageView.setStyle("-fx-background-color:transparent;");
//	        }
//	    });
	    
//		buyButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//                try {
//                	// Booking erstellen
//                	Booking booking = new Booking();
//                	booking.setEvent(vorstellung);
//                	booking.setTickets(ticketData);
//                	Main.startTicketZahlen(booking);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//            }
//        });
		
		
//		timer = new AnimationTimer() {
//		    
//			@Override
//		    public void start() {
//				startTime = 600 * 1000; // 10 Minuten
//		        super.start();
//		    }
//
//		    @Override
//		    public void handle(long timestamp) {
////		        long now = System.currentTimeMillis();
////		        time.set((now - startTime) / 1000.0);
//		    	startTime = timestamp;
//		        
//		        SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
//		        timeLabel.setText(fmt.format(startTime));
//		    }
//		};
//		timer.start();
		
//		hallGrid.widthProperty().addListener((ov, oldValue, newValue) -> {
//			resizeSeat(newValue);
//        });
//		hallGrid.heightProperty().addListener((ov, oldValue, newValue) -> {
//			resizeSeat(newValue);
//        });
		
//  	//!!! Tabelle geht leider nicht, da ich keine Button in die Zelle reintung kann !!!
//		//Die Spalten der Tabelle definieren
//		colSeatDescription.setCellValueFactory(new PropertyValueFactory<Ticket, String>("seatDescription"));
//		colPrice.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
//		colPrice.setCellFactory(ComboBoxTableCell.<Ticket, TicketType>forTableColumn(TicketType.values()));
//		
//		//Die Tabelle anzeigen.
//		ticketTable.setItems(ticketData);
//		ticketTable.setEditable(true);
	}
	
	public Vorstellung getVorstellung() {
		return vorstellung;
	}

	public void setVorstellung(Vorstellung vorstellung) {
		this.vorstellung = vorstellung;
		initTitle();
		renderSeatView();
		renderReservedSeats();
		renderBookedSeats();
	}
	
	public void setVorstellung(Booking reservation) {
		this.reservation = reservation;
		this.vorstellung = this.reservation.getEvent();
		setVorstellung(this.reservation.getEvent());
	}
	
	private void initTitle() {
		String movieTitle = vorstellung.getShow().getMovie().getTitle();
		String movieLanguage = vorstellung.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		vorstellungChoice.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(vorstellung.getDate()) + ", " + vorstellung.getHall().getHallName());
	}
	
	private void initTicketControl() {
		// TODO: echt gruusig! (sollte doch ein wiederverwendbares Control sein à la MVVM, habe ich aber nicht hingekriegt)
		// TableView hat leider keine ButtonCell 
		// ButtonCell habe ich dann doch noch geschafft,
		// dann kam aber schon der nächste Killer: Table ist nicht direkt im Edit-Mode, man muss erst doppelklicken!!!

		Image trash = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/trash.png"));
		boxTimer.setVisible(false);
		ticketData.addListener((ListChangeListener<Ticket>) change -> {
			ticketGrid.getChildren().clear();
			List<Ticket> tickets = FXCollections.observableArrayList(ticketData);
			
			if (tickets.isEmpty()) {
				labelNoTickets.setVisible(true);
				boxTimer.setVisible(false);
				return;
			}
			
			labelNoTickets.setVisible(false);
			boxTimer.setVisible(true);
			tickets.sort((t1, t2) -> t1.getSeat().getSeatNumber().compareTo(t2.getSeat().getSeatNumber()));
			
			TicketRow rowSumme = new TicketRow();
			rowSumme.setMaxWidth(350.00);
			rowSumme.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
			        + "-fx-border-width: 2;" + "-fx-border-insets: -1;"
			        + "-fx-border-color: gray;-fx-background-color:wheat;-fx-min-width: 350px");		
			
			// Pro TicketType die Anzahl zählen
			EnumMap<TicketType, Long> map = new EnumMap<>(TicketType.class);
			tickets.forEach(t->map.merge(t.getTicketType(), 1L, Long::sum));
			String countText = map.entrySet().stream().map(c -> c.getKey().getTitle() + " " + c.getValue()).collect(Collectors.joining(", "));			
			Label labelCount = new Label();
			//labelCount.setMaxWidth(200);
			labelCount.setWrapText(true);
			labelCount.setText(countText);
			labelCount.setStyle("-fx-padding: 3;-fx-min-width: 240px;-fx-max-width: 240px");
			labelCount.setTextAlignment(TextAlignment.CENTER);	
			rowSumme.getChildren().add(labelCount);
			
			// Summe der Tickets auflisten
			Double sum = tickets.stream().mapToDouble(o -> o.getTicketType().getCost()).sum();
			String sumPrice = String.format(Locale.ROOT, "%.2f", sum);
			Label sumPriceLabel = new Label();
			sumPriceLabel.setText(sumPrice + " CHF");
			sumPriceLabel.setStyle("-fx-padding: 3;-fx-max-width: 80px;-fx-font-weight: bold; -fx-font-size: 11pt;");
			rowSumme.getChildren().add(sumPriceLabel);
			
			ticketGrid.getChildren().add(rowSumme);
			
			// Einzelne Tickets auflisten
			for(Ticket ticket : tickets) {			
				// Erstelle das Control für die Anzeige eines Tickets
				// TODO: Styles auslagern ins css
				TicketRow row = new TicketRow();
				row.setMaxWidth(350.00);
				row.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
				        + "-fx-border-width: 2;" + "-fx-border-insets: -1;"
				        + "-fx-border-color: gray;-fx-background-color:white;-fx-min-width: 350px");
				row.setTicket(ticket);
				
				// Information zum Sitz (Reihe, Nummer)
				Label label = new Label();
				label.setText(ticket.getSeat().toString());
				label.setStyle("-fx-padding: 3;-fx-min-width: 120px");
				label.setTextAlignment(TextAlignment.CENTER);	
				row.getChildren().add(label);
				
				// Auswahl für den Ticket-Typ
				ComboBox<TicketType> cbxTicketType = new ComboBox<>();
				cbxTicketType.setStyle("-fx-padding: 0,5; -fx-min-width: 100px;");
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
				row.getChildren().add(cbxTicketType);

				// Preis
				String price = String.format(Locale.ROOT, "%.2f", ticket.getTicketType().getCost());
				Label labelPrice = new Label();
				labelPrice.setText(price + " CHF");
				labelPrice.setStyle("-fx-padding: 3;-fx-min-width: 60px");
				labelPrice.setTextAlignment(TextAlignment.CENTER);					
				row.getChildren().add(labelPrice);
				
				// Delete Button mit Bild
				Button deleteButton = new Button();
				deleteButton.setStyle("-fx-padding: 0; -fx-margin:10;-fx-background-color: lightgray;-fx-background-radius: 100;");
				
				ImageView imageView = new ImageView();
				imageView.setImage(trash);
				imageView.setFitWidth(23);
				imageView.setFitHeight(23);
				deleteButton.setGraphic(imageView);
				row.getChildren().add(deleteButton);
				
				deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	int seatNumber = ticket.getSeat().getSeatNumber();
				    	 SeatView seatView =  selectedSeats.stream().filter(x -> x.getSeat().getSeatNumber() == seatNumber).findFirst().get();
				    	 seatView.deselect();
				    	 ticketData.remove(ticket);
				    }
				});
				
				// Zeile zum Grid hinzufügen
				ticketGrid.getChildren().add(row);				
			}
			
//			for(Ticket ticket : ticketData) {
//				
//				// ToDo: Styles auslagern ins css
//				TicketRow row = new TicketRow();
//				row.setMaxWidth(320.00);
//				row.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
//				        + "-fx-border-width: 2;" + "-fx-border-insets: -1;"
//				        + "-fx-border-color: gray;");
//				row.setTicket(ticket);
//				
//				// Information zum Sitz (Reihe, Nummer)
//				Label label = new Label();
//				label.setText(ticket.getSeat().toString());
//				label.setStyle("-fx-padding: 3;-fx-min-width: 100px");
//				label.setTextAlignment(TextAlignment.CENTER);	
//				row.getChildren().add(label);
//				
//				// Auswahl für den Ticket-Typ
//				ComboBox<TicketType> cbxTicketType = new ComboBox<>();
//				cbxTicketType.setStyle("-fx-padding: 0,5;-fx-min-width: 120px;");
//				cbxTicketType.setItems( FXCollections.observableArrayList( TicketType.values()));					
//				cbxTicketType.getSelectionModel().select(ticket.getTicketType());
//				cbxTicketType.valueProperty().addListener(new ChangeListener<TicketType>() {
//				    @Override
//				    public void changed(ObservableValue<? extends TicketType> observable, TicketType oldValue, TicketType newValue) {
//				        if(newValue != null){
//				        	ticket.setTicketType(newValue);
//				        }
//				    }
//				});					
//				row.getChildren().add(cbxTicketType);
//
//				// Preis
//				String price = String.format(Locale.ROOT, "%.2f", ticket.getTicketType().getCost());
//				Label labelPrice = new Label();
//				labelPrice.setText(price + " CHF");
//				labelPrice.setStyle("-fx-padding: 3;-fx-min-width: 60px");
//				labelPrice.setTextAlignment(TextAlignment.CENTER);	
//				row.getChildren().add(labelPrice);
//				
//				// Delete Button mit Bild
//				Button deleteButton = new Button();
//				deleteButton.setStyle("-fx-padding: 0; -fx-margin:10;-fx-background-color: lightgray;-fx-background-radius: 100;");
//				
//				ImageView imageView = new ImageView();
//				imageView.setImage(trash);
//				imageView.setFitWidth(23);
//				imageView.setFitHeight(23);
//				deleteButton.setGraphic(imageView);
//				row.getChildren().add(deleteButton);
//				
//				deleteButton.setOnAction(new EventHandler<ActionEvent>() {
//				    @Override public void handle(ActionEvent e) {
//				    	int seatNumber = ticket.getSeat().getSeatNumber();
//				    	 SeatView seatView =  selectedSeats.stream().filter(x -> x.getSeat().getSeatNumber() == seatNumber).findFirst().get();
//				    	 seatView.deselect();
//				    	 ticketData.remove(ticket);
//				    }
//				});
//				
//				// Zeile zum Grid hinzufügen
//				ticketGrid.getChildren().add(row);
//			}
		});
	}

	private void initTimeline() {
			timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> {
			    	long diff = endTime - System.currentTimeMillis();
			    	if(diff <= (long)0) {
			    		timeline.stop();
			    		Platform.runLater(new Runnable() {
			    		      @Override public void run() {
			    		    	  // Ausgewählte Sitze freigeben
			    		    	  clearSelectedSeats();
			    		    	  // Reservierung löschen
			    		    	  Main.cinemaProgrammService.setCurrentReservation(null);
			    		    	  
			    		    	  // Meldung an den Benutzer
				  	    		  Alert alert = new Alert(AlertType.WARNING);
				  	    		  alert.setTitle("Reservierungszeit abgelaufen");
				  	    		  alert.setHeaderText("Bitte wähle neue Plätze");
				  	    		  alert.setContentText("Die Reservierungszeit ist abgelaufen, daher wurden deine Plätze freigegeben.");
				  	    		  alert.showAndWait();
			    		      }
			    		});			    					    		
			    	}else {
			    		SimpleDateFormat fmt = new SimpleDateFormat("mm:ss");
				        timeLabel.setText(fmt.format(diff));
			    	} 
			    })
			);
			timeline.setCycleCount( Animation.INDEFINITE );
	}

	private void clearSelectedSeats() {
		// ACHTUNG: NICHT THREAD-SICHER (hat mich viele Stunden gekostet)!!!
//		selectedSeats.forEach((seat) -> { 
//			seat.deselect();
//		});
//		selectedSeats.clear();
		
		// THREAD-SICHER
		Iterator<SeatView> seats = this.selectedSeats.iterator();
		while (seats.hasNext()) {
			SeatView seat = seats.next();
			seats.remove();
			seat.deselect();
		}
	}
	  
	public void StartTimeAnimation(){
		endTime = System.currentTimeMillis() + (600 * 1000); // 10 Minuten
		//endTime = System.currentTimeMillis() + (6 * 1000);
		timeline.play();
	}
	
	public void StopTimeAnimation(){
		timeline.stop();
	}
	  
	/**
	 * Die Sitze für den entsprechenden Kinosaal zeichnen
	*/
	private void renderSeatView() {
		Hall hall = vorstellung.getHall();
		int rows = hall.getRows();
	    int columns = hall.getColumns();
	    SeatType[][] seatPlan = hall.getSeatPlan();

	    ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
	    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
	    
	    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
	    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
	    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
	    
	    views = new SeatView[rows][];
	    
	    // Bilder der Sitzplätze laden
	    Image selectedSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSelected_small.png"));
	    Image normalSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seat_small.png"));
	    Image premiumSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatPremium_small.png"));
	    Image handicapSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatHandicap_small.png"));
	    
	    // Bild für den ausgewählten Sitz für alle Objekte setzen	    
	    SeatView.setImageSelcectedSeat(selectedSeat);
	    
	    int seatCounter = 1;	    
        for (int r = 0; r < rows; r++) {
            views[r] = new SeatView[columns];          
            for (int c = 0; c < columns; c++) {
                Seat seat = new Seat(r, c, seatCounter);
            	SeatView seatView = new SeatView(seat);         	
            	views[r][c] = seatView;
            	
            	// Je nach Sitz-Typ ein anderes Standardbild nehmen            	
            	if(seatPlan[r][c] == SeatType.NONE){
            		seatView.setDisable(true);
            		seatView.setOpacity(0);
            		continue;
    	    	}else if (seatPlan[r][c] == SeatType.HANDYCAP){
    	    		seatView.setImageSeat(handicapSeat);
    	    	}else if (seatPlan[r][c] == SeatType.PREMIUM){
    	    		seatView.setImageSeat(premiumSeat);
    	    	}else {
    	    		seatView.setImageSeat(normalSeat);
    	    	}

            	// ImageViews are not resizeable, but you could use a parent that is resizeable and bind the fitWidth and fitHeight properties 
            	// to the size of the parent using expression binding          	    	
            	seatView.fitWidthProperty().bind(size);
            	seatView.fitHeightProperty().bind(size);
            	  	
            	seatCounter++;
            	
            	seatView.getState().addListener((e, oldValue, newValue) -> {
		        	if (newValue) {
			        	  if(selectedSeats.size() == 0)
			        		  StartTimeAnimation();	        	  
			        	  selectedSeats.add(seatView);
			        	  // Ticket für diesen Sitzplatz hinzufügen
			        	  Ticket ticket = new Ticket(seat);
			        	  ticketData.add(ticket);
			          }
			          else {
			            this.selectedSeats.remove(seatView);
			            if(selectedSeats.size() == 0)
			            	StopTimeAnimation();
			            
			            // Ticket aus der Liste entfernen
			            Optional<Ticket> removeTicket = ticketData.stream().
			            		filter(x -> x.getSeat().equals(seat)).findFirst();  			            
			            if(removeTicket.isPresent())
			            	ticketData.remove(removeTicket.get());
			          }		        		
    	        });
            	              
//                HBox box = new HBox(5);
//                box.getChildren().add(views[r][c]);
//                hallGrid.add(box, c, r);
            	this.hallGrid.add(views[r][c], c, r);
            }
        }
	}
	
	private void renderReservedSeats() {
	    if(this.reservation != null) {
	    	for(Ticket ticket : reservation.getTickets()) {
	    		Seat seat = ticket.getSeat();
	    		SeatView seatView = views[seat.getSeatRow()][seat.getSeatColumn()];
	    		if (seatView != null)
	    			seatView.select();
	    	}
	    }
	}
	  
	private void renderBookedSeats() {
	    List<Booking> bookings = vorstellung.getBookings();
	    if(bookings.isEmpty()) return;
	    Image soldSeat = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/seatSold_small.png"));
	    for (Booking booking : bookings) {
	    	for(Ticket ticket : booking.getTickets()) {
	    		Seat seat = ticket.getSeat();
	    		SeatView seatView = views[seat.getSeatRow()][seat.getSeatColumn()];
	    		if (seatView != null) {
	    			seatView.setSold();
            		seatView.setImageSeat(soldSeat);
	    		}
	    	}
	    }
	}
	
	private EventHandler<ActionEvent> commandDeleteTicketsHandler = (evt) -> {
		clearSelectedSeats();
	};
	
    private EventHandler<ActionEvent> commandAddTicketHandler = (evt) -> {
    	// Ticket hinzufügen
    	if(selectedSeats.size() == 0) return;
    	
    	int maxCol = 10;
    	
    	SeatView lastSeat = selectedSeats.get(selectedSeats.size() - 1);
    	int row = lastSeat.getSeat().getSeatRow();
    	int col = lastSeat.getSeat().getSeatColumn();
    	
    	// Links next: column vom Sitz - 1 (0 + 4 - 1 = 3)
    	// Rechts next: Anzahl colums - column vom Sitz (10 - 4 = 6)
    	
    	
    	Boolean goLinks = false;
    	Boolean leftSold = false;
    	Boolean isSeatFree = true;
    	
    	// Muss ich nach links?
    	// - wenn der letzte Platz von der Mitte rechts liegt
    	// - wenn der letzte Platz nicht am rechten Ende ist
    	// - wenn der nächste mögliche Platz frei ist
    	if(col > maxCol - col) {
    		if(col > 0)
    		{
    			SeatView nextSeat = views[row][col-1];	
    			if(nextSeat.getSold() || nextSeat.getIsSelected())
    				isSeatFree = false;
    			
    			goLinks = isSeatFree;
//    			leftSold = nextSeat.getSold() || nextSeat.getIsSelected();
//        		if(!leftSold) {
//        			goLinks = true;
//        		}
    		}
    	}
    	
    	// Muss ich nach rechts?
    	if(!goLinks && isSeatFree) {
	    	if(col < maxCol && col != 0)
			{
				SeatView nextSeat = views[row][col+1];
				if(nextSeat.getSold() || nextSeat.getIsSelected()) {
	    			goLinks = true;
	    		}
			}
    	}
    	
    	
    	if(goLinks) {
    		for(int c=col-1; c < views[row].length; c--) {
                //System.out.println("Values at arr["+r+"]["+c+"] is " + views[r][c]);
    			
    			// Wenn frei
    			SeatView nextSeat = views[row][c];
    			System.out.println("Values at arr["+(row)+"]["+c+"] is " + views[(row)][c]);
            	nextSeat.select();
            	
            	lastSeat = nextSeat;
    			break;
            }
    	}else {
    		for(int c=col+1; c < views[row].length; c++) {
                //System.out.println("Values at arr["+r+"]["+c+"] is " + views[r][c]);
    			
    			// Wenn frei
    			SeatView nextSeat = views[row][c];
    			System.out.println("Values at arr["+(row)+"]["+c+"] is " + views[(row)][c]);
            	nextSeat.select();
            	
            	lastSeat = nextSeat;
    			break;
            }
    	}
    	
    	
    	// Suche den besten Platz
    	for(int r=0; r < views.length; r++) {
            for(int c=0; c< views[r].length; c++) {
            	
            	
            	
                //System.out.println("Values at arr["+r+"]["+c+"] is " + views[r][c]);
            }
        }
    	
//    	Values at arr[0][0] is SeatView@6fd4129b[styleClass=image-view]
//		Values at arr[0][1] is SeatView@2eb33627[styleClass=image-view]
//		Values at arr[0][2] is SeatView@4d57d155[styleClass=image-view]
//		Values at arr[0][3] is SeatView@58fe9ffb[styleClass=image-view]
//		Values at arr[0][4] is SeatView@17f11eb5[styleClass=image-view]
//		Values at arr[0][5] is SeatView@20c41062[styleClass=image-view]
//		Values at arr[0][6] is SeatView@53eab056[styleClass=image-view]
//		Values at arr[0][7] is SeatView@7f47ec97[styleClass=image-view]
//		Values at arr[0][8] is SeatView@60dc517c[styleClass=image-view]
//		Values at arr[0][9] is SeatView@609b0a05[styleClass=image-view]
//		Values at arr[1][0] is SeatView@66757e40[styleClass=image-view]
//		Values at arr[1][1] is SeatView@2616054d[styleClass=image-view]
//		Values at arr[1][2] is SeatView@2443e742[styleClass=image-view]
//		Values at arr[1][3] is SeatView@1cb00aed[styleClass=image-view]
//		Values at arr[1][4] is SeatView@51faf045[styleClass=image-view]
//		Values at arr[1][5] is SeatView@4750bb5a[styleClass=image-view]
//		Values at arr[1][6] is SeatView@7c123d05[styleClass=image-view]
//		Values at arr[1][7] is SeatView@6069b708[styleClass=image-view]
//		Values at arr[1][8] is SeatView@66170b98[styleClass=image-view]
//		Values at arr[1][9] is SeatView@245ff66b[styleClass=image-view]
    	
    	
    	
    	
    	
//    	int number = firstSeat.getSeat().getSeatNumber();
//    	SeatView nextSeat = views[row][col+1];
//    	nextSeat.select();
    	
//    	selectedSeats.add(nextSeat);
//    	Seat seat = nextSeat.getSeat();
//    	Ticket ticket = new Ticket(seat);
//  	  	ticketData.add(ticket);
  	  
  	  //SeatType[][] seatPlan = hall.getSeatPlan();
  	  
//    	Booking booking = new Booking();
//    	booking.setEvent(vorstellung);
//    	booking.setTickets(ticketData);
//    	try {
//			Main.startTicketZahlen(booking);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    };
    
    private EventHandler<ActionEvent> commandBuyHandler = (evt) -> {
    	// Booking erstellen und weiter zu zahlen
    	Booking booking = new Booking();
    	booking.setEvent(vorstellung);
    	booking.setTickets(ticketData);    	
    	// Aktuelle Reservierung setzten
    	Main.cinemaProgrammService.setCurrentReservation(booking);
    	try {
			Main.startTicketZahlen(booking);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    };

// Suche im GridPane
//    SeatView seat = null;
//	for (Node node: this.hallGrid.getChildren()) {
//		if (!SeatView.class.isAssignableFrom(node.getClass())) {
//			continue;
//		}
//		seat = (SeatView) node;
//	}
    
    
//	private void renderHallGrid() {
//	Hall hall = vorstellung.getHall();
//	int rows = hall.getRows();
//    int columns = hall.getColumns();
//    Boolean[][] seatPlan = hall.getSeatPlan();
//   
//	this.selectedSeats.clear();
//
//    ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
//    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
//
////    double x = gridWidth.get();
////    double y = gridHeight.get();
////    
////    NumberBinding size1 = gridWidth.subtract(220).divide(columns).subtract(0);
////    NumberBinding size2 = gridHeight.subtract(20).divide(rows).subtract(2);
//    
//    // Für den Zoom-Effekt
//    // High-Level Binding (Fluent API)
//    // Man braucht nun keine zusätzlichen Listener für Variablen-Änderungen mehr zu erstellen 
//    NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
//    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
//    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
//	
//    // Hier werden die Controls für die Sitzplätze entsprechend der Sitzplatz-Definition der Halle erstelllt 
//    int seatCounter = 1;
//    for (int row = 0; row < (rows ); row++) {
//      for (int column = 0; column < (columns ); column++) {
//        
//    	Seat seat = new Seat(row , column, seatCounter);
//    	seatCounter++;
//        seat.widthProperty().bind(size);
//        seat.heightProperty().bind(size);
//
//    	if(seatPlan[row][column] == false){
//    		seat.setDisable(true);
//    		seat.setOpacity(0);
//    	}
//    	
//        seat.getState().addListener((e, oldValue, newValue) -> {
//        	if(isTimeOver.get() == false) {
//	        	if (newValue) {
//		        	  if(selectedSeats.size() == 0)
//		        		  StartTimeAnimation();	        	  
//		        	  selectedSeats.add(seat);
//		        	  // Ticket für diesen Sitzplatz hinzufügen
//		        	  Ticket ticket = new Ticket(seat);
//	        	  ticketData.add(ticket);
//		          }
//		          else {
//		            this.selectedSeats.remove(seat);
//		            if(selectedSeats.size() == 0)
//		            	StopTimeAnimation();
//		            for(Ticket ticket : ticketData) {
//		                if(ticket.getSeat().equals(seat)) {
//		                	// Ticket für diesen Sitzplatz entfernen
//		                	ticketData.remove(ticket);
//		                }
//		            } 
//		          }		        		
//        	}
//    	  
//
//
//        });
//        
//        this.hallGrid.add(seat, column, row);   	
//      }
//    }
//    
//}
    
//	private void resizeSeat(Number newValue) {
//	Hall hall = vorstellung.getHall(); 
//    int rows = hall.getRows();
//    int columns = hall.getColumns();
//    
//	ReadOnlyDoubleProperty gridWidth = this.hallGrid.widthProperty();
//    ReadOnlyDoubleProperty gridHeight = this.hallGrid.heightProperty();
//    
//	NumberBinding size = new When(gridWidth.divide(columns).lessThan(gridHeight.divide(rows)))
//    		.then(gridWidth.subtract(20).divide(columns).subtract(2))
//    		.otherwise(gridHeight.subtract(20).divide(rows).subtract(2));
//	
//	for(Node node: this.hallGrid.getChildren()) {
//		Rectangle seat = (Rectangle)node;
//		seat.widthProperty().unbind();
//		seat.heightProperty().unbind();
//		seat.widthProperty().bind(size);
//    	seat.heightProperty().bind(size);
//	}
//}
}
