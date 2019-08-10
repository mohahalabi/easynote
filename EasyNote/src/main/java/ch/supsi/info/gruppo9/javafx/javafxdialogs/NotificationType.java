package ch.supsi.info.gruppo9.javafx.javafxdialogs;

public enum NotificationType {

    SUCCESS("#00C851"), FAILED("#ff4444");

    private final String color;

    NotificationType(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
