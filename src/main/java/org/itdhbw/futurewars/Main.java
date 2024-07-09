package org.itdhbw.futurewars;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.StartupController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;

import java.io.IOException;

public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(final String[] args) {
        launch();
    }

    @Override
    public void start(final Stage stage) throws IOException {


        StartupController.loadFonts();
        StartupController.initializeUserDirectory();
        StartupController.initializeContext(stage);
        StartupController.loadFiles();
        StartupController.initializeStage(stage);
        ErrorHandler.showErrorPopup();


        LOGGER.info("Initialization complete!");
        LOGGER.info("Loaded a total of:");
        LOGGER.info("\t {} tiles", Context.getTileRepository().getTileCount());
        LOGGER.info("\t {} units", Context.getUnitRepository().getUnitCount());
        LOGGER.info("\t {} maps", Context.getMapRepository().getMapCount());

        stage.show();
    }

}
