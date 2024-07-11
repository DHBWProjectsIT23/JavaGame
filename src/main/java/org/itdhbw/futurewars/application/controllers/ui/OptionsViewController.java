package org.itdhbw.futurewars.application.controllers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.OptionsController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;

import java.net.MalformedURLException;
import java.net.URL;

public class OptionsViewController {
    private static final Logger LOGGER = LogManager.getLogger(OptionsViewController.class);
    private final OptionsController optionsController;
    Stage stage;

    @FXML
    private MenuButton viewModeButton;
    @FXML
    private MenuButton resolutionButton;
    private boolean initializedResolutions = false;
    @FXML
    private StackPane backgroundPane;

    public OptionsViewController() {
        this.optionsController = Context.getOptionsController();
    }

    @FXML
    public void initialize() {
        try {
            URL backgroundImage = FileHelper.getFile("$INTERNAL_DIR/assets/splashArtDualStrike.jpg").toURL();
            backgroundPane.setStyle("-fx-background-image: url('" + backgroundImage + "')");
        } catch (FailedToLoadFileException | MalformedURLException e) {
            ErrorHandler.addException(e, "Failed to load background image");
        }

        for (MenuItem item : viewModeButton.getItems()) {
            item.setOnAction(ignored -> {
                handleViewModeChange(item.getText());
                viewModeButton.setText(item.getText());
            });
        }

        String resolution = optionsController.getResolution();
        String viewMode = optionsController.getViewMode();
        viewModeButton.setText(viewMode);
        resolutionButton.setText(resolution);


        this.stage = Context.getPrimaryStage();
        resolutionButton.setOnShowing(ignored -> openResolutions());
        LOGGER.info("Previous scene from OptionsViewController: {}", Context.getGameState().getPreviousRoot());
    }

    private void populateResolutionMenu() {
        resolutionButton.getItems().clear();
        for (String resolution : optionsController.getResolutions()) {
            LOGGER.info("Adding resolution {}", resolution);
            MenuItem item = new MenuItem(resolution);
            resolutionButton.getItems().add(item);
            item.setOnAction(ignored -> {
                handleResolutionChange(item.getText());
                resolutionButton.setText(item.getText());
            });
        }
        initializedResolutions = true;
    }

    private void handleViewModeChange(String viewMode) {
        switch (viewMode) {
            case "Fullscreen":
                optionsController.setFullscreen();
                break;
            case "Windowed":
                optionsController.setWindowed();
                break;
            default:
                LOGGER.error("Unknown view mode: {}", viewMode);
        }
    }

    private void handleResolutionChange(String resolution) {
        optionsController.setResolution(resolution);
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
            stage.setFullScreen(true);
        }
    }


    @FXML
    private void goBack(ActionEvent ignored) {
        optionsController.saveSettings();
        stage.getScene().setRoot(Context.getGameState().getPreviousRoot());
    }

    private void openResolutions() {
        LOGGER.info("Opening resolutions menu");
        if (!initializedResolutions) {
            LOGGER.info("Populating resolutions menu");
            populateResolutionMenu();
        }
    }
}
