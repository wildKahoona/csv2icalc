package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.ws.ServiceMode;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.service.CinemaProgrammServiceMock;
import ch.ffhs.kino.table.model.ProgrammTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ProgrammController {


	/**
	 * TODO: Denis Fertigstellen
	 */
	//FIXME: hahllo 
	@FXML
	TableView<ProgrammTableModel> programmtable;
	@FXML
	TextField searchKey;

	List<String> columns = new ArrayList<String>();

	private List<Vorstellung> vorstellungen = new ArrayList<Vorstellung>();

	public void setVorstellungen(List<Vorstellung> vorstellungen) {
		this.vorstellungen = vorstellungen;
		initRows();
	}

	private final ObservableList<ProgrammTableModel> data = FXCollections.observableArrayList();

	@FXML
	protected void initialize() {

		columns.add("Film");
		columns.add("Sprache");
		columns.add("Saal");

		// Nächstee Sieben Tage laden
		GregorianCalendar heute = new GregorianCalendar();
		for (int i = 0; i < 7; i++) {
			heute.add(GregorianCalendar.DAY_OF_YEAR, 1);

			SimpleDateFormat fmt = new SimpleDateFormat("EEE dd.MM");

			columns.add(fmt.format(heute.getTime()));

		}

		for (String string : columns) {
			TableColumn<ProgrammTableModel, String> column = new TableColumn<>(string);
			column.setCellValueFactory(new PropertyValueFactory<ProgrammTableModel, String>(string));
			column.setCellFactory(
					new Callback<TableColumn<ProgrammTableModel, String>, TableCell<ProgrammTableModel, String>>() {
						@Override
						public TableCell<ProgrammTableModel, String> call(TableColumn<ProgrammTableModel, String> col) {
							final TableCell<ProgrammTableModel, String> cell = new TableCell<ProgrammTableModel, String>() {
								@Override
								public void updateItem(String firstName, boolean empty) {
									super.updateItem(firstName, empty);
									if (empty) {
										setText(null);
									} else {
										setText(firstName);
									}
								}
							};
							cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent event) {

									Object source = event.getSource();
									TableColumn<ProgrammTableModel, String> tableColumn = cell.getTableColumn();

									if (tableColumn.getText().equals("Film")) {
										try {
											// nicht die beste Lösung ..
											for (Vorstellung vorstellung : vorstellungen) {
												if (vorstellung.getShow().getMovie().getTitle()
														.equals(cell.getItem())) {
													Main.startMovieDetail(vorstellung.getShow().getMovie());
													break;
												}
											}

										} catch (IOException e) {
											e.printStackTrace();
										}
									}

									System.out.println(source);
									System.out.println("double click on " + cell.getItem());

								}
							});
							return cell;
						}

					});
			programmtable.getColumns().add(column);
		}
	}

	protected void initRows() {
		for (Vorstellung vorstellung : vorstellungen) {
			data.add(new ProgrammTableModel(vorstellung.getShow().getMovie().getTitle(),
					vorstellung.getShow().getLanguage().getText(), vorstellung.getHall().getHallName()));

		}

		programmtable.setItems(data);

	}

	@FXML
	public void search(KeyEvent event) {
		searchKey.setPromptText("You Have clicked");
	}
}
