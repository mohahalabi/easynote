package ch.supsi.info.gruppo9.javafx.javafxpresentation;

import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;

public class GroupTagCell extends ListCell<GroupTag> {


    @Override
    protected void updateItem(GroupTag groupTag, boolean empty) {

        super.updateItem(groupTag, empty);
        if (empty) {
            setText(null);
        } else {
            final GroupTagPresenter presenter = new GroupTagPresenter();
            final Tooltip tooltip = new Tooltip();
            final StringBuilder builder = new StringBuilder();
            for (Tag tag : groupTag.getTagList()) {
                builder.append(tag.getName());
                builder.append("\n");
            }
            tooltip.setText(builder.toString());
            tooltip.setStyle("-fx-font-size: 14px");
            setTooltip(tooltip);
            setText(presenter.present(groupTag));

        }
    }
}
