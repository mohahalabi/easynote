package ch.supsi.info.gruppo9.javafx.javafxdialogs;

import javafx.scene.control.Alert;

public class AppAlertDialog extends Alert {


    public AppAlertDialog(AlertType alertType, String title, String contentText) {
        super(alertType);
        setTitle(title);
        setContentText(contentText);
    }
}
