package ch.supsi.info.gruppo9.javafx.javafxpresentation;

import ch.supsi.info.gruppo9.model.Note;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MetaDataPresenter implements Presentable<Note> {


    private String formatDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm");
        return localDateTime.format(formatter);
    }

    @Override
    public String present(final Note note) {
        return "Created by: " + note.getAuthor().getName()
                + " on: " + formatDate(note.getCreationDate())
                + ". Last modification on: " + formatDate(note.getModificationDate());
    }


}
