package org.itdhbw.futurewars.controller.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.util.FileHelper;
import org.itdhbw.futurewars.util.FileNotFoundExceptions;

import java.io.IOException;
import java.util.List;

public class MenuViewController {

    private static final Logger LOGGER = LogManager.getLogger(MenuViewController.class);
    @FXML
    private Button startButton;
    @FXML
    private Button mapEditorButton;
    @FXML
    private VBox mapButtonContainer;

    public void initialize() {
        LOGGER.info("Initializing menu view...");
        mapButtonContainer.setVisible(false);
        List<String> maps = Context.getMapLoader().getMapNames();
        for (String map : maps) {
            Button button = new Button(map);
            button.setOnAction(this::startGame);
            button.setUserData(map);
            mapButtonContainer.getChildren().add(button);
        }
    }

    @FXML
    private void showMapSelection(ActionEvent actionEvent) {
        LOGGER.info("Showing map selection...");
        LOGGER.info("Button amount: {}", mapButtonContainer.getChildren().size());
        mapButtonContainer.setVisible(!mapButtonContainer.isVisible());
    }

    private void startGame(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        String map = (String) source.getUserData();
        try {
            Context.getMapLoader().loadMap(map);
            Parent gameView = FXMLLoader.load(FileHelper.getFxmlFile("game-view.fxml").toURL());
            Scene scene = new Scene(gameView);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FileNotFoundExceptions e) {
            LOGGER.error("Error while starting game", e);
        }
    }

    @FXML
    private void startMapEditor(ActionEvent actionEvent) {
        Stage stage = Context.getPrimaryStage();
        try {
            Parent gameView = FXMLLoader.load(FileHelper.getFxmlFile("map-editor-view.fxml").toURL());
            Scene scene = new Scene(gameView);
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FileNotFoundExceptions e) {
            LOGGER.error("Error while opening map editor", e);
        }
    }

    @FXML
    private void openOptions(ActionEvent actionEvent) {
        LOGGER.info("Opening options...");
        Stage stage = Context.getPrimaryStage();
        Context.getGameState().setPreviousScene(stage.getScene());
        try {
            Parent gameView = FXMLLoader.load(FileHelper.getFxmlFile("options-view.fxml").toURL());
            Scene scene = new Scene(gameView);
            LOGGER.info("Setting previous scene...");
            LOGGER.info("Previous scene: {}", Context.getGameState().getPreviousScene());
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FileNotFoundExceptions e) {
            LOGGER.error("Error while opening settings", e);
        }
    }

    @FXML
    private void startUnitEditor(ActionEvent actionEvent) {
        // TODO document why this method is empty
    }

    @FXML
    private void startTileEditor(ActionEvent actionEvent) {
        // TODO document why this method is empty
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}
