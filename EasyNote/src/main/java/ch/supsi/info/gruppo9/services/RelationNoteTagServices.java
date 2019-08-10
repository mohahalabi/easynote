package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.*;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.persistence.IRelationNoteTagPersistenceLayerService;
import ch.supsi.info.gruppo9.persistence.csv.FileCSVRelationNoteTag;

import java.util.*;

public class RelationNoteTagServices implements IRelationNoteTagServices {
    private static RelationNoteTagServices service;
    /* RelationNoteTag Persistence Layer Service */
    private IRelationNoteTagPersistenceLayerService relationNoteTagPersistenceLayerService =
            FileCSVRelationNoteTag.getInstance();
    /* collection that holds Relations */
    private static Map<Note, List<Tag>> relations;

    public static RelationNoteTagServices getInstance() {
        if (service == null)
            service = new RelationNoteTagServices();
        return service;
    }

    private RelationNoteTagServices() {
    }

    public void loadRelations(List<Note> notes, List<Tag> tags) throws PersistenceLayerException,
            NotesNotLoadedException,
            TagsNotLoadedException {
        if (notes == null)
            throw new NotesNotLoadedException();
        if (tags == null)
            throw new TagsNotLoadedException();
        Map<Note, List<Tag>> loadRelations = new HashMap<>();
        for (final Note note : notes) {
            List<Tag> noteTag = relationNoteTagPersistenceLayerService.loadByNote(note, tags);
            if (noteTag != null)
                loadRelations.put(note, noteTag);
            else
                throw new PersistenceLayerException();
        }
        relations = new HashMap<>(loadRelations);
    }

    public void addTagToNote(final Note note, final Tag tag) throws NoteAlreadyHasTagException,
            PersistenceLayerException {
        List<Tag> tags;
        if (!getRelations().containsKey(note))
            add(note);
        tags = getRelations().get(note);
        if (!tags.contains(tag)) {
            if (relationNoteTagPersistenceLayerService.addTagToNote(note, tag))
                tags.add(tag);
            else
                throw new PersistenceLayerException();
        } else
            throw new NoteAlreadyHasTagException();
    }

    public void addGroupTagToNote(final Note note, final GroupTag groupTag) throws PersistenceLayerException {
        for (Tag tag : groupTag.getTagList()) {
            try {
                addTagToNote(note, tag);
            } catch (NoteAlreadyHasTagException ignored) {
            }
        }
    }

    public void removeTagFromNote(final Note note, final Tag tag) throws NoteDoesntHaveTagException, PersistenceLayerException {
        List<Tag> tags = getRelations().get(note);
        if (tags.contains(tag)) {
            if (relationNoteTagPersistenceLayerService.removeTagFromNote(note, tag))
                tags.remove(tag);
            else
                throw new PersistenceLayerException();
        } else
            throw new NoteDoesntHaveTagException();
    }

    public Map<Note, List<Tag>> getRelations() {
        return Collections.unmodifiableMap(relations);
    }

    public List<Tag> getTagFromNote(final Note note) {
        return Collections.unmodifiableList(relations.get(note));
    }

    /* these 3 methods are package-private because of its encapsulation uses
     * usage:   NoteService, TagService
     * use:     for consistency reason of runtime data holder (create and drop cascade relations)  */

    void add(final Note note) {
        relations.put(note, new ArrayList<>());
    }

    void clean(final Note note) {
        relations.remove(note);
    }

    void clean(final Tag tag) {
        for (Note note : relations.keySet())
            relations.get(note).remove(tag);
    }
}
