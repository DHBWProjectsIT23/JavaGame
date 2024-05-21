package org.itdhbw.futurewars;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.StartupController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * The type Main.
 */
public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        launch();
    }

    @Override
    public void start(final Stage stage) throws IOException {

        StartupController.initializeUserDirectory();
        StartupController.initializeContext(stage);
        try {
            StartupController.loadTiles();
            StartupController.loadUnits();
            StartupController.retrieveMaps();
        } catch (Exception e) {
            ErrorHandler.addException(e, "Error loading game data");
            Platform.exit();
        }
        StartupController.initializeStage(stage);
        ErrorHandler.showErrorPopup();

        LOGGER.info("Initialization complete!");
        LOGGER.info("Loaded a total of:");
        LOGGER.info("\t {} tiles", Context.getTileLoader().numberOfTileFiles());
        LOGGER.info("\t {} units", Context.getUnitLoader().numberOfUnitFiles());
        LOGGER.info("\t {} maps", Context.getMapLoader().numberOfMapFiles());

        stage.show();
    }

}
