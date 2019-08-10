package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.model.Author;
import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;

import java.util.List;
import java.util.Map;

public interface INoteServices extends IApplicationService {
    /* function that loads Note from Persistence Layer and save into 'notes' collection */
    void loadNotes() throws PersistenceLayerException;

    /*  function that create a new empty note with its id, author, creation date time (equals to last modification date
        time). the new note is saved into the holder collection and on Persistence Layer
        return value: the new Note  */
    Note newNote(final Author author) throws PersistenceLayerException;

    /*  function that update a note with a new text, implicitly the last modification date time is updated too.
        modification are both saved to holder collection and Persistence Layer  */
    void updateNote(final Note note, final String text) throws PersistenceLayerException;

    /*  function that delete a note from the runtime collection holder and Persistence Layer. if notes has tag
        relations, these are even deleted   */
    boolean deleteNote(final Note note) throws PersistenceLayerException;

    /*  function that search notes by text content   */
    List<Note> searchByTextContent(final String testo, List<Note> notes);

    /*  function that search notes by group tag   */
    List<Note> searchByGroupTag(List<Tag> tags, Map<Note,List<Tag>> relations);

    /*  function that search notes by tag   */
    List<Note> searchByTag(Tag tag, Map<Note,List<Tag>> relations);

    /* function that return an unmodifiable list of notes holder collection */
    List<Note> getNotes();
}
