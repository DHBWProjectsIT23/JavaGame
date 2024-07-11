package org.itdhbw.futurewars.application.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class ErrorPopup {
    private ErrorPopup() {
        // private constructor to prevent instantiation
    }

    public static void showErrorPopup(String message, Throwable e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An Error occurred!");
            alert.setHeaderText(message);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        });
    }

    public static void showErrorPopup(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(message);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

}
