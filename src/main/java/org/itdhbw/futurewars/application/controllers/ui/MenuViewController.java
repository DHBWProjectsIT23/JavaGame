package org.itdhbw.futurewars.application.controllers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.SceneController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class MenuViewController {

    private static final Logger LOGGER =
            LogManager.getLogger(MenuViewController.class);
    @FXML
    private Button startButton;
    @FXML
    private Button mapEditorButton;
    @FXML
    private VBox mapButtonContainer;
    @FXML
    private AnchorPane backgroundPane;

    public void initialize() {
        LOGGER.info("Initializing menu view...");
        mapButtonContainer.setVisible(false);
        Set<String> maps = Context.getMapRepository().getMapNames();
        for (String map : maps) {
            Button button = new Button(map);
            button.setOnAction(this::startGame);
            button.setUserData(map);
            mapButtonContainer.getChildren().add(button);
        }
        try {
            URL backgroundImage = FileHelper.getFile(
                    "$INTERNAL_DIR/assets/splashArtDualStrike.jpg").toURL();
            backgroundPane.setStyle(
                    "-fx-background-image: url('" + backgroundImage + "')");
        } catch (FailedToLoadFileException | MalformedURLException e) {
            ErrorHandler.addException(e, "Failed to load background image");
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
            Context.getFileLoader().loadMap(map);
        } catch (FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to load map: " + map);
        }

        try {
            SceneController.loadScene("game-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to load game view");
            ErrorHandler.showErrorPopup();
        }
    }

    @FXML
    private void startMapEditor(ActionEvent actionEvent) {
        try {
            SceneController.loadScene("map-editor-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to open map editor");
            ErrorHandler.showErrorPopup();
        }
    }

    @FXML
    private void openOptions(ActionEvent actionEvent) {
        try {
            SceneController.loadScene("options-view.fxml");
        } catch (FailedToLoadSceneException e) {
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
        try {
            SceneController.loadScene("error-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to open errors view");
            ErrorHandler.showErrorPopup();
        }
    }
}
