package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.GroupTagAlreadyExistException;
import ch.supsi.info.gruppo9.exception.GroupTagFewTagsException;
import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagsNotLoadedException;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;

import java.util.List;

public interface IGroupTagServices extends IApplicationService {
    /* function that loads GroupTag from Persistence Layer and save into 'groupTags' collection.
     * the tags list as a parameter is needed */
    void loadGroupTag(List<Tag> tags) throws PersistenceLayerException, TagsNotLoadedException;

    /*  function that create a new groupTag with its name and list of tags. the new groupTag is saved into the holder
        collection and on Persistence Layer. group tags must contains at leats two tag and have unique name.
        for this version, once its created the group tag is unmodifiable. it can only be deleted.
        return value: the new groupTag  */
    GroupTag newGroupTag(final String name, List<Tag> tags) throws PersistenceLayerException,
            GroupTagAlreadyExistException, GroupTagFewTagsException;

    /*  function that delete a note from the runtime collection holder and Persistence Layer. if notes has tag
        relations, these are even deleted   */
    void deleteGroupTag(final GroupTag groupTag) throws PersistenceLayerException;

    /* function that return an unmodifiable list of grouptag holder collection */
    List<GroupTag> getGroupTags();
}
