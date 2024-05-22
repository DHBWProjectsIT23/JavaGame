package org.itdhbw.futurewars.application.controllers.other;

import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.game.models.gameState.GameState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartupController {
    private static final Logger LOGGER = LogManager.getLogger(StartupController.class);

    private StartupController() {
        // empty constructor
    }

    public static void initializeContext(Stage stage) {
        LOGGER.info("Initializing Context...");
        Context.initialize();

        LOGGER.info("Initializing settings...");
        Context.getOptionsController().initializeSettings(stage);

        initializeGameState(Context.getGameState(), stage);
    }

    public static void loadUnits() throws FailedToLoadFileException {
        try {
            LOGGER.info("Retrieving unit files...");
            Context.getUnitLoader().retrieveSystemUnits();
            Context.getUnitLoader().retrieveUserUnits();
        } catch (FailedToRetrieveFilesException e) {
            ErrorHandler.addException(e, "Failed to load units");
        }
        LOGGER.info("Loading units...");
        Context.getUnitLoader().loadUnitsFromFiles();
    }

    public static void loadTiles() throws FailedToLoadFileException {
        LOGGER.info("Retrieving tile files...");
        try {
            Context.getTileLoader().retrieveSystemTiles();
            Context.getTileLoader().retrieveUserTiles();
        } catch (FailedToRetrieveFilesException e) {
            ErrorHandler.addException(e, "Failed to load tiles");
        }
        LOGGER.info("Loading tiles...");
        Context.getTileLoader().loadTilesFromFiles();
    }

    public static void retrieveMaps() throws FailedToLoadFileException {
        try {
            LOGGER.info("Retrieving map files...");
            Context.getMapLoader().retrieveSystemMaps();
            Context.getMapLoader().retrieveUserMaps();
        } catch (FailedToRetrieveFilesException e) {
            ErrorHandler.addException(e, "Failed to load maps");
        }
    }

    public static void initializeStage(Stage stage) {
        LOGGER.info("Initializing stage...");
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            Context.getGameState().setMapWidth(newValue.intValue() / 100 * 90);
        });
        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            Context.getGameState().setMapHeight(newValue.intValue() / 100 * 90);
        });
        Context.setPrimaryStage(stage);
        stage.setTitle("Future Wars");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        initializeScene();

    }

    private static void initializeScene() {
        try {
            SceneController.loadScene("menu-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to load menu view");
        }
    }

    private static void initializeGameState(GameState gameState, Stage stage) {
        gameState.setMapWidth((int) (stage.getWidth() / 100 * 80));
        gameState.setMapHeight((int) stage.getHeight() / 100 * 80);
    }

    public static void initializeUserDirectory() {
        LOGGER.info("Checking user directory...");

        try {
            for (String subDir : FileHelper.SUB_DIRS) {
                Path dirPath = Paths.get(FileHelper.USER_DIR, subDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                    LOGGER.info("Directory {} created", dirPath);
                } else {
                    LOGGER.info("Directory {} exists", dirPath);
                }
            }
        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to initialize user directorys");
        }
    }
}
