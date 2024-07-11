package org.itdhbw.futurewars.application.controllers.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.SceneController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ConfirmPopup;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class MenuViewController {

    private static final Logger LOGGER = LogManager.getLogger(MenuViewController.class);

    @FXML
    private VBox mapButtonContainer;
    @FXML
    private AnchorPane backgroundPane;
    @FXML
    private Text loadingText;
    @FXML
    private StackPane parentPane;

    public void initialize() {
        LOGGER.info("Initializing menu view...");
        mapButtonContainer.setVisible(false);
        Set<String> maps = Context.getMapRepository().getMapNames();
        for (String map : maps) {
            Button button = new Button(map.split("\\.")[0].replace("_", " "));
            button.setOnAction(this::startGame);
            button.setUserData(map);
            button.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(button, Priority.ALWAYS);
            mapButtonContainer.getChildren().add(button);
        }
        try {
            URL backgroundImage = FileHelper.getFile("$INTERNAL_DIR/assets/splashArtDualStrike.jpg").toURL();
            backgroundPane.setStyle("-fx-background-image: url('" + backgroundImage + "')");
        } catch (FailedToLoadFileException | MalformedURLException e) {
            ErrorHandler.addException(e, "Failed to load background image");
        }
    }

    private void startGame(ActionEvent actionEvent) {
        this.loadingText.setVisible(true);
        Thread thread = new Thread(() -> loadGameScene(actionEvent));
        thread.start();
    }

    private void loadGameScene(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        String map = (String) source.getUserData();


        try {
            Context.getFileLoader().loadMap(map);
        } catch (FailedToLoadFileException e) {
            ErrorHandler.addVerboseException(e, "Failed to load map: " + map);
        }

        Platform.runLater(() -> {
        try {
            SceneController.loadScene("game-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addVerboseException(e, "Failed to load game view");
        } finally {
            this.loadingText.setVisible(false);
        }
        });
    }

    @FXML
    private void showMapSelection(ActionEvent ignored) {
        LOGGER.info("Showing map selection...");
        LOGGER.info("Button amount: {}", mapButtonContainer.getChildren().size());
        mapButtonContainer.setVisible(!mapButtonContainer.isVisible());
    }

    @FXML
    private void startMapEditor(ActionEvent ignored) {
        try {
            SceneController.loadScene("map-editor-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addVerboseException(e, "Failed to open map editor");
        }
    }

    @FXML
    private void openOptions(ActionEvent ignored) {
        try {
            SceneController.loadScene("options-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addVerboseException(e, "Failed to open options");
        }
    }

    @FXML
    private void exitApplication(ActionEvent ignored) {
        ConfirmPopup.showWithRunnable(parentPane, "Are you sure you want to exit?", "", Platform::exit);
    }

    @FXML
    private void openErrorsView(ActionEvent ignored) {
        try {
            SceneController.loadScene("error-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addVerboseException(e, "Failed to open errors view");
        }
    }
}
