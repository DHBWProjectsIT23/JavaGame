package org.itdhbw.futurewars.application.utils;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.logging.Logger;

public class ConfirmPopup {
    private static final Logger LOGGER = Logger.getLogger(ConfirmPopup.class.getSimpleName());

    private ConfirmPopup() {
        // private constructor to prevent instantiation
    }

    public static void showWithRunnable(Pane parent, String header, String message, Runnable runnable) {
        LOGGER.info("Showing confirmation popup...");
        Platform.runLater(() -> {
            adjustBackground(parent, true, 0.5);
            VBox mainLayout = createMainLayout();
            HBox buttonLayout = createButtonLayout();

            Text headerText = new Text(header);
            headerText.getStyleClass().add("dialogue-header");
            Text messageText = new Text(message);
            messageText.getStyleClass().add("dialogue-message");

            Button okButton = new Button("Confirm");
            Button cancelButton = new Button("Cancel");
            cancelButton.getStyleClass().add("yellow-background");

            okButton.setOnAction(ignored -> {
                runnable.run();
                parent.getChildren().remove(mainLayout);
                adjustBackground(parent, false, 1);
                LOGGER.info("Confirmed action in popup");
            });

            cancelButton.setOnAction(ignored -> {
                parent.getChildren().remove(mainLayout);
                adjustBackground(parent, false, 1);
                LOGGER.info("Cancelled action in popup");
            });

            finalizePopup(parent, mainLayout, buttonLayout, headerText, messageText, okButton, cancelButton);
        });
    }

    private static void adjustBackground(Pane parent, boolean isDisabled, double opacity) {
        for (Node node : parent.getChildren()) {
            node.setDisable(isDisabled);
            node.setOpacity(opacity);
        }
    }

    private static VBox createMainLayout() {
        VBox mainLayout = new VBox();
        mainLayout.getStyleClass().add("dialogue-pane");
        mainLayout.setSpacing(10);
        mainLayout.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        return mainLayout;
    }

    private static HBox createButtonLayout() {
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.setAlignment(Pos.BOTTOM_LEFT);
        return buttonLayout;
    }

    private static void finalizePopup(Pane parent, VBox mainLayout, HBox buttonLayout, Text headerText, Text messageText, Button okButton, Button cancelButton) {
        buttonLayout.getChildren().addAll(okButton, cancelButton);
        mainLayout.getChildren().addAll(headerText, messageText, buttonLayout);
        StackPane.setAlignment(mainLayout, Pos.CENTER);
        if (parent instanceof AnchorPane) {
            StackPane alignmentPane = new StackPane();
            alignmentPane.getChildren().add(mainLayout);
            parent.getChildren().add(alignmentPane);
            AnchorPane.setTopAnchor(alignmentPane, 0.0);
            AnchorPane.setBottomAnchor(alignmentPane, 0.0);
            AnchorPane.setLeftAnchor(alignmentPane, 0.0);
            AnchorPane.setRightAnchor(alignmentPane, 0.0);
        } else {
            parent.getChildren().add(mainLayout);
        }
    }
}
