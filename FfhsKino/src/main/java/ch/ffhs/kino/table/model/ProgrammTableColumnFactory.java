package ch.ffhs.kino.table.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.model.Vorstellung;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ProgrammTableColumnFactory {

	public TableColumn createColumnCinema() {
		TableColumn<ProgrammTableModel, ProgrammTableModel> filmCol = new TableColumn<>("Film");
		filmCol.setSortable(false);
		filmCol.setEditable(false);
		filmCol.setMinWidth(220);
		filmCol.setCellValueFactory(
				new Callback<CellDataFeatures<ProgrammTableModel, ProgrammTableModel>, ObservableValue<ProgrammTableModel>>() {
					@Override
					public ObservableValue<ProgrammTableModel> call(
							CellDataFeatures<ProgrammTableModel, ProgrammTableModel> features) {
						ProgrammTableModel value = features.getValue();
						return new ReadOnlyObjectWrapper(value);
					}
				});

		filmCol.setComparator(new Comparator<ProgrammTableModel>() {

			@Override
			public int compare(ProgrammTableModel o1, ProgrammTableModel o2) {
				return o1.getFilm().compareTo(o2.getFilm());
			}
		});

		filmCol.setCellFactory(
				new Callback<TableColumn<ProgrammTableModel, ProgrammTableModel>, TableCell<ProgrammTableModel, ProgrammTableModel>>() {
					@Override
					public TableCell<ProgrammTableModel, ProgrammTableModel> call(
							TableColumn<ProgrammTableModel, ProgrammTableModel> btnCol) {
						return new CinemaCell();
					}
				});

		return filmCol;
	}

	public TableColumn createColumnLanguage() {
		TableColumn<ProgrammTableModel, ProgrammTableModel> filmCol = new TableColumn<>("Sprache");
		filmCol.setSortable(false);
		filmCol.setEditable(false);
		filmCol.setMinWidth(20);
		filmCol.setCellValueFactory(
				new Callback<CellDataFeatures<ProgrammTableModel, ProgrammTableModel>, ObservableValue<ProgrammTableModel>>() {
					@Override
					public ObservableValue<ProgrammTableModel> call(
							CellDataFeatures<ProgrammTableModel, ProgrammTableModel> features) {
						ProgrammTableModel value = features.getValue();
						return new ReadOnlyObjectWrapper(value);
					}
				});

		filmCol.setComparator(new Comparator<ProgrammTableModel>() {

			@Override
			public int compare(ProgrammTableModel o1, ProgrammTableModel o2) {
				return o1.getSprache().compareTo(o2.getSprache());
			}
		});

		filmCol.setCellFactory(
				new Callback<TableColumn<ProgrammTableModel, ProgrammTableModel>, TableCell<ProgrammTableModel, ProgrammTableModel>>() {
					@Override
					public TableCell<ProgrammTableModel, ProgrammTableModel> call(
							TableColumn<ProgrammTableModel, ProgrammTableModel> btnCol) {
						return new LanguageCell();
					}
				});

		return filmCol;
	}

	public TableColumn createColumnTime(String columntitle, Date day) {
		TableColumn<ProgrammTableModel, ProgrammTableModel> dateCol = new TableColumn<>(columntitle);
		dateCol.setSortable(false);
		dateCol.setEditable(false);
		dateCol.setMinWidth(20);
		dateCol.getProperties().put("day", day);
		dateCol.setCellValueFactory(
				new Callback<CellDataFeatures<ProgrammTableModel, ProgrammTableModel>, ObservableValue<ProgrammTableModel>>() {
					@Override
					public ObservableValue<ProgrammTableModel> call(
							CellDataFeatures<ProgrammTableModel, ProgrammTableModel> features) {
						ProgrammTableModel value = features.getValue();
						return new ReadOnlyObjectWrapper(value);
					}
				});

		dateCol.setCellFactory(
				new Callback<TableColumn<ProgrammTableModel, ProgrammTableModel>, TableCell<ProgrammTableModel, ProgrammTableModel>>() {
					@Override
					public TableCell<ProgrammTableModel, ProgrammTableModel> call(
							TableColumn<ProgrammTableModel, ProgrammTableModel> btnCol) {

						return new TimeCell();
					}
				});

		return dateCol;
	}

	public class TimeCell extends TableCell<ProgrammTableModel, ProgrammTableModel> {

		@Override
		public void updateItem(final ProgrammTableModel programm, boolean empty) {
			super.updateItem(programm, empty);

			if (!empty) {

				SimpleDateFormat fmt = new SimpleDateFormat("EEE dd.MM");
				SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm");

				VBox vBox = new VBox();

				Date dayKey = (Date) getTableColumn().getProperties().get("day");
				if (dayKey != null) {
					List<Vorstellung> vorstellungByDate = programm.getVorstellungByDate(dayKey);
					if (vorstellungByDate != null) {
						for (Vorstellung vorstellung : vorstellungByDate) {

							Hyperlink value = new Hyperlink();
							value.getProperties().put("vorstellung", vorstellung);
							value.setText(fmtTime.format(vorstellung.getDate().getTime()));
							vBox.getChildren().add(value);

							value.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent event) {
									try {
										Hyperlink hl = (Hyperlink) event.getSource();

										Vorstellung v = (Vorstellung) hl.getProperties().get("vorstellung");
										Main.startMovieShow(v);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							});

						}
					}
				}
				setGraphic(vBox);

			}
		}
	};

	public class LanguageCell extends TableCell<ProgrammTableModel, ProgrammTableModel> {

		@Override
		public void updateItem(final ProgrammTableModel programm, boolean empty) {
			super.updateItem(programm, empty);
			if (!empty) {
				setText(programm.getSprache());
			}
		}
	};

	public class CinemaCell extends TableCell<ProgrammTableModel, ProgrammTableModel> {

		@Override
		public void updateItem(final ProgrammTableModel programm, boolean empty) {
			super.updateItem(programm, empty);

			// super.updateItem(programm, empty);
			// Check if the previous row has the same
			// content

			if (!empty) {
				boolean visible = true;

				ProgrammTableModel vor = getTableColumn().getCellData(getIndex() - 1);
				ProgrammTableModel hier = getTableColumn().getCellData(getIndex());

				if (vor != null) {

					if (hier.getMovie().getTitle().equals(vor.getMovie().getTitle())) {
						visible = false;
					} else {
						System.out.println("this Film: " + hier.getFilm() + " vorCell: " + programm.getFilm());
					}
					if (programm.isOdd()) {
						getTableRow().setId("ODD");
					} else {
						getTableRow().setId("EVEN");
					}
				}

				if (programm != null) {

					final Hyperlink title = setFilmGraphics(programm, visible);

					title.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							try {
								Hyperlink link = (Hyperlink) event.getSource();
								Movie m = (Movie) link.getProperties().get("movie");
								Main.startMovieDetail(m);
							} catch (IOException e) {
								// do nothing else
								e.printStackTrace();
							}
						}
					});
				} else {
					setGraphic(null);
				}
			}
		}

		private Hyperlink setFilmGraphics(final ProgrammTableModel programm, boolean visible) {

			final GridPane infos = new GridPane();

			final Label genre = new Label();
			genre.setText(new String(programm.getGenre()));
			genre.setVisible(visible);

			final Hyperlink title = new Hyperlink();
			title.setText(new String(programm.getFilm()));
			title.getProperties().put("movie", programm.getMovie());
			title.setVisible(visible);

			if (programm.getThreeD()) {
				final ImageView threeDImage = getThreeD();

				infos.add(threeDImage, 1, 0);

			}

			infos.add(title, 0, 0);
			infos.add(genre, 0, 1);
			if (visible) {
				getTableRow().setStyle("-fx-border-width: 1 0 1 0; -fx-border-color: gray gray white white;");
			} else {
				getTableRow().setStyle("-fx-border-width: 1 0 0 0; -fx-border-color: white;");

			}

			setGraphic(infos);
			return title;
		}

	}

	private ImageView getThreeD() {
		Image value = new Image(getClass().getResourceAsStream("/ch/ffhs/kino/images/3d.png"));
		final ImageView threeDImage = new ImageView(value);

		threeDImage.prefHeight(30);
		threeDImage.setFitHeight(30.0);
		threeDImage.prefWidth(30);
		threeDImage.setFitWidth(30);
		return threeDImage;
	}

	public TableColumn<ProgrammTableModel, ?> createColumnHall() {
		TableColumn<ProgrammTableModel, ProgrammTableModel> hallColumn = new TableColumn<>("Saal");
		hallColumn.setSortable(false);
		hallColumn.setEditable(false);
		hallColumn.setMinWidth(40);
		hallColumn.setCellValueFactory(
				new Callback<CellDataFeatures<ProgrammTableModel, ProgrammTableModel>, ObservableValue<ProgrammTableModel>>() {
					@Override
					public ObservableValue<ProgrammTableModel> call(
							CellDataFeatures<ProgrammTableModel, ProgrammTableModel> features) {
						ProgrammTableModel value = features.getValue();
						return new ReadOnlyObjectWrapper(value);
					}
				});
		hallColumn.setCellFactory(
				new Callback<TableColumn<ProgrammTableModel, ProgrammTableModel>, TableCell<ProgrammTableModel, ProgrammTableModel>>() {
					@Override
					public TableCell<ProgrammTableModel, ProgrammTableModel> call(
							TableColumn<ProgrammTableModel, ProgrammTableModel> btnCol) {

						return new HallCell();
					}
				});

		return hallColumn;

	}

	public class HallCell extends TableCell<ProgrammTableModel, ProgrammTableModel> {

		@Override
		public void updateItem(final ProgrammTableModel programm, boolean empty) {
			super.updateItem(programm, empty);
			if (!empty) {
				setText(programm.getSaal());
			}
		}
	}

	public Object createColumnThreeD() {
		TableColumn<ProgrammTableModel, ProgrammTableModel> threeDColumn = new TableColumn<>("3D");
		threeDColumn.setSortable(false);
		threeDColumn.setEditable(false);
		threeDColumn.setMinWidth(40);
		threeDColumn.setCellValueFactory(
				new Callback<CellDataFeatures<ProgrammTableModel, ProgrammTableModel>, ObservableValue<ProgrammTableModel>>() {
					@Override
					public ObservableValue<ProgrammTableModel> call(
							CellDataFeatures<ProgrammTableModel, ProgrammTableModel> features) {
						ProgrammTableModel value = features.getValue();
						return new ReadOnlyObjectWrapper(value);
					}
				});
		threeDColumn.setCellFactory(
				new Callback<TableColumn<ProgrammTableModel, ProgrammTableModel>, TableCell<ProgrammTableModel, ProgrammTableModel>>() {
					@Override
					public TableCell<ProgrammTableModel, ProgrammTableModel> call(
							TableColumn<ProgrammTableModel, ProgrammTableModel> btnCol) {

						return new ThreeDCell();
					}
				});

		return threeDColumn;

	};

	public class ThreeDCell extends TableCell<ProgrammTableModel, ProgrammTableModel> {

		@Override
		public void updateItem(final ProgrammTableModel programm, boolean empty) {
			super.updateItem(programm, empty);
			if (!empty) {

				if (programm.getThreeD()) {
					setGraphic(getThreeD());
					
				}else 
				{
					setGraphic(null);
					
				}

			}
		}

	}

}
