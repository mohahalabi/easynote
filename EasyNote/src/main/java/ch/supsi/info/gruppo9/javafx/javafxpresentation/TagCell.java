package ch.supsi.info.gruppo9.javafx.javafxpresentation;

import ch.supsi.info.gruppo9.model.Tag;
import javafx.scene.control.ListCell;

public class TagCell extends ListCell<Tag> {


    @Override
    protected void updateItem(Tag tag, boolean empty) {
        super.updateItem(tag, empty);

        if (empty) {
            setText(null);
        } else {
            TagPresenter presenter = new TagPresenter();
            setText(presenter.present(tag));
        }
    }
}
