package ch.supsi.info.gruppo9.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Note extends ModelObject {
    /* metadata */
    private final Metadata metadata;
    /* text context */
    private String text;

    public Note(final UUID id, final Author author, final LocalDateTime creationDateTime,
                final LocalDateTime modificationDateTime, final String text) {
        super(id);
        this.metadata = new Metadata(UUID.randomUUID(), author, creationDateTime, modificationDateTime);
        this.text = text;
    }

    public Author getAuthor() {
        return metadata.getAuthor();
    }

    public LocalDateTime getCreationDate() {
        return metadata.getCreationDateTime();
    }

    public LocalDateTime getModificationDate() {
        return metadata.getModificationDateTime();
    }

    public String getText() {
        return text;
    }

    public void updateModificationDaTeTime(final LocalDateTime dateTime) {
        metadata.updateModificationDaTeTime(dateTime);
    }

    public void updateText(final String newText) {
        text = newText;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + getId() +
                ", metadata=" + metadata +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(getId(), note.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}