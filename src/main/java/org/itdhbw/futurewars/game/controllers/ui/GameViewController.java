package org.itdhbw.futurewars.game.controllers.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.SceneController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;
import org.itdhbw.futurewars.game.models.game_state.GameState;

import java.net.MalformedURLException;
import java.net.URL;

public class GameViewController {

    private static final Logger LOGGER =
            LogManager.getLogger(GameViewController.class);
    private final GameState gameState;
    @FXML
    private StackPane escapeMenu;
    @FXML
    private AnchorPane backgroundPane;
    @FXML
    private AnchorPane gamePane;
    @FXML
    private Label currentTurnLabel;
    @FXML
    private Label currentPlayerLabel;

    public GameViewController() {
        this.gameState = Context.getGameState();
    }

    public void initialize() {
        Platform.runLater(() -> {
            Scene scene = this.backgroundPane.getScene();
            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    escapeMenu.setVisible(!escapeMenu.isVisible());
                }
            });
        });

        try {
            URL backgroundImage = FileHelper.getFile(
                    "$INTERNAL_DIR/assets/advanceWarsSplash.jpg").toURL();
            backgroundPane.setStyle(
                    "-fx-background-image: url('" + backgroundImage + "')");
        } catch (FailedToLoadFileException | MalformedURLException e) {
            ErrorHandler.addException(e, "Failed to load background image");
        }

        gameState.currentPlayerProperty().addListener((_, _, newValue) -> {
            if (newValue.equals(1)) {
                LOGGER.info("Player 1's turn");
                gamePane.getStyleClass().remove("red-border");
                gamePane.getStyleClass().add("blue-border");

                currentPlayerLabel.setText("Player 1");
                currentTurnLabel.getStyleClass().add("blue-bg");
                currentTurnLabel.getStyleClass().remove("red-bg");
            } else {
                LOGGER.info("Player 2's turn");
                gamePane.getStyleClass().remove("blue-border");
                gamePane.getStyleClass().add("red-border");

                currentPlayerLabel.setText("Player 2");
                currentTurnLabel.getStyleClass().add("red-bg");
                currentTurnLabel.getStyleClass().remove("blue-bg");
            }
        });

        gameState.currentDayProperty().addListener((_, _, newValue) -> {
            currentTurnLabel.setText("Day " + newValue);
        });

    }

    @FXML
    private void openSettings(ActionEvent actionEvent) {
        try {
            SceneController.loadScene("options-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to load settings view");
            ErrorHandler.showErrorPopup();
        }
    }

    @FXML
    private void quitToMenu(ActionEvent actionEvent) {
        try {
            SceneController.loadScene("menu-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to load menu view");
            ErrorHandler.showErrorPopup();
        }
    }

    @FXML
    private void quitToDesktop(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    private void endTurn(ActionEvent actionEvent) {

        Context.getGameState().endTurn();
    }
}
