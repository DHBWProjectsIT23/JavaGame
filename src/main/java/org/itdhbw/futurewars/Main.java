package org.itdhbw.futurewars;

import javafx.application.Application;
import javafx.stage.Stage;
import org.itdhbw.futurewars.application.controllers.other.StartupController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getSimpleName());

    public static void main(final String[] args) {
        launch();
    }

    @Override
    public void start(final Stage stage) throws IOException {
        LOGGER.info("Starting application...");

        LOGGER.info("Initializing application...");
        StartupController.loadFonts();
        StartupController.initializeUserDirectory();
        StartupController.initializeContext(stage);
        StartupController.loadFiles();
        StartupController.initializeStage(stage);
        ErrorHandler.showErrorPopup();

        LOGGER.info("Initialization complete!");
        LOGGER.info("Loaded a total of:");
        LOGGER.info("\t " + Context.getTileRepository().getTileCount() + " tiles");
        LOGGER.info("\t " + Context.getUnitRepository().getUnitCount() + " units");
        LOGGER.info("\t " + Context.getMapRepository().getMapCount() + " maps");

        stage.show();
    }

}
