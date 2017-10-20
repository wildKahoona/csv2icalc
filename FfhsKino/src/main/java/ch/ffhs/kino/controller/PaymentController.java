package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import ch.ffhs.kino.component.TicketTable;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());
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
	private VBox gridTickets;
	
	private Booking booking;
	
	private Boolean payCreditCard = true;
	
	private Timeline timeline;
	
	private long sessionRemainTime;
	
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
		loadData();
		setTitle();
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
			Main.cinemaProgrammService.addBooking(booking);
			Main.startBookingConfirm(booking);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadData() {
		setBooking(Main.cinemaProgrammService.getCurrentReservation());
		sessionRemainTime = Main.cinemaProgrammService.getSessionRemainTime();
	}
	
	private void setTitle() {
		if(this.booking == null) return;
		String movieTitle = booking.getEvent().getShow().getMovie().getTitle();
		String movieLanguage = booking.getEvent().getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		lbMovieShow.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(booking.getEvent().getDate()) + ", " + booking.getEvent().getHall().getHallName());
	}

	private void renderTicketTable() {			
		TicketTable table = new TicketTable(ticketData, gridTickets);
		table.createTicketListener(null, gridTickets);
	}
	
	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
		if(this.booking == null) return;
		List<Ticket> tickets = this.booking.getTickets();
		// Leider nein, muss die Tickets einzeln zur ObservableList hinzufügen, damit der AddListener anspringt
		//ticketData = FXCollections.observableArrayList(tickets);
		if(tickets == null) return;
		for(Ticket ticket : tickets){
			ticketData.add(ticket);
		}
	}
}
