package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie.GenreType;
import ch.ffhs.kino.model.Movie.MovieLanguage;
import ch.ffhs.kino.model.Vorstellung;
import ch.ffhs.kino.table.model.ProgrammTableModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


/**
 * TODO: Denis: Movies gruppieren
 * TODO: Denis: Richtige Zeiten anzeigen
 * FIXME: Methode implementieren die Zeiten nach Saal Movie und Sprache zurückgibt  Denis Bittante
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

		TableColumn<ProgrammTableModel, ProgrammTableModel> filmCol = createFilmColumn();
		programmtable.getColumns().add(filmCol);

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
																Main.startVorstellung(v);
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

	private TableColumn<ProgrammTableModel, ProgrammTableModel> createFilmColumn() {
		// https://stackoverflow.com/questions/16360323/javafx-table-how-to-add-components
		TableColumn<ProgrammTableModel, ProgrammTableModel> filmCol = new TableColumn<>("Film");
		filmCol.setMinWidth(220);
		filmCol.setCellValueFactory(
				new Callback<CellDataFeatures<ProgrammTableModel, ProgrammTableModel>, ObservableValue<ProgrammTableModel>>() {
					@Override
					public ObservableValue<ProgrammTableModel> call(
							CellDataFeatures<ProgrammTableModel, ProgrammTableModel> features) {
						return new ReadOnlyObjectWrapper(features.getValue());
					}
				});

		filmCol.setCellFactory(
				new Callback<TableColumn<ProgrammTableModel, ProgrammTableModel>, TableCell<ProgrammTableModel, ProgrammTableModel>>() {
					@Override
					public TableCell<ProgrammTableModel, ProgrammTableModel> call(
							TableColumn<ProgrammTableModel, ProgrammTableModel> btnCol) {
						return new TableCell<ProgrammTableModel, ProgrammTableModel>() {

							@Override
							public void updateItem(final ProgrammTableModel programm, boolean empty) {
								super.updateItem(programm, empty);
								if (programm != null) {

									final Hyperlink title = setFilmGraphics(programm);
									title.getProperties().put("movie", programm.getMovie());

									title.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											// nicht die beste Lösung ..
											for (Vorstellung vorstellung : filteredVorstellungen) {
												if (vorstellung.getShow().getMovie().getTitle()
														.equals(((Hyperlink) event.getSource()).getText())) {
													try {
														Main.startMovieDetail(vorstellung.getShow().getMovie());
													} catch (IOException e) {
													// do nothing else
														e.printStackTrace();
													}
													break;
												}
											}
										}
									});
								} else {
									setGraphic(null);
								}
							}

							private Hyperlink setFilmGraphics(final ProgrammTableModel programm) {
								final GridPane infos = new GridPane();

								final Label genre = new Label();
								genre.setText(new String(programm.getGenre()));

								final Hyperlink title = new Hyperlink();
								title.setText(new String(programm.getFilm()));

								if (programm.getThreeD()) {
									Image value = new Image(
											getClass().getResourceAsStream("/ch/ffhs/kino/images/3d.png"));
									final ImageView threeDImage = new ImageView(value);

									threeDImage.prefHeight(30);
									threeDImage.setFitHeight(30.0);
									threeDImage.prefWidth(30);
									threeDImage.setFitWidth(30);

									infos.add(threeDImage, 1, 0);
								}

								infos.add(title, 0, 0);
								infos.add(genre, 0, 1);
								setGraphic(infos);
								return title;
							}
						};
					}
				});
		return filmCol;
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
