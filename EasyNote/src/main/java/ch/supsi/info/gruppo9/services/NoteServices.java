package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.model.Author;
import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.persistence.INotePersistenceLayerService;
import ch.supsi.info.gruppo9.persistence.csv.FIleCSVNote;

import java.time.LocalDateTime;
import java.util.*;

public class NoteServices implements INoteServices {
    private static NoteServices service;
    /* Note Persistence Layer Service */
    private INotePersistenceLayerService notePersistenceLayerService = FIleCSVNote.getInstance();
    /* service of the same layer needed for internal operation */
    private RelationNoteTagServices relationNoteTagServices = RelationNoteTagServices.getInstance();
    /* collection that holds Notes */
    private static List<Note> notes;

    private NoteServices() {
    }

    public static NoteServices getInstance() {
        if (service == null)
            service = new NoteServices();
        return service;
    }

    public void loadNotes() throws PersistenceLayerException {
        List<Note> loadNotes = notePersistenceLayerService.load();
        if (loadNotes != null)
            notes = new ArrayList<>(loadNotes);
        else
            throw new PersistenceLayerException();
    }

    public Note newNote(final Author author) throws PersistenceLayerException {
        if (author == null)
            return null;
        final LocalDateTime dateTime = LocalDateTime.now();
        final Note note = notePersistenceLayerService.saveAndGet(author, dateTime);
        if (note != null) {
            notes.add(note);
            relationNoteTagServices.add(note);
            return note;
        }
        else
            throw new PersistenceLayerException();
    }

    public void updateNote(final Note note, final String text) throws PersistenceLayerException {
        final LocalDateTime dateTime = LocalDateTime.now();
        if (notePersistenceLayerService.updateNote(note, dateTime, text)) {
            note.updateModificationDaTeTime(dateTime);
            note.updateText(text);
        }
        else
            throw new PersistenceLayerException();
    }

    public boolean deleteNote(final Note note) throws PersistenceLayerException {
        if (notePersistenceLayerService.delete(note)) {
            notes.remove(note);
            //removed also from the relationNoteTagService relation holder
            relationNoteTagServices.clean(note);
            return true;
        }
        else
            throw new PersistenceLayerException();
    }

    public List<Note> searchByTextContent(final String testo, List<Note> notes) {
        List<Note> matchingNotes = new ArrayList<>();
        for (Note note : notes) {
            if (note.getText().toLowerCase().contains(testo.toLowerCase()))
                matchingNotes.add(note);
        }
        return matchingNotes;
    }

    public List<Note> searchByGroupTag(List<Tag> tags, Map<Note,List<Tag>> relations) {
        List<Note> matchingNotes = new ArrayList<>();
        for (Note note : relations.keySet()) {
            if (relations.get(note).containsAll(tags))
                matchingNotes.add(note);
        }
        return Collections.unmodifiableList(matchingNotes);
    }

    public List<Note> searchByTag(Tag tag, Map<Note,List<Tag>> relations) {
        List<Note> matchingNotes = new ArrayList<>();
        for (Note note : relations.keySet()) {
            if (relations.get(note).contains(tag))
                matchingNotes.add(note);
        }
        return Collections.unmodifiableList(matchingNotes);
    }

    public List<Note> getNotes() {
        if (notes != null)
            return Collections.unmodifiableList(notes);
        else
            return null;
    }

}