package ch.supsi.info.gruppo9.javafx.javafxcontrollers;

import ch.supsi.info.gruppo9.exception.GroupTagAlreadyExistException;
import ch.supsi.info.gruppo9.exception.GroupTagFewTagsException;
import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagAlreadyExistException;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationTask;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationType;
import ch.supsi.info.gruppo9.javafx.javafxpresentation.TagCell;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.services.GroupTagServices;
import ch.supsi.info.gruppo9.services.IGroupTagServices;
import ch.supsi.info.gruppo9.services.ITagServices;
import ch.supsi.info.gruppo9.services.TagServices;
import com.jfoenix.controls.JFXChipView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JFXAddGroupTagController implements Initializable {

    private ITagServices tagServices = TagServices.getInstance();
    private IGroupTagServices groupTagServices = GroupTagServices.getInstance();

    private JFXTagDetailsController tagDetailsController;

    @FXML
    private TextField nameTextfield;

    @FXML
    private JFXChipView<Tag> tagsChipView;

    @FXML
    private ListView<Tag> tagsListView;

    @FXML
    private Label notificationLabel;

    @FXML
    private Button cancelBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Tag> observableTagsList = FXCollections.observableList(tagServices.getTags());
        tagsListView.setItems(observableTagsList);

        tagsListView.setCellFactory(tagListView -> new TagCell());
        tagsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // convert string <--> Tag when the user write the tag in the tag area
        tagsChipView.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag.getName();
            }

            @Override
            public Tag fromString(String tagName) {
                try {
                    Tag tag = tagServices.newTag(tagName);
                    if (!tagsChipView.getChips().contains(tag))
                        return tag;
                } catch (TagAlreadyExistException e) {
                    Tag tag = tagServices.getTags().stream().filter(t -> t.getName().equals(tagName)).findFirst().get();
                    if (!tagsChipView.getChips().contains(tag))
                        return tag;
                } catch (PersistenceLayerException e) {
                    return null;
                }
                return null;
            }
        });
    }

    @FXML
    void Cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void addTagToTheGroup() {
        List<Tag> selectedTags = new ArrayList<>(tagsListView.getSelectionModel().getSelectedItems());
        if (!selectedTags.isEmpty()) {
            for (Tag tag : selectedTags) {
                if (!tagsChipView.getChips().contains(tag))
                    tagsChipView.getChips().add(tag);
            }
        }
    }

    @FXML
    void confirm() {
        NotificationTask notif;
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        if (isValidGroupTag()) {
            try {
                GroupTag groupTag = groupTagServices.newGroupTag(nameTextfield.getText(), tagsChipView.getChips());
                tagDetailsController.addToGroupTagList(groupTag);
                tagDetailsController.addToTagList(groupTag.getTagList());
                stage.close();
            } catch (PersistenceLayerException e) {
                e.printStackTrace();
            } catch (GroupTagAlreadyExistException e) {
                // Already exist, nothing to do
                stage.close();
            } catch (GroupTagFewTagsException e) {
                // do noting, isValidGroupTag() already handled this case
            }
        } else {
            notif = new NotificationTask(notificationLabel, 4);
            notif.setNotificationType(NotificationType.FAILED);
            notif.setNotificationMessage("Name can't be blank. Add at least 2 tags");
            notif.show();
        }

    }


    private boolean isValidGroupTag() {
        return !nameTextfield.getText().isBlank() && tagsChipView.getChips().size() >= 2;
    }

    void setTagDetailsController(JFXTagDetailsController controller) {
        this.tagDetailsController = controller;
    }
}
