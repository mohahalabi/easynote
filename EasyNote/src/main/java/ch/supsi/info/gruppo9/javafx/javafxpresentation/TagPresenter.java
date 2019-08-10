package ch.supsi.info.gruppo9.javafx.javafxpresentation;


import ch.supsi.info.gruppo9.model.Tag;

public class TagPresenter implements Presentable<Tag> {

    @Override
    public String present(Tag tag) {
        return tag.getName();
    }
}
