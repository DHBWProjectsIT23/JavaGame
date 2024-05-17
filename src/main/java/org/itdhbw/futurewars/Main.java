package org.itdhbw.futurewars;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.StartupController;
import org.itdhbw.futurewars.model.game.Context;

import java.io.IOException;

public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

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
            LOGGER.error("Error loading game data", e);
            System.exit(1);
        }
        StartupController.initializeStage(stage);

        LOGGER.info("Initialization complete!");
        LOGGER.info("Loaded a total of:");
        LOGGER.info("\t {} tiles", Context.getTileLoader().numberOfTileFiles());
        LOGGER.info("\t {} units", Context.getUnitLoader().numberOfUnitFiles());
        LOGGER.info("\t {} maps", Context.getMapLoader().numberOfMapFiles());


        stage.show();
    }

}
