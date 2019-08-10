package ch.supsi.info.gruppo9.javafx.javafxdialogs;

import javafx.concurrent.Task;
import javafx.scene.control.Label;


public class NotificationTask extends Task<Void> {

    private final Label label;
    private final int duration;

    public NotificationTask(Label label, int duration) {
        this.label = label;
        this.duration = duration;
    }

    public void setNotificationMessage(String message) {
        label.setText(message);
    }

    @Override
    protected Void call() throws Exception {
        label.setVisible(true);
        Thread.sleep(duration * 1000);
        label.setVisible(false);
        return null;
    }

    public void setNotificationType(NotificationType type) {
        String style = "-fx-border-color:" + type.getColor() + ";" + "-fx-text-fill:" + type.getColor() + ";";
        label.setStyle(style);
    }

    public void show() {
        new Thread(this).start();
    }
}
