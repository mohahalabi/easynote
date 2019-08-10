package ch.supsi.info.gruppo9;

import ch.supsi.info.gruppo9.exception.NotesNotLoadedException;
import ch.supsi.info.gruppo9.exception.PersistenceLayerException;
import ch.supsi.info.gruppo9.exception.TagsNotLoadedException;
import ch.supsi.info.gruppo9.javafx.javafxdialogs.AppAlertDialog;
import ch.supsi.info.gruppo9.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

class NewMain {

    public static void main(String[] args) {
        NoteApplication.main(args);
    }

}

public class NoteApplication extends Application {

    public static void main(String[] args) {
        if (loadFromServices()) {
            launch(args);
        } else {
            Alert alert = new AppAlertDialog(Alert.AlertType.ERROR,
                    "Error",
                    "Can't load services");
            alert.setHeaderText("Application launch Error");
            alert.show();
        }
    }

    private static boolean loadFromServices() {
        INoteServices noteServices = NoteServices.getInstance();
        ITagServices tagServices = TagServices.getInstance();
        IGroupTagServices groupTagServices = GroupTagServices.getInstance();
        IRelationNoteTagServices relationServices = RelationNoteTagServices.getInstance();
        try {
            noteServices.loadNotes();
            tagServices.loadTags();
            groupTagServices.loadGroupTag(tagServices.getTags());
            relationServices.loadRelations(noteServices.getNotes(), tagServices.getTags());
            return true;
        } catch (PersistenceLayerException | TagsNotLoadedException | NotesNotLoadedException e) {
            return false;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = FXMLLoader.load(getClass().getResource("/gui/fxml/MainApplicationView.fxml"));
        stage.setTitle("Easy Note");
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/gui/icons/app-icon.png")));
        stage.setResizable(false);
        stage.show();

    }

}
