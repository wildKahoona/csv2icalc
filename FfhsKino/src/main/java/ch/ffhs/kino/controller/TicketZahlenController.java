package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import ch.ffhs.kino.component.TicketRow;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Booking;
import ch.ffhs.kino.model.Ticket;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.model.Ticket.TicketType;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.TextAlignment;

public class TicketZahlenController {

	/**
	 * Birgit
	 * TODO: Fertigstellen
	 */
	
//	@FXML
//	private TableView<TicketTableModel> tableView;

	@FXML
	private Label vorstellungChoice;
	
	@FXML
	private VBox ticketGrid;
		
	@FXML
	private GridPane kk;
	
	@FXML
	private TextField email;
	
	@FXML
	private RadioButton rbtnCreditCard;
	
	@FXML
	private RadioButton rbtnPayPal;
	
	@FXML
	private TextField kknummer;

	@FXML
	private ComboBox<String> cbMonat;
	
	@FXML
	private ComboBox<String> cbJahr;
	
	@FXML
	private TextField cvv;
	
	@FXML
	private TextField karteninhaber;	
	
	@FXML
	private Button payButton;
	
	@FXML
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());
	}
	
	
	private Booking booking;
	private Boolean payCreditCard = true;
	private String selectedCreditCardMonth;
	private String selectedCreditCardYear;
	
	/**
     * Die Liste der Reservierungen
     */
	private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();
	
	private ValidationSupport validationSupport = new ValidationSupport();
	//private boolean isValid = true;
	
	@FXML
	public void initialize() {
		
		Image imgCreditCard = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/visaCard.png"));
		ImageView imgViewCreditCard = new ImageView(imgCreditCard);
		imgViewCreditCard.setFitWidth(25.00);
		imgViewCreditCard.setFitHeight(25.00);
		rbtnCreditCard.setGraphic(imgViewCreditCard);
		rbtnCreditCard.setSelected(payCreditCard);
		
		Image imgPayPal = new Image(Main.class.getResourceAsStream("/ch/ffhs/kino/images/payPal.png"));
		ImageView imgViewPayPal = new ImageView(imgPayPal);
		imgViewPayPal.setFitWidth(25.00);
		imgViewPayPal.setFitHeight(25.00);
		rbtnPayPal.setGraphic(imgViewPayPal);
		rbtnPayPal.setSelected(!payCreditCard);
		
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
					selectedCreditCardMonth = newValue;
				}  
			}
	    });
	    
		cbJahr.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
		        if (newValue != null) {
		        	selectedCreditCardYear = newValue;
		        }  
		      }
		});		

		validationSupport.registerValidator(email, Validator.createEmptyValidator("Die E-Mail-Adresse ist obligatorisch."));
		validationSupport.registerValidator(kknummer, Validator.createEmptyValidator("Die Kreditkartennummer ist obligatorisch."));
		validationSupport.registerValidator(cvv, Validator.createEmptyValidator("Die CVV-Nummer ist obligatorisch."));
		validationSupport.registerValidator(karteninhaber, Validator.createEmptyValidator("Der Name des Karteninhabers ist obligatorisch."));
		validationSupport.registerValidator(cbMonat, Validator.createEmptyValidator("Der Monat ist obligatorisch"));
		validationSupport.registerValidator(cbJahr, Validator.createEmptyValidator("Das Jahr ist obligatorisch"));
		
		payButton.disableProperty().bind(validationSupport.invalidProperty());
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
		setTitle();
		initTicketControl();
		if(this.booking == null) return;
		List<Ticket> tickets = this.booking.getTickets();
		if(tickets == null || tickets.isEmpty()) return;
		for(Ticket ticket : tickets){
			ticketData.add(ticket);
		}
	}

	public Booking getBooking() {
		return booking;
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

	@FXML
	protected void paypalSelected(ActionEvent event) {
		payCreditCard = false;
		kk.setVisible(false);
	}

	@FXML
	protected void kkSelected(ActionEvent event) {
		payCreditCard = true;
		kk.setVisible(true);
	}

	private void setTitle() {
		Vorstellung event = booking.getEvent();
		String movieTitle = event.getShow().getMovie().getTitle();
		String movieLanguage = event.getShow().getLanguage().getText();
				
		SimpleDateFormat fmt = new SimpleDateFormat("E dd MMM yyyy HH:mm");		
		vorstellungChoice.setText(movieTitle + " (" + movieLanguage + "), " + fmt.format(event.getDate()) + ", " + event.getHall().getHallName());
	}
	
	private void initTicketControl() {

	}


}