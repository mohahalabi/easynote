package ch.supsi.info.gruppo9.persistence;

import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;

import java.util.List;

public interface IGroupTagPersistenceLayerService extends IPersistenceLayer {
    /* function that load group tags from persistence layer and return a collection. it needs the list of all tag */
    List<GroupTag> load(List<Tag> allTag);

    /* function that persist a new group tag and return that group tag */
    GroupTag saveAndGet(final String name, List<Tag> tags);

    /* function that delete a group tag from persistence layer */
    boolean delete(final GroupTag groupTag);
}
