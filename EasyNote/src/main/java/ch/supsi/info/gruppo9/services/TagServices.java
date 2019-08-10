package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagAlreadyExistException;
import ch.supsi.info.gruppo9.exception.TagNotEliminableException;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.persistence.ITagPersistenceLayerService;
import ch.supsi.info.gruppo9.persistence.csv.FileCSVTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagServices implements ITagServices {
    private static TagServices service;
    /* Tag Persistence Layer Service */
    private ITagPersistenceLayerService tagPersistenceLayerService = FileCSVTag.getInstance();
    /* service of the same layer needed for internal operation */
    private RelationNoteTagServices relationNoteTagServices = RelationNoteTagServices.getInstance();
    /* collection that holds Tags */
    private static List<Tag> tags;

    public static TagServices getInstance() {
        if (service == null)
            service = new TagServices();
        return service;
    }

    private TagServices() {
    }

    public void loadTags() throws PersistenceLayerException {
        List<Tag> loadTags = tagPersistenceLayerService.load();
        if (loadTags != null) {
            tags = new ArrayList<>(loadTags);
        } else {
            throw new PersistenceLayerException();
        }
    }

    public Tag newTag(final String name) throws TagAlreadyExistException, PersistenceLayerException {
        if (validateTag(name))
            throw new TagAlreadyExistException();
        final Tag tag = tagPersistenceLayerService.saveAndGet(name);
        if (tag != null) {
            tags.add(tag);
            return tag;
        }
        else
            throw new PersistenceLayerException();
    }

    public boolean deleteTag(final Tag tag, List<GroupTag> groupTags) throws TagNotEliminableException, PersistenceLayerException {
        for (GroupTag groupTag : groupTags) {
            if (groupTag.getTagList().contains(tag))
                throw new TagNotEliminableException();
        }
        if (tagPersistenceLayerService.delete(tag)) {
            tags.remove(tag);
            //removed also from the relationNoteTagService relation holder
            relationNoteTagServices.clean(tag);
            return true;
        } else
            throw new PersistenceLayerException();
    }

    public List<Tag> getTags() {
        if (tags != null)
            return Collections.unmodifiableList(tags);
        else
            return null;
    }

    private boolean validateTag(final String tagName) {
        if (tagName == null || tagName.equals(""))
            return false;
        for (Tag tag : tags) {
            if (tag.getName().equals(tagName))
                return true;
        }
        return false;
    }

}
