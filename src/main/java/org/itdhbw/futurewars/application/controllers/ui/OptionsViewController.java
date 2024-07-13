package org.itdhbw.futurewars.application.controllers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.itdhbw.futurewars.application.controllers.other.OptionsController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;

import java.util.logging.Logger;

public class OptionsViewController {
    private static final Logger LOGGER = Logger.getLogger(OptionsViewController.class.getSimpleName());
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
            Image backgroundImage = FileHelper.getMiscTexture(FileHelper.MiscTextures.SPLASH_ART);
            backgroundPane.setStyle("-fx-background-image: url('" + backgroundImage.getUrl() + "')");
        } catch (FailedToLoadTextureException e) {
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
    }

    private void populateResolutionMenu() {
        resolutionButton.getItems().clear();
        for (String resolution : optionsController.getResolutions()) {
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
                LOGGER.severe("Invalid view mode: " + viewMode);
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

    @Override
    public String toString() {
        return "OptionsViewController{" + "optionsController=" + optionsController + ", stage=" + stage +
               ", viewModeButton=" + viewModeButton + ", resolutionButton=" + resolutionButton +
               ", initializedResolutions=" + initializedResolutions + ", backgroundPane=" + backgroundPane + '}';
    }
}
