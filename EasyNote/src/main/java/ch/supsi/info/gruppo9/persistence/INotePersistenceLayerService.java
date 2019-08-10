package ch.supsi.info.gruppo9.persistence;

import ch.supsi.info.gruppo9.model.Author;
import ch.supsi.info.gruppo9.model.Note;

import java.time.LocalDateTime;
import java.util.List;

public interface INotePersistenceLayerService extends IPersistenceLayer {
    /* function that load notes from persistence layer and return a collection */
    List<Note> load();

    /* function that persist a new note and return that note */
    Note saveAndGet(final Author author, final LocalDateTime dateTime);

    /* function that persist the update of a note (modification date time and text) */
    boolean updateNote(final Note note, final LocalDateTime dateTime, final String text);

    /* function that delete a note from persistence layer */
    boolean delete(final Note note);
}
