package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.*;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;

import java.util.List;
import java.util.Map;

public interface IRelationNoteTagServices extends IApplicationService {
    /* function that loads note tag relations from Persistence Layer and save into relation holder collection.
     * it needs list of note and tags ad parameter */
    void loadRelations(List<Note> notes, List<Tag> tags) throws PersistenceLayerException, NotesNotLoadedException,
            TagsNotLoadedException;

    /* function that add a tag to a note and persist the relation */
    void addTagToNote(final Note note, final Tag tag) throws NoteAlreadyHasTagException,
            PersistenceLayerException;

    /* function that add a group tag to a note and persist the relation as a multiple call of addTagToNote with
     * single tag associated to the group tag */
    void addGroupTagToNote(final Note note, final GroupTag groupTag) throws PersistenceLayerException;

    /* function that remove and persist the relation between a note and a tag */
    void removeTagFromNote(final Note note, final Tag tag) throws NoteDoesntHaveTagException, PersistenceLayerException;

    /* function that return an unmodifiable list of relations holder collection */
    Map<Note, List<Tag>> getRelations();

    /* function that return an unmodifiable list of tag associated to a note (parameter) */
    List<Tag> getTagFromNote(final Note note);
}
