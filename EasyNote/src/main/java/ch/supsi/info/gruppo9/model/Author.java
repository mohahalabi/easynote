package ch.supsi.info.gruppo9.model;

import java.util.Objects;
import java.util.UUID;

public class Author extends ModelObject {
    private final String name;

    public Author(final String name) {
        /* UUID not necessary for this application version */
        super(null);
        this.name = name;
    }

    public Author(final UUID id, final String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return name.equals(author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
