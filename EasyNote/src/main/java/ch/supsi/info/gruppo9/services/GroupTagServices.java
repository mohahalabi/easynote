package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.GroupTagAlreadyExistException;
import ch.supsi.info.gruppo9.exception.GroupTagFewTagsException;
import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagsNotLoadedException;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.persistence.IGroupTagPersistenceLayerService;
import ch.supsi.info.gruppo9.persistence.csv.FileCSVGroupTag;

import java.util.*;

public class GroupTagServices implements IGroupTagServices {
    private static GroupTagServices service;
    /* Group Tag Persistence Layer Service */
    private IGroupTagPersistenceLayerService groupTagPersistenceLayerService = FileCSVGroupTag.getInstance();
    /* collection that holds GroutTags */
    private static List<GroupTag> groupTags;

    public static GroupTagServices getInstance() {
        if (service == null)
            service = new GroupTagServices();
        return service;
    }

    private GroupTagServices() {
    }

    public void loadGroupTag(List<Tag> tags) throws PersistenceLayerException, TagsNotLoadedException {
        if (tags == null)
            throw new TagsNotLoadedException();
        List<GroupTag> loadGroupTags = groupTagPersistenceLayerService.load(tags);
        if (loadGroupTags != null)
            groupTags = new ArrayList<>(loadGroupTags);
        else
            throw new PersistenceLayerException();
    }

    public GroupTag newGroupTag(final String name, List<Tag> tags) throws PersistenceLayerException, GroupTagAlreadyExistException, GroupTagFewTagsException {
        if (!validateGroupTag(name, tags))
            return null;
        final GroupTag groupTag = groupTagPersistenceLayerService.saveAndGet(name, tags);
        if (groupTag!=null) {
            groupTags.add(groupTag);
            return groupTag;
        } else throw new PersistenceLayerException();
    }

    public void deleteGroupTag(final GroupTag groupTag) throws PersistenceLayerException {
        if (groupTagPersistenceLayerService.delete(groupTag))
            groupTags.remove(groupTag);
        else
            throw new PersistenceLayerException();
    }

    public List<GroupTag> getGroupTags() {
        return Collections.unmodifiableList(groupTags);
    }

    private boolean validateGroupTag(final String name, List<Tag> tags) throws GroupTagFewTagsException, GroupTagAlreadyExistException {
        if (tags == null || tags.size() < 2)
            throw new GroupTagFewTagsException();
        for (GroupTag groupTagL : groupTags) {
            if (listEquals(groupTagL.getTagList(), tags) || groupTagL.getName().equals(name))
                throw new GroupTagAlreadyExistException();
        }
        return true;
    }

    private boolean listEquals(List<Tag> list1, List<Tag> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

}
