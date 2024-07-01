package org.itdhbw.futurewars.application.controllers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.OptionsController;
import org.itdhbw.futurewars.application.models.Context;

public class OptionsViewController {
    private static final Logger LOGGER = LogManager.getLogger(OptionsViewController.class);
    private final OptionsController optionsController;
    Stage stage;
    @FXML
    private Button backButton;
    @FXML
    private MenuButton viewModeButton;
    @FXML
    private MenuButton resolutionButton;
    private boolean initializedResolutions = false;

    public OptionsViewController() {
        this.optionsController = Context.getOptionsController();
    }

    @FXML
    public void initialize() {
        for (MenuItem item : viewModeButton.getItems()) {
            item.setOnAction(event -> {
                handleViewModeChange(item.getText());
                viewModeButton.setText(item.getText());
            });
        }

        String resolution = optionsController.getResolution();
        String viewMode = optionsController.getViewMode();
        viewModeButton.setText(viewMode);
        resolutionButton.setText(resolution);


        this.stage = Context.getPrimaryStage();
        resolutionButton.setOnShowing(event -> openResolutions());
        LOGGER.info("Previous scene from OptionsViewController: {}", Context.getGameState().getPreviousScene());
    }

    private void populateResolutionMenu() {
        resolutionButton.getItems().clear();
        for (String resolution : optionsController.getResolutions()) {
            LOGGER.info("Adding resolution {}", resolution);
            MenuItem item = new MenuItem(resolution);
            resolutionButton.getItems().add(item);
            item.setOnAction(event -> {
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
            case "Borderless":
                //setBorderless();
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
    private void goBack(ActionEvent actionEvent) {
        optionsController.saveSettings();
        stage.setScene(Context.getGameState().getPreviousScene());
        Context.getOptionsController().loadSettings();
    }

    private void setBorderless() {
        /*stage.initStyle(StageStyle.UNDECORATED);
        Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());*/
    }

    private void setWindowed() {
        stage.setFullScreen(false);
    }

    private void openResolutions() {
        LOGGER.info("Opening resolutions menu");
        if (!initializedResolutions) {
            LOGGER.info("Populating resolutions menu");
            populateResolutionMenu();
        }
    }
}
