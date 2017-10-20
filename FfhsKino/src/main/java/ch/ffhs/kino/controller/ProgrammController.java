package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie.GenreType;
import ch.ffhs.kino.model.Movie.MovieLanguage;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.table.model.ProgrammTableColumnFactory;
import ch.ffhs.kino.table.model.ProgrammTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * TODO: Denis: Movies gruppieren TODO: Denis: Richtige Zeiten anzeigen FIXME:
 * Methode implementieren die Zeiten nach Saal Movie und Sprache zurückgibt
 * Denis Bittante
 */
public class ProgrammController {

	@FXML
	TableView<ProgrammTableModel> programmtable;

	List<String> columns = new ArrayList<String>();

	private List<Vorstellung> vorstellungen = new ArrayList<Vorstellung>();

	public void initVorstellungen(List<Vorstellung> vorstellungen) {
		this.vorstellungen = vorstellungen;
		setFilteredVorstellungen(vorstellungen);
		initRows();
	}

	public List<Vorstellung> getVorstellungen() {
		return vorstellungen;
	}

	private List<Vorstellung> filteredVorstellungen = new ArrayList<>();

	public List<Vorstellung> getFilteredVorstellungen() {
		return filteredVorstellungen;
	}

	public void setFilteredVorstellungen(List<Vorstellung> filteredVorstellungen) {
		this.filteredVorstellungen = filteredVorstellungen;

	}

	private final ObservableList<ProgrammTableModel> data = FXCollections.observableArrayList();

	@FXML
	ComboBox<GenreType> sucheGenre;

	@FXML
	CheckBox suche3D;

	@FXML
	ComboBox sucheLanguage;

	@FXML
	TextField sucheBegriff;

	@FXML
	protected void initialize() {

		initTable();
		sucheGenre.getItems().addAll(GenreType.values());
		sucheLanguage.getItems().addAll(MovieLanguage.values());

	}

	private void initTable() {

		ProgrammTableColumnFactory programmTableColumnFactory = new ProgrammTableColumnFactory();
		programmtable.getColumns().add(programmTableColumnFactory.createColumnCinema());
		programmtable.getColumns().add(programmTableColumnFactory.createColumnLanguage());
		programmtable.getColumns().add(programmTableColumnFactory.createColumnHall());

		GregorianCalendar heute = new GregorianCalendar();

		for (int i = 0; i < 7; i++) {
			heute.add(GregorianCalendar.DAY_OF_YEAR, 1);

			SimpleDateFormat fmt = new SimpleDateFormat("EEE dd.MM");

			String string = fmt.format(heute.getTime());

			programmtable.getColumns().add(programmTableColumnFactory.createColumnTime(string, heute.getTime()));

		}

	}

	protected void initRows() {
		data.clear();
		for (Vorstellung vorstellung : filteredVorstellungen) {

			ProgrammTableModel e = new ProgrammTableModel(vorstellung.getShow().getMovie().getTitle(),
					vorstellung.getShow().getLanguage().getText(), vorstellung.getHall().getHallName(),
					vorstellung.getShow().getMovie().getGenreText(), vorstellung.getType().isThreeD(),
					vorstellung.getShow().getMovie());
			if (!data.contains(e)) {
				data.add(e);
			} else {

				for (int i = 0; i < data.size(); i++) {

					if (data.get(i).equals(e)) {

						data.get(i).addShowTime(vorstellung);
					}
				}
			}

		}
		Collections.sort(data, new Comparator<ProgrammTableModel>() {

			@Override
			public int compare(ProgrammTableModel o1, ProgrammTableModel o2) {
				return o1.getConcatID().compareTo(o2.getConcatID());
			}
		});

		if (data.size() > 0) {
			data.get(0).setOdd(true);
			for (int i = 1; i < data.size(); i++) {
				if (data.get(i).getFilm().equals(data.get(i - 1).getFilm())) {
					data.get(i).setOdd(data.get(i - 1).isOdd());
				} else {

					if (data.get(i - 1).isOdd()) {
						data.get(i).setOdd(false);
					} else {
						data.get(i).setOdd(true);
					}

				}
			}
		}
		programmtable.setItems(data);
		programmtable.refresh();

	}

	@FXML
	public void search() {

		filteredVorstellungen = new ArrayList<Vorstellung>(vorstellungen);

		for (int i = filteredVorstellungen.size() - 1; i >= 0; i--) {

			Vorstellung vorstellung = filteredVorstellungen.get(i);
			if (suche3D.isSelected()) {
				if (!vorstellung.getType().isThreeD()) {
					filteredVorstellungen.remove(i);
					continue;
				}
			}
			if (sucheGenre != null) {
				if (sucheGenre.getValue() != null) {
					if (sucheGenre.getValue() != GenreType.NONE) {

						if (!vorstellung.getShow().getMovie().getGenreText()
								.contains(sucheGenre.getValue().toString())) {
							filteredVorstellungen.remove(i);
							continue;
						}
					}
				}

			}
			if (sucheLanguage != null) {
				if (sucheLanguage.getValue() != null)
					if (sucheLanguage.getValue() != "") {

						if (!vorstellung.getShow().getLanguage().getLongText()
								.contains(sucheLanguage.getValue().toString())) {
							filteredVorstellungen.remove(i);
							continue;
						}

					}
			}

			if (sucheBegriff.getLength() > 0) {
				if (sucheBegriff.getText() != "") {

					String[] split = sucheBegriff.getText().split(" ");
					boolean found = false;
					for (String searchToken : split) {

						if (vorstellung.toString().toUpperCase().contains(searchToken.toUpperCase())) {
							found = true;
						}
					}
					if (!found) {
						filteredVorstellungen.remove(i);
					}

				}
			}

		}

		initRows();

	}

	@FXML
	public void breadcrumbAction(MouseEvent event) {
		ControllerUtils.breadcrumbAction(event.getSource());

	}

}
