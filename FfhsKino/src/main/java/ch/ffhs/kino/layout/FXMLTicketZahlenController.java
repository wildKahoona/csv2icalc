package ch.ffhs.kino.layout;

import java.io.IOException;
import java.text.DecimalFormat;

import ch.ffhs.kino.table.model.TicketTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class FXMLTicketZahlenController {
	@FXML
	private TableView<TicketTableModel> tableView;

	@FXML
	private Label vorstellung;
	@FXML
	private GridPane kk;

	@FXML
	private ComboBox<String> monat;
	@FXML
	private ComboBox<String> jahr;

	@FXML
	public void initialize() {

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

		monat.setItems(options);
		jahr.setItems(jahre);

	}

	@FXML
	protected void pay(ActionEvent event) {

		try {
			Main.startKinoProgramm();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	protected void paypalSelected(ActionEvent event) {
		kk.setVisible(false);
	}

	@FXML
	protected void kkSelected(ActionEvent event) {
		kk.setVisible(true);
	}

}