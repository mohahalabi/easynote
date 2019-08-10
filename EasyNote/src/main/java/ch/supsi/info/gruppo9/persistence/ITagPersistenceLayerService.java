package ch.supsi.info.gruppo9.persistence;

import ch.supsi.info.gruppo9.model.Tag;

import java.util.List;

public interface ITagPersistenceLayerService extends IPersistenceLayer {
    /* function that load tags from persistence layer and return a collection */
    List<Tag> load();

    /* function that persist a new tag and return that tag */
    Tag saveAndGet(final String name);

    /* function that delete a tag from persistence layer */
    boolean delete(final Tag tag);
}
