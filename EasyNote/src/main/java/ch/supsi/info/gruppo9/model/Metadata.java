package ch.supsi.info.gruppo9.model;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Metadata extends ModelObject {
    private final Author author;
    private final LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;

    Metadata(final UUID id, final Author author, final LocalDateTime creationDateTime,
             final LocalDateTime modificationDateTime) {
        super(id);
        this.author = author;
        this.creationDateTime = creationDateTime;
        this.modificationDateTime = modificationDateTime;
    }

    LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    LocalDateTime getModificationDateTime() {
        return modificationDateTime;
    }

    Author getAuthor() {
        return author;
    }

    void updateModificationDaTeTime(final LocalDateTime dateTime) {
        modificationDateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "author=" + author +
                ", creationDateTime=" + creationDateTime +
                ", modificationDateTime=" + modificationDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(author, metadata.author) &&
                Objects.equals(creationDateTime, metadata.creationDateTime) &&
                Objects.equals(modificationDateTime, metadata.modificationDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, creationDateTime, modificationDateTime);
    }

}