package ch.supsi.info.gruppo9.model;

import java.util.UUID;

public abstract class ModelObject {
    /* immutable universally unique identifier */
    private final UUID id;

    ModelObject(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

}
