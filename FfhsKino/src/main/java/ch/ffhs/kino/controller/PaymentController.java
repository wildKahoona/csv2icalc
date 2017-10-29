package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import ch.ffhs.kino.component.TicketTable;
import ch.ffhs.kino.component.TicketTableHeader;
import ch.ffhs.kino.component.TimerAnimation;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.service.CinemaProgrammServiceMock;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PaymentController {

	@FXML
	public void breadcrumbAction(MouseEvent event) throws IOException {
		
		timer.stopTimeAnimation();
		getBooking().setTickets(ticketData);
		getBooking().setSessionRemainTime(timer.getRemainTime());
		
		if (event.getSource() instanceof Label) {
			Label label = (Label) event.getSource();
			if (!label.isDisable()) {
				String id = label.getId();
				if (id.equals("bc_sitzplatz")) {
					Main.startMovieShow(getBooking());
				}
			}
		} else {
			ControllerUtils.breadcrumbAction(event.getSource());
		}
	}
	
	@FXML
	private Label lbMovieShow;
	
	@FXML
	private TextField inputEmail;
	
	@FXML
	private RadioButton rbCreditCard;
	
	@FXML
	private RadioButton rbPayPal;
	
	@FXML
	private GridPane gridZahlung;
	
	@FXML
	private TextField inputKreditkartenNr;
	
	@FXML
	private TextField inputKarteninhaber;
	
	@FXML
	private ComboBox<String> cbMonat;
	
	@FXML
	private ComboBox<String> cbJahr;
	
	@FXML
	private TextField inputCvv;
		
	@FXML
	private Button btnPay;
	
	@FXML
	private GridPane gridTimer;
	
	@FXML
	private Label lbTimer;
	
	@FXML
	private VBox gridTickets;
	
	@FXML
	private	VBox gridSumTickets;
	
	private Booking booking;	
	private Boolean payCreditCard = true;	
	private TimerAnimation timer = new TimerAnimation();
	private SimpleDateFormat remainTimeFormat = new SimpleDateFormat("mm:ss");	
	private ValidationSupport validationSupport = new ValidationSupport();	
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	
	@FXML
	public void initialize() {
		Image imgCreditCard = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/visaCard.png"));
		ImageView imgViewCreditCard = new ImageView(imgCreditCard);
		imgViewCreditCard.setFitWidth(25.00);
		imgViewCreditCard.setFitHeight(25.00);
		rbCreditCard.setGraphic(imgViewCreditCard);
		rbCreditCard.setSelected(payCreditCard);
		
		Image imgPayPal = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/payPal.png"));
		ImageView imgViewPayPal = new ImageView(imgPayPal);
		imgViewPayPal.setFitWidth(25.00);
		imgViewPayPal.setFitHeight(25.00);
		rbPayPal.setGraphic(imgViewPayPal);
		rbPayPal.setSelected(!payCreditCard);
		
		// Kreditkarten-Felder
		ObservableList<String> options = FXCollections.observableArrayList();
		DecimalFormat decim = new DecimalFormat("00");
		for (int i = 1; i <= 12; i++) {
			options.add(decim.format(i));
		}
		ObservableList<String> jahre = FXCollections.observableArrayList();
		DecimalFormat decim2 = new DecimalFormat("2000");
		for (int i = 17; i <= 27; i++) {
			jahre.add(decim2.format(i));
		}

		cbMonat.setItems(options);
		cbJahr.setItems(jahre);
		
		cbMonat.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
				if (newValue != null) {
					//selectedCreditCardMonth = newValue;
				}  
			}
	    });
	    
		cbJahr.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
		        if (newValue != null) {
		        	//selectedCreditCardYear = newValue;
		        }  
		      }
		});		

		validationSupport.registerValidator(inputEmail, Validator.createEmptyValidator("Die E-Mail-Adresse ist obligatorisch."));
		validationSupport.registerValidator(inputKreditkartenNr, Validator.createEmptyValidator("Die Kreditkartennummer ist obligatorisch."));
		validationSupport.registerValidator(inputCvv, Validator.createEmptyValidator("Die CVV-Nummer ist obligatorisch."));
		validationSupport.registerValidator(inputKarteninhaber, Validator.createEmptyValidator("Der Name des Karteninhabers ist obligatorisch."));
		validationSupport.registerValidator(cbMonat, Validator.createEmptyValidator("Der Monat ist obligatorisch"));
		validationSupport.registerValidator(cbJahr, Validator.createEmptyValidator("Das Jahr ist obligatorisch"));
		
		BooleanBinding booleanBind = (validationSupport.invalidProperty()).or(Bindings.size(ticketData).isEqualTo(0));
		btnPay.disableProperty().bind(booleanBind);
		
		renderTicketTable();
		
		timer = new TimerAnimation();
		gridTimer.visibleProperty().bind(timer.timeElapsedProperty().not());
		timer.remainTimeProperty().addListener((ChangeListener<? super Number>) (o, oldVal, newVal) -> {
			lbTimer.setText(remainTimeFormat.format(timer.getRemainTime()));
        });
		timer.timeElapsedProperty().addListener((ChangeListener<? super Boolean>) (o, oldVal, newVal) -> {
			if(newVal) {
				// Alle Tickets entfernen
				Iterator<Ticket> tickets = ticketData.iterator();
				while (tickets.hasNext()) {
					tickets.next();
					tickets.remove();
				}
				
				// Reset Buchungszeit
				getBooking().setSessionRemainTime(CinemaProgrammServiceMock.SESSION_TIME);
				
				// Meldung an den Benutzer
				Alert alert = new Alert(AlertType.WARNING);
			    alert.setTitle("Reservierungszeit abgelaufen");
			    alert.setHeaderText("Bitte w�hlen Sie neue Pl�tze");
			    alert.setContentText("Die Reservierungszeit ist abgelaufen, daher wurden Ihre Pl�tze freigegeben.");
			    alert.showAndWait();
			    
			    //gridTimer.
			}
		});
	}

	@FXML
	protected void creditSelected(ActionEvent event) {
		payCreditCard = true;
		gridZahlung.setVisible(true);
	}
	
	@FXML
	protected void paypalSelected(ActionEvent event) {
		payCreditCard = false;
		gridZahlung.setVisible(false);
	}

	@FXML
	protected void pay(ActionEvent event) {
		try {					
			Main.startBookingConfirm(booking);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setTitle() {
		if(this.booking == null) return;
		String movieTitle = booking.getEvent().getShow().getMovie().getTitle();
		String movieLanguage = booking.getEvent().getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		lbMovieShow.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(booking.getEvent().getDate()) + ", " + booking.getEvent().getHall().getHallName());
	}

	private void renderTicketTable() {			
//		TicketTable table = new TicketTable(ticketData, gridTickets);
//		table.createTicketListener(null, gridTickets);
		TicketTableHeader ticketHeader = new TicketTableHeader(ticketData, gridSumTickets);
		ticketHeader.createTicketListener();
		TicketTable table = new TicketTable(ticketData, gridTickets);
		table.createTicketListener(null);
	}
	
	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
		if(this.booking == null) return;
		setTitle();
		
		timer.startTimeAnimation(booking.getSessionRemainTime());
		List<Ticket> tickets = this.booking.getTickets();
		if(tickets == null) return;
		for(Ticket ticket : tickets){
			ticketData.add(ticket);
		}
	}
}
