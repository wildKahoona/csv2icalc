package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ch.ffhs.kino.component.ProgrammTableColumnFactory;
import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie.GenreType;
import ch.ffhs.kino.model.Movie.MovieLanguage;
import ch.ffhs.kino.model.Vorstellung;
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

		programmtable.getColumns().add(ProgrammTableColumnFactory.createColumnCinema());

		createWeekColumns();

	}

	private void createWeekColumns() {
		// Nächstee Sieben Tage laden
		GregorianCalendar heute = new GregorianCalendar();
		for (int i = 0; i < 7; i++) {
			heute.add(GregorianCalendar.DAY_OF_YEAR, 1);

			SimpleDateFormat fmt = new SimpleDateFormat("EEE dd.MM");

			String string = fmt.format(heute.getTime());

			TableColumn<ProgrammTableModel, String> column = new TableColumn<>(string);
			column.getProperties().put("Date", heute.getTime());
			column.setCellValueFactory(new PropertyValueFactory<ProgrammTableModel, String>(string));
			column.setCellFactory(
					new Callback<TableColumn<ProgrammTableModel, String>, TableCell<ProgrammTableModel, String>>() {
						@Override
						public TableCell<ProgrammTableModel, String> call(TableColumn<ProgrammTableModel, String> col) {
							final TableCell<ProgrammTableModel, String> cell = new TableCell<ProgrammTableModel, String>() {
								@Override
								public void updateItem(String firstName, boolean empty) {
									super.updateItem(firstName, empty);

									ObservableMap<Object, Object> properties = col.getProperties();
									Date object = (Date) properties.get("Date");
									List<Vorstellung> timesForShow = getTimesForShow(filteredVorstellungen, object,
											null, null, null);

									SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
									if (timesForShow != null) {
										HBox hBox = new HBox();

										for (Vorstellung vorstellung : timesForShow) {

											Hyperlink value = new Hyperlink();
											value.getProperties().put("vorstellung", vorstellung);
											value.setText(fmt.format(vorstellung.getDate()));
											hBox.getChildren().add(value);

											value.addEventHandler(MouseEvent.MOUSE_CLICKED,
													new EventHandler<MouseEvent>() {
														@Override
														public void handle(MouseEvent event) {
															try {
																Hyperlink hl = (Hyperlink) event.getSource();

																Vorstellung v = (Vorstellung) hl.getProperties()
																		.get("vorstellung");
																Main.startMovieShow(v);
															} catch (IOException e) {
																e.printStackTrace();
															}
														}
													});

										}
										setGraphic(hBox);

									}
									if (empty) {

										setText(null);
									} else {
										setText(firstName);
									}
								}
							};

							return cell;
						}

					});
			programmtable.getColumns().add(column);
		}
	}

	protected void initRows() {
		data.clear();
		for (Vorstellung vorstellung : filteredVorstellungen) {
			data.add(new ProgrammTableModel(vorstellung.getShow().getMovie().getTitle(),
					vorstellung.getShow().getLanguage().getText(), vorstellung.getHall().getHallName(),
					vorstellung.getShow().getMovie().getGenreText(), vorstellung.getType().isThreeD(),
					vorstellung.getShow().getMovie()));

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

	private List<Vorstellung> getTimesForShow(List<Vorstellung> vorstellungen, Date date, String hall, String Language,
			String MovieTitle) {

		List<Vorstellung> temp = new ArrayList<Vorstellung>();
		if (vorstellungen != null) {
			for (Vorstellung vorstellung : vorstellungen) {

				if (date != null) {
					// Same day
					SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
					if (fmt.format(date).equals(fmt.format(vorstellung.getDate()))) {

						if (vorstellung.getHall().toString().equals(hall)) {
							if (vorstellung.getShow().getLanguage().toString().equals(Language)) {
								if (vorstellung.getShow().getMovie().getTitle().equals(MovieTitle)) {
									temp.add(vorstellung);
								}
							}

						}
					}

					ArrayList<Vorstellung> arrayList = new ArrayList<Vorstellung>();

					arrayList.add(filteredVorstellungen.get(0));
					return arrayList;
				}
			}
		}
		return null;

	}

}
