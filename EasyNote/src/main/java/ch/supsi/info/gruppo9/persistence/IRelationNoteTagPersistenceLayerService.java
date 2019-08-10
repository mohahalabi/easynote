package ch.supsi.info.gruppo9.persistence;

import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;

import java.util.List;

public interface IRelationNoteTagPersistenceLayerService extends IPersistenceLayer {
    /*  function that load the relations between note and tag from persistence layer
        note by note. it needs the list of tag */
    List<Tag> loadByNote(final Note note, List<Tag> allTags);

    /*  function that persit a new note tag relation */
    boolean addTagToNote(final Note note, final Tag tag);

    /* function that delete from persistence a relation note tag */
    boolean removeTagFromNote(final Note note, final Tag tag);

    /*  function that clean the relation persistence layer when a note is deleted */
    boolean cleanNote(final Note note);

    /*  function that clean the relation persistence layer when a tag is deleted */
    boolean cleanTag(final Tag tag);
}
