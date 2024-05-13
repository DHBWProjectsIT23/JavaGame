package org.itdhbw.futurewars.controller.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;

import java.io.IOException;
import java.util.Objects;

public class MenuController {

    private static final Logger LOGGER = LogManager.getLogger(MenuController.class);
    @FXML
    private Button startButton;
    @FXML
    private Button mapEditorButton;

    public MenuController() {
    }

    @FXML
    private void startGame(ActionEvent actionEvent) {
        try {
            Parent gameView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/itdhbw/futurewars/game-view.fxml")));
            Scene scene = new Scene(gameView);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            Context.getOptionsController().loadSettings(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void startMapEditor(ActionEvent actionEvent) {
        try {
            Parent gameView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/itdhbw/futurewars/map-editor-view.fxml")));
            Scene scene = new Scene(gameView);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            Context.getOptionsController().loadSettings(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void openOptions(ActionEvent actionEvent) {
        try {
            Parent gameView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/itdhbw/futurewars/options-view.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(gameView);
            Context.getGameState().setPreviousScene(stage.getScene());
            stage.setScene(scene);
            Context.getOptionsController().loadSettings(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void startUnitEditor(ActionEvent actionEvent) {
    }

    @FXML
    private void startTileEditor(ActionEvent actionEvent) {
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
