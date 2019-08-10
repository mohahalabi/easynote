package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagAlreadyExistException;
import ch.supsi.info.gruppo9.exception.TagNotEliminableException;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;

import java.util.List;

public interface ITagServices extends IApplicationService {
    /* function that loads tag from Persistence Layer and save into 'tag' collection */
    void loadTags() throws PersistenceLayerException;

    /*  function that create a new tag with its name. the new tag is saved into the holder collection and on
        Persistence Layer
        return value: the new tag  */
    Tag newTag(final String name) throws TagAlreadyExistException, PersistenceLayerException;

    /*  function that delete a tag from the runtime collection holder and Persistence Layer. if there is a
     *  group tag that contains that tag, the tag is not deleted. it needs the list of group tag */
    boolean deleteTag(final Tag tag, List<GroupTag> groupTags) throws TagNotEliminableException, PersistenceLayerException;

    /* function that returns an unmodifiable list of tags holder collection */
    List<Tag> getTags();
}
