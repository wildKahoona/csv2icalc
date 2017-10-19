package ch.ffhs.kino.component;

import java.io.IOException;
import java.util.Comparator;

import com.sun.jmx.remote.security.FileLoginModule;

import ch.ffhs.kino.layout.Main;
import ch.ffhs.kino.model.Movie;
import ch.ffhs.kino.table.model.ProgrammTableModel;
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
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class ProgrammTableColumnFactory {

	public static TableColumn createColumnCinema() {
		TableColumn<ProgrammTableModel, ProgrammTableModel> filmCol = new TableColumn<>("Film");
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

	public static class CinemaCell extends TableCell<ProgrammTableModel, ProgrammTableModel> {


		private boolean color = true;

		private boolean getColor() {
			return color;
		}

		private void swapColor() {
			if (color) {
				color = false;
			} else {
				color = true;
			}
		}

		@Override
		public void updateItem(final ProgrammTableModel programm, boolean empty) {
			// super.updateItem(programm, empty);

			// Check if the previous row has the same
			// content
			boolean visible = true;

			ProgrammTableModel vor = getTableColumn().getCellData(getIndex() - 1);
			ProgrammTableModel hier = getTableColumn().getCellData(getIndex());

			if (vor != null) {

				if (hier.getMovie().getTitle().equals(vor.getMovie().getTitle())) {
					visible = false;
				} else {
					System.out.println("this Film: " + hier.getFilm() + " vorCell: " + programm.getFilm());
					swapColor();

				}
				if(getColor()){
					
					getTableRow().setId("ODD");
				}else{
					
					getTableRow().setId("EVEN");
				}

			}

			if (programm != null) {

				final Hyperlink title = setFilmGraphics(programm, visible, getColor());
				title.getProperties().put("movie", programm.getMovie());

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

		private Hyperlink setFilmGraphics(final ProgrammTableModel programm, boolean visible, boolean odd) {
			final GridPane infos = new GridPane();
			// infos.setVisible(visible);
			final Label genre = new Label();
			genre.setText(new String(programm.getGenre()));

			final Hyperlink title = new Hyperlink();
			title.setText(new String(programm.getFilm()) + " -- " + odd);

			if (programm.getThreeD()) {
				Image value = new Image(getClass().getResourceAsStream("/ch/ffhs/kino/images/3d.png"));
				final ImageView threeDImage = new ImageView(value);

				threeDImage.prefHeight(30);
				threeDImage.setFitHeight(30.0);
				threeDImage.prefWidth(30);
				threeDImage.setFitWidth(30);

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
	};
}
