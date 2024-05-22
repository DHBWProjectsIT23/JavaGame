package org.itdhbw.futurewars;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.StartupController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;

import java.io.IOException;

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

        try {
            Font.loadFont(FileHelper.getFile(
                                  "$INTERNAL_DIR/fonts/VCR_OSD_MONO_1.001.ttf").toString(),
                          12);
            Font.loadFont(FileHelper.getFile("$INTERNAL_DIR/fonts/upheavtt.ttf")
                                    .toString(), 12);
        } catch (FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to load font");
        }

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
