package org.itdhbw.futurewars.application.controllers.ui;

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
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;

import java.io.IOException;
import java.util.List;

/**
 * The type Menu view controller.
 */
public class MenuViewController {

    private static final Logger LOGGER =
            LogManager.getLogger(MenuViewController.class);
    @FXML
    private Button startButton;
    @FXML
    private Button mapEditorButton;
    @FXML
    private VBox mapButtonContainer;

    /**
     * Initialize.
     */
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
        LOGGER.info("Button amount: {}",
                    mapButtonContainer.getChildren().size());
        mapButtonContainer.setVisible(!mapButtonContainer.isVisible());
    }

    private void startGame(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        String map = (String) source.getUserData();
        try {
            Context.getMapLoader().loadMap(map);
            Parent gameView = FXMLLoader.load(
                    FileHelper.getFxmlFile("game-view.fxml").toURL());
            Scene scene = new Scene(gameView);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene()
                                                                  .getWindow();
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to start game");
        }
    }

    @FXML
    private void startMapEditor(ActionEvent actionEvent) {
        Stage stage = Context.getPrimaryStage();
        try {
            Parent gameView = FXMLLoader.load(
                    FileHelper.getFxmlFile("map-editor-view.fxml").toURL());
            Scene scene = new Scene(gameView);
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to open map editor");
            ErrorHandler.showErrorPopup();
        }
    }

    @FXML
    private void openOptions(ActionEvent actionEvent) {
        LOGGER.info("Opening options...");
        Stage stage = Context.getPrimaryStage();
        Context.getGameState().setPreviousScene(stage.getScene());
        try {
            Parent gameView = FXMLLoader.load(
                    FileHelper.getFxmlFile("options-view.fxml").toURL());
            Scene scene = new Scene(gameView);
            LOGGER.info("Setting previous scene...");
            LOGGER.info("Previous scene: {}",
                        Context.getGameState().getPreviousScene());
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to open options");
            ErrorHandler.showErrorPopup();
        }
    }

    @FXML
    private void startUnitEditor(ActionEvent actionEvent) {
        // here the unit editor will start
    }

    @FXML
    private void startTileEditor(ActionEvent actionEvent) {
        // here the tile editor will start
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        Stage stage =
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openErrorsView(ActionEvent actionEvent) {
        LOGGER.info("Opening Error View...");
        Stage stage = Context.getPrimaryStage();
        Context.getGameState().setPreviousScene(stage.getScene());
        try {
            Parent gameView = FXMLLoader.load(
                    FileHelper.getFxmlFile("error-view.fxml").toURL());
            Scene scene = new Scene(gameView);
            LOGGER.info("Setting previous scene...");
            LOGGER.info("Previous scene: {}",
                        Context.getGameState().getPreviousScene());
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to open error view");
            ErrorHandler.showErrorPopup();
        }
    }
}
