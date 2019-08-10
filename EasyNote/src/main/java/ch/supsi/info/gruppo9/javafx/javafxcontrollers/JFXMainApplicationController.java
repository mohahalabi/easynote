package ch.supsi.info.gruppo9.javafx.javafxcontrollers;

import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.AppAlertDialog;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationTask;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.NotificationType;
import ch.supsi.info.gruppo9.javafx.javafxpresentation.NoteCell;
import ch.supsi.info.gruppo9.model.GroupTag;
import ch.supsi.info.gruppo9.model.Note;
import ch.supsi.info.gruppo9.model.Tag;
import ch.supsi.info.gruppo9.services.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class JFXMainApplicationController implements Initializable {

    private INoteServices noteServices = NoteServices.getInstance();
    private IRelationNoteTagServices relationServices = RelationNoteTagServices.getInstance();
    private ITagServices tagServices = TagServices.getInstance();
    private IGroupTagServices groupTagServices = GroupTagServices.getInstance();
    private IAuthorService authorService = AuthorService.getInstance();

    private ObservableList<Note> observableNotesList;
    private Parent centerNoteVBox;
    private JFXNoteController noteController;
    private JFXTagDetailsController tagController;
    private HBox rightTagDetailsHBox;

    @FXML
    private BorderPane root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Region topReducibleRegion;

    @FXML
    private ListView<Note> notesListView;

    @FXML
    private HBox leftHBox;

    @FXML
    private ImageView hideNoteListImageView;

    @FXML
    private TextField searchTextField;

    @FXML
    private VBox centerHomeVBox;

    @FXML
    private Label notificationLabel;

    @FXML
    private ToggleGroup searchGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        menuBar.useSystemMenuBarProperty().set(true);

        observableNotesList = FXCollections.observableArrayList();
        observableNotesList.setAll(noteServices.getNotes());
        sortObservableNoteList();
        notesListView.setItems(observableNotesList);

        try { // load the various views
            FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/gui/fxml/NoteView.fxml"));
            centerNoteVBox = centerLoader.load();
            FXMLLoader rightLoader = new FXMLLoader(getClass().getResource("/gui/fxml/TagDetailsView.fxml"));
            rightTagDetailsHBox = rightLoader.load();
            noteController = centerLoader.getController();
            tagController = rightLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        noteController.setMainController(this);
        tagController.setMainController(this);
        noteController.setTagController(tagController);
        tagController.setNoteController(noteController);


        notesListView.setCellFactory(lst -> new NoteCell());

        // Event that assign the clicked note to be opened in the note view
        notesListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            Note note = notesListView.getSelectionModel().getSelectedItem();
            int index = notesListView.getSelectionModel().getSelectedIndex();
            if (note != null) {
                noteController.setNote(note, index);
                root.setCenter(centerNoteVBox);
            }
        });

    }

    @FXML
    void addNewNote() {
        NotificationTask notif = new NotificationTask(notificationLabel, 3);
        try {
            Note note = noteServices.newNote(authorService.getAuthor());
            noteServices.updateNote(note, "");
            observableNotesList.add(0, note);
            notesListView.getSelectionModel().select(0);
            notif.setNotificationMessage("added new note");
            notif.setNotificationType(NotificationType.SUCCESS);
            notif.show();
        } catch (PersistenceLayerException e) {
            notif.setNotificationMessage("Error while adding new note");
            notif.setNotificationType(NotificationType.FAILED);
            notif.show();
        }
    }

    @FXML
    void closeNoteList() {
        if (root.getLeft() == null) {
            root.setLeft(leftHBox);
            topReducibleRegion.setPrefWidth(130); // normal width 130
            hideNoteListImageView.setImage(new Image(getClass().getResource("/gui/icons/hide_list_icon.png").toString()));
        } else {
            root.setLeft(null);
            topReducibleRegion.setPrefWidth(0);
            hideNoteListImageView.setImage(new Image(getClass().getResource("/gui/icons/show_list_icon.png").toString()));
        }
    }

    @FXML
    void showTagView() {
        if (root.getRight() == null)
            root.setRight(rightTagDetailsHBox);
        else
            root.setRight(null);
    }

    @FXML
    void searchNote() {
        String searchBy = ((RadioMenuItem) searchGroup.getSelectedToggle()).getText();
        setCenterHomeVBox();
        switch (searchBy) {

            case "By text":
                ObservableList<Note> result = FXCollections.observableArrayList(noteServices.searchByTextContent(searchTextField.getText(), noteServices.getNotes()));
                observableNotesList.setAll(result);
                sortObservableNoteList();
                break;
            case "By tag":
                Optional<Tag> tagOptional = tagServices.getTags().stream()
                        .filter(tag -> tag.getName().toLowerCase().equals(searchTextField.getText().toLowerCase()))
                        .findFirst();
                if (tagOptional.isPresent()) {
                    Tag tag = tagOptional.get();
                    if (!noteServices.searchByTag(tag, relationServices.getRelations()).isEmpty())
                        observableNotesList.setAll(noteServices.searchByTag(tag, relationServices.getRelations()));
                } else {
                    observableNotesList.setAll(noteServices.getNotes());
                }
                break;
            case "By group tag":
                Optional<GroupTag> groupTagOptional = groupTagServices.getGroupTags().stream()
                        .filter(groupTag -> groupTag.getName().toLowerCase().equals(searchTextField.getText().toLowerCase()))
                        .findFirst();
                if (groupTagOptional.isPresent()) {
                    GroupTag groupTag = groupTagOptional.get();
                    if (!noteServices.searchByGroupTag(groupTag.getTagList(), relationServices.getRelations()).isEmpty())
                        observableNotesList.setAll(noteServices.searchByGroupTag(groupTag.getTagList(), relationServices.getRelations()));
                } else {
                    observableNotesList.setAll(noteServices.getNotes());
                }
                break;
        }
    }

    @FXML
    void aboutTheApp() {
        ProjectInformationService projectInfo = ProjectInformationService.getInstance();
        StringBuilder aboutContent = new StringBuilder("Easy Note application.\n");
        try {
            projectInfo.loadProjectInfo();
            aboutContent.append("Version: ").append(projectInfo.getVersion()).append("\n");
            aboutContent.append("Group id: ").append(projectInfo.getGroupId()).append("\n");
            aboutContent.append("Artifact: ").append(projectInfo.getArtifact());
        } catch (IOException e) {
            aboutContent.append("Version: unknown.").append("\n");
            aboutContent.append("Group id: unknown.").append("\n");
            aboutContent.append("Artifact: unknown.");
        }
        Alert about = new AppAlertDialog(Alert.AlertType.INFORMATION, "About Easy Note", aboutContent.toString());
        about.setHeaderText("About the application");
        about.showAndWait();
    }

    @FXML
    void closeTheApp() {
        Alert exitAlert = new AppAlertDialog(Alert.AlertType.CONFIRMATION,
                "Exit", "Sure you want to exit?");
        exitAlert.setHeaderText("Confirmation");
        exitAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK))
                Platform.exit();
            else
                exitAlert.close();
        });
    }

    private void sortObservableNoteList() {
        observableNotesList.sort((o1, o2) -> o2.getModificationDate().compareTo(o1.getModificationDate()));
    }

    void clearNoteSelection() {
        notesListView.getSelectionModel().clearSelection();
    }

    void setCenterHomeVBox() {
        root.setCenter(centerHomeVBox);
    }

    void deleteNote(Note note) {
        observableNotesList.remove(note);
    }

    void updateNote(Note note, int index) {
        observableNotesList.set(index, note);
    }

    Label getNotificationLabel() {
        return notificationLabel;
    }

}