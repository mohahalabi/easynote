package ch.supsi.info.gruppo9.javafx.javafxpresentation;

import ch.supsi.info.gruppo9.model.Note;


public class NoteTitlePresenter implements Presentable<Note> {


    @Override
    public String present(Note note) {
        String[] rows = note.getText().split("\n");
        return rows[0];
    }
}
