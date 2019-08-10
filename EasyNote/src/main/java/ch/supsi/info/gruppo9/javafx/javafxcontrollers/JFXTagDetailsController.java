package ch.supsi.info.gruppo9.javafx.javafxcontrollers;

import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagAlreadyExistException;
import ch.supsi.info.gruppo9.exception.TagNotEliminableException;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.AppAlertDialog;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationTask;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationType;
import ch.supsi.info.gruppo9.javafx.javafxpresentation.GroupTagCell;
import ch.supsi.info.gruppo9.javafx.javafxpresentation.TagCell;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.services.GroupTagServices;
import ch.supsi.info.gruppo9.services.IGroupTagServices;
import ch.supsi.info.gruppo9.services.ITagServices;
import ch.supsi.info.gruppo9.services.TagServices;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class JFXTagDetailsController implements Initializable {

    private ITagServices tagServices = TagServices.getInstance();
    private IGroupTagServices groupTagServices = GroupTagServices.getInstance();

    private JFXNoteController noteController;

    private ObservableList<GroupTag> observableGroupTagList;
    private ObservableList<Tag> observableTagList;
    private JFXMainApplicationController mainController;

    @FXML
    private Button removeTagBtn;

    @FXML
    private ListView<GroupTag> groupTagsListView;

    @FXML
    private ListView<Tag> tagsListView;

    @FXML
    private Button removeTagGroupBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableGroupTagList = FXCollections.observableArrayList(groupTagServices.getGroupTags());
        observableTagList = FXCollections.observableArrayList(tagServices.getTags());
        sortGroupTagList();
        sortTagList();

        groupTagsListView.setItems(observableGroupTagList);
        tagsListView.setItems(observableTagList);

        tagsListView.setCellFactory(tagListView -> new TagCell());
        groupTagsListView.setCellFactory(groupTagListView -> new GroupTagCell());

        // disable remove tag/group tag button when nothing is selected
        removeTagGroupBtn.disableProperty().bind(Bindings.isEmpty(groupTagsListView.getSelectionModel().getSelectedItems()));
        removeTagBtn.disableProperty().bind(Bindings.isEmpty(tagsListView.getSelectionModel().getSelectedItems()));

    }

    @FXML
    void addTag() {
        NotificationTask notif = new NotificationTask(mainController.getNotificationLabel(), 3);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new tag");
        dialog.setContentText("Please enter a name for your tag:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(tagName -> {
            try {
                if (!tagName.isBlank()) {
                    tagServices.newTag(tagName);
                    notif.setNotificationMessage("Added new tag");
                    notif.setNotificationType(NotificationType.SUCCESS);
                    notif.show();
                }
            } catch (TagAlreadyExistException e) {
                notif.setNotificationMessage("The tag already exist, check out the tag list");
                notif.setNotificationType(NotificationType.FAILED);
                notif.show();
            } catch (PersistenceLayerException e) {
                e.printStackTrace();
            } finally {
                observableTagList.setAll(tagServices.getTags());
                sortTagList();
            }
        });
    }

    @FXML
    void addGroupTag() {
        Stage stage = new Stage();
        Parent addGroupTagView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/AddGroupTagView.fxml"));
            addGroupTagView = loader.load();
            JFXAddGroupTagController addGroupTagController = loader.getController();
            addGroupTagController.setTagDetailsController(this);
            stage.setTitle("Add group tag");
            stage.setScene(new Scene(addGroupTagView));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void removeTag() {
        Tag tag = tagsListView.getSelectionModel().getSelectedItem();
        Alert alert = new AppAlertDialog(Alert.AlertType.CONFIRMATION,
                "Remove a tag",
                "Sure you want to remove the tag " + tag.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK)) {
                NotificationTask notif = new NotificationTask(mainController.getNotificationLabel(), 3);
                try {
                    if (tagServices.deleteTag(tag, groupTagServices.getGroupTags())) {
                        noteController.removeTagFromNote(tag);
                        notif.setNotificationMessage("The tag has been deleted");
                        notif.setNotificationType(NotificationType.SUCCESS);
                        notif.show();
                    }
                } catch (TagNotEliminableException e) {
                    notif.setNotificationMessage("The tag Can't be deleted");
                    notif.setNotificationType(NotificationType.FAILED);
                    notif.show();
                } catch (PersistenceLayerException e) {
                    notif.setNotificationMessage("Error while deleting th tag");
                    notif.setNotificationType(NotificationType.FAILED);
                    notif.show();
                } finally {
                    observableTagList.setAll(tagServices.getTags());
                }
            } else {
                alert.close();
            }
        });

    }

    @FXML
    void removeGroupTag() {
        NotificationTask notif = new NotificationTask(mainController.getNotificationLabel(), 3);
        GroupTag groupTag = groupTagsListView.getSelectionModel().getSelectedItem();
        Alert alert = new AppAlertDialog(Alert.AlertType.CONFIRMATION,
                "Remove a group tag",
                "Sure you want to remove the group tag " + groupTag.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK)) {
                try {
                    groupTagServices.deleteGroupTag(groupTag);
                    notif.setNotificationMessage("The group tag has been deleted");
                    notif.setNotificationType(NotificationType.SUCCESS);
                    notif.show();
                } catch (PersistenceLayerException e) {
                    notif.setNotificationMessage("Error while deleting group tag");
                    notif.setNotificationType(NotificationType.FAILED);
                    notif.show();
                } finally {
                    observableGroupTagList.setAll(groupTagServices.getGroupTags());
                    sortGroupTagList();
                }
            } else {
                alert.close();
            }
        });
    }

    @FXML
    void addGroupTagToNote() {
        GroupTag groupTag = groupTagsListView.getSelectionModel().getSelectedItem();
        if (groupTag != null)
            noteController.addGroupTagToNote(groupTag);
    }

    @FXML
    void addTagToNote() {
        Tag tag = tagsListView.getSelectionModel().getSelectedItem();
        if (tag != null)
            noteController.addTagToNote(tag);
    }

    void setNoteController(JFXNoteController controller) {
        this.noteController = controller;
    }

    void setMainController(JFXMainApplicationController controller) {
        this.mainController = controller;
    }

    void addToTagList(Tag tag) {
        if (!observableTagList.contains(tag)) {
            observableTagList.add(tag);
        }
    }

    void addToGroupTagList(GroupTag groupTag) {
        if (!observableGroupTagList.contains(groupTag)) {
            observableGroupTagList.add(groupTag);
        }
    }

    void addToTagList(List<Tag> tags) {
        for (Tag tag : tags) {
            addToTagList(tag);
        }
    }

    private void sortTagList() {
        observableTagList.sort(Comparator.comparing(Tag::getName));
    }

    private void sortGroupTagList() {
        observableGroupTagList.sort(Comparator.comparing(GroupTag::getName));
    }


}