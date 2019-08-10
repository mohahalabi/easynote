package ch.supsi.info.gruppo9.javafx.javafxpresentation;

import ch.supsi.info.gruppo9.model.GroupTag;

public class GroupTagPresenter implements Presentable<GroupTag> {

    @Override
    public String present(GroupTag groupTag) {
        return groupTag.getName().toUpperCase() + " | " + groupTag.getTagList().size() + " tags";
    }
}
