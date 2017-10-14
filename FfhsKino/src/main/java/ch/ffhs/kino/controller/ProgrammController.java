package ch.ffhs.kino.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javafx.util.Callback;

public class ProgrammController {

	/**
	 * TODO: Denis Fertigstellen
	 */
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
	ComboBox sucheGenre;

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
		columns.add("Sprache");
		columns.add("Saal");

		// https://stackoverflow.com/questions/16360323/javafx-table-how-to-add-components
		TableColumn<ProgrammTableModel, ProgrammTableModel> btnCol = new TableColumn<>("Film");
		btnCol.setMinWidth(220);
		btnCol.setCellValueFactory(
				new Callback<CellDataFeatures<ProgrammTableModel, ProgrammTableModel>, ObservableValue<ProgrammTableModel>>() {
					@Override
					public ObservableValue<ProgrammTableModel> call(
							CellDataFeatures<ProgrammTableModel, ProgrammTableModel> features) {
						return new ReadOnlyObjectWrapper(features.getValue());
					}
				});

		btnCol.setCellFactory(
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
														// TODO Auto-generated
														// catch block
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
		programmtable.getColumns().add(btnCol);

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
											for (Vorstellung vorstellung : filteredVorstellungen) {
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
		data.clear();
		for (Vorstellung vorstellung : filteredVorstellungen) {
			data.add(new ProgrammTableModel(vorstellung.getShow().getMovie().getTitle(),
					vorstellung.getShow().getLanguage().getText(), vorstellung.getHall().getHallName(),
					vorstellung.getShow().getMovie().getGenreText(), vorstellung.getType().isThreeD()));

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
					if (sucheGenre.getValue() != "") {

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
