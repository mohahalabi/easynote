package ch.supsi.info.gruppo9.javafx.javafxcontrollers;

import ch.supsi.info.gruppo9.exception.NoteAlreadyHasTagException;
import ch.supsi.info.gruppo9.exception.NoteDoesntHaveTagException;
import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagAlreadyExistException;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.AppAlertDialog;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationTask;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationType;
import ch.supsi.info.gruppo9.javafx.javafxpresentation.MetaDataPresenter;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.services.*;
import com.jfoenix.controls.JFXChipView;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class JFXNoteController implements Initializable {

    private INoteServices noteServices = NoteServices.getInstance();
    private ITagServices tagServices = TagServices.getInstance();
    private IRelationNoteTagServices relationServices = RelationNoteTagServices.getInstance();

    private Note note; // the note to be opened in the note view
    private int noteIndex; // it's index in the notes list

    private JFXMainApplicationController mainController;
    private JFXTagDetailsController tagController;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private Tooltip infoToolTip;

    @FXML
    private Button saveBtn;

    @FXML
    private JFXChipView<Tag> tagsChipView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                    tagController.addToTagList(tag);
                    if (!tagsChipView.getChips().contains(tag))
                        return tag;
                } catch (TagAlreadyExistException e) {
                    Tag tag = tagServices.getTags()
                            .stream()
                            .filter(t -> t.getName().equals(tagName)).findFirst().get();
                    tagController.addToTagList(tag);
                    if (!tagsChipView.getChips().contains(tag))
                        return tag;
                } catch (PersistenceLayerException e) {
                    return null;
                }
                return null;
            }
        });

        // if a change is detected, enable the save button, otherwise disable it
        tagsChipView.getChips().addListener((ListChangeListener<? super Tag>) change -> {
            if (tagsChipView.getChips().equals(relationServices.getTagFromNote(note))) {
                saveBtn.setDisable(true);
            } else {
                saveBtn.setDisable(false);
            }
        });
    }


    @FXML
    void saveNote() {
        NotificationTask notif = new NotificationTask(mainController.getNotificationLabel(), 3);
        List<Tag> tagsSaved = relationServices.getTagFromNote(note);
        List<Tag> tagsToBeSaved = tagsChipView.getChips();
        List<Tag> excludedTags = new ArrayList<>();
        for (Tag tag : tagsSaved) {
            if (!tagsToBeSaved.contains(tag))
                excludedTags.add(tag);
        }
        try {
            noteServices.updateNote(note, noteTextArea.getText());
            for (Tag tag : tagsToBeSaved) {
                if (!tagsSaved.contains(tag))
                    relationServices.addTagToNote(note, tag);
            }
            for (Tag tag : excludedTags) {
                relationServices.removeTagFromNote(note, tag);
            }
            mainController.updateNote(note, noteIndex);
            initNoteInfo();
            notif.setNotificationMessage("The note has been saved");
            notif.setNotificationType(NotificationType.SUCCESS);
            notif.show();
            saveBtn.setDisable(true);
        } catch (PersistenceLayerException e) {
            notif.setNotificationMessage("Error while saving the note");
            notif.setNotificationType(NotificationType.FAILED);
            notif.show();
        } catch (NoteAlreadyHasTagException e) {
            // Already has it, do nothing
        } catch (NoteDoesntHaveTagException e) {
            // The note hasn't it, so nothing to remove, do nothing
        } finally {
            initTagsChipView();
        }
    }

    @FXML
    void deleteNote() {
        NotificationTask notif = new NotificationTask(mainController.getNotificationLabel(), 3);
        Alert alert = new AppAlertDialog(Alert.AlertType.CONFIRMATION,
                "Delete the note",
                "Sure you want to delete the current note?");
        alert.setHeaderText("Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK)) {
                try {
                    if (noteServices.deleteNote(note)) {
                        mainController.deleteNote(note);
                        mainController.setCenterHomeVBox();
                        mainController.clearNoteSelection();
                        notif.setNotificationMessage("The note has been deleted");
                        notif.setNotificationType(NotificationType.SUCCESS);
                        notif.show();
                    }
                } catch (PersistenceLayerException e) {
                    notif.setNotificationMessage("Error while deleting the note");
                    notif.setNotificationType(NotificationType.FAILED);
                    notif.show();
                }
            } else {
                alert.close();
            }

        });
    }

    @FXML
    void noteTextChanged() { // if the note's text is changed, enable save button, otherwise disable it
        if (note.getText().equals(noteTextArea.getText())) {
            saveBtn.setDisable(true);
        } else {
            saveBtn.setDisable(false);
        }
    }

    // set the note received from the main view controller
    void setNote(Note note, int noteIndex) {
        this.note = note;
        this.noteIndex = noteIndex;
        saveBtn.setDisable(true);
        initNoteText(note.getText());
        initNoteInfo();
        initTagsChipView();
    }

    // initialize the meta data into the tooltip
    private void initNoteInfo() {
        MetaDataPresenter presenter = new MetaDataPresenter();
        infoToolTip.setText(presenter.present(note));
    }

    private void initNoteText(String text) {
        noteTextArea.setText(text);
    }

    private void initTagsChipView() {
        tagsChipView.getChips().clear();
        List<Tag> tags = relationServices.getTagFromNote(note);
        if (tags != null)
            tagsChipView.getChips().setAll(tags);
    }

    void setMainController(JFXMainApplicationController controller) {
        this.mainController = controller;
    }

    void setTagController(JFXTagDetailsController controller) {
        this.tagController = controller;
    }

    void addGroupTagToNote(GroupTag groupTag) {
        for (Tag tag : groupTag.getTagList()) {
            if (!tagsChipView.getChips().contains(tag))
                tagsChipView.getChips().add(tag);
        }
    }

    void addTagToNote(Tag tag) {
        if (!tagsChipView.getChips().contains(tag))
            tagsChipView.getChips().add(tag);
    }

    void removeTagFromNote(Tag tag) {
        tagsChipView.getChips().remove(tag);
    }
}
