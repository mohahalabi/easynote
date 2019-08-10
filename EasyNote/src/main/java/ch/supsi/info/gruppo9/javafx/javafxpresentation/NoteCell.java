package ch.supsi.info.gruppo9.javafx.javafxpresentation;

import ch.supsi.info.gruppo9.model.Note;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NoteCell extends ListCell<Note> {

    private final int CELL_WIDTH = 230;
    private final int CELL_HEIGHT = 85;

    @Override
    protected void updateItem(Note note, boolean empty) {
        super.updateItem(note, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setPrefSize(CELL_WIDTH, CELL_HEIGHT);
            setGraphic(getCellLayoutVBox(note));
        }

    }

    private String presentCreationDate(LocalDateTime dateTime) {
        if (dateTime.toLocalDate().equals(LocalDate.now())) {
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

    }

    private VBox getCellLayoutVBox(Note note) {

        VBox cellVBox = new VBox();
        cellVBox.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
        cellVBox.setSpacing(15);
        cellVBox.setAlignment(Pos.BOTTOM_LEFT);

        Label titleLbl = new Label();
        NoteTitlePresenter presenter = new NoteTitlePresenter();
        String title = presenter.present(note);
        if (title.length() > 25)
            titleLbl.setText(title.substring(0, 22) + "...");
        else
            titleLbl.setText(title);
        titleLbl.setStyle("-fx-font-size: 18;");

        Label creationLbl = new Label(presentCreationDate(note.getCreationDate()));
        creationLbl.setStyle("-fx-font-size: 12;");
        Separator hSeparator = new Separator();
        hSeparator.setPrefHeight(0.2);

        cellVBox.getChildren().addAll(titleLbl, creationLbl, hSeparator);
        VBox.setMargin(hSeparator, new Insets(0, 0, -5, 0));
        return cellVBox;
    }

}

