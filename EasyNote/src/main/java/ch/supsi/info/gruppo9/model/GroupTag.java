package ch.supsi.info.gruppo9.model;


import java.util.*;

public class GroupTag extends ModelObject {
    private final String name;
    private List<Tag> tagList;

    public GroupTag(final UUID id, final String name, List<Tag> tagList) {
        super(id);
        this.name = name;
        this.tagList = new ArrayList<>(tagList);
    }

    public String getName() {
        return name;
    }

    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tagList);
    }

    @Override
    public String toString() {
        return "GroupTag{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", tagList= " + getTagListName() +
                '}';
    }

    private String getTagListName() {
        StringBuilder sb = new StringBuilder();
        tagList.forEach(tag ->
                sb.append("'")
                        .append(tag.getName())
                        .append("' "));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupTag groupTag = (GroupTag) o;
        return Objects.equals(getId(), groupTag.getId()) &&
                Objects.equals(name, groupTag.name) &&
                Objects.equals(tagList, groupTag.tagList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name, tagList);
    }
}
