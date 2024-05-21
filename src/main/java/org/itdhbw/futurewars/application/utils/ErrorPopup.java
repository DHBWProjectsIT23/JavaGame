package org.itdhbw.futurewars.application.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * The type Error popup.
 */
public class ErrorPopup {
    private ErrorPopup() {
        // private constructor to prevent instantiation
    }

    /**
     * Show error popup.
     *
     * @param message the message
     * @param e       the e
     */
    public static void showErrorPopup(String message, Throwable e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
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

    /**
     * Show recoverable error popup.
     *
     * @param message the message
     * @param e       the e
     */
    public static void showRecoverableErrorPopup(String message, Throwable e) {
        showRecoverableErrorPopup(message, e.getMessage());
    }

    /**
     * Show recoverable error popup.
     *
     * @param message the message
     * @param e       the e
     */
    public static void showRecoverableErrorPopup(String message, String e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText(message);
            alert.setContentText(e + "\nDo you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
            } else {
                Platform.exit();
            }
        });
    }


    /**
     * Show unrecoverable error popup.
     *
     * @param message the message
     * @param e       the e
     */
    public static void showUnrecoverableErrorPopup(String message, Throwable e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fatal Error");
            alert.setHeaderText(message);
            alert.setContentText(e.getMessage() +
                                 "\nApplication will close after this message.");
            alert.showAndWait();
            Platform.exit();
        });
    }
}
