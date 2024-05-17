package org.itdhbw.futurewars.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.util.ErrorPopup;
import org.itdhbw.futurewars.util.FileHelper;
import org.itdhbw.futurewars.util.exceptions.CanNotLoadException;

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

    public static void loadUnits() throws CanNotLoadException {
        LOGGER.info("Retrieving unit files...");
        Context.getUnitLoader().retrieveSystemUnits();
        Context.getUnitLoader().retrieveUserUnits();
        LOGGER.info("Loading units...");
        Context.getUnitLoader().loadUnitsFromFiles();
    }

    public static void loadTiles() throws CanNotLoadException {
        LOGGER.info("Retrieving tile files...");
        Context.getTileLoader().retrieveSystemTiles();
        Context.getTileLoader().retrieveUserTiles();
        LOGGER.info("Loading tiles...");
        Context.getTileLoader().loadTilesFromFiles();
    }

    public static void retrieveMaps() throws TextureNotLoaded {
        LOGGER.info("Retrieving map files...");
        Context.getMapLoader().retrieveSystemMaps();
        Context.getMapLoader().retrieveUserMaps();
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

        initializeScene(stage);

    }

    private static void initializeScene(Stage stage) {
        LOGGER.info("Loading menu scene...");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FileHelper.getFxmlFile("menu-view.fxml").toURL());
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException | TextureNotLoaded e) {
            ErrorPopup.showUnrecoverableErrorPopup("Could not load menu scene", e);
        }
    }

    private static void initializeGameState(GameState gameState, Stage stage) {
        gameState.setMapWidth((int) (stage.getWidth() / 100 * 90));
        gameState.setMapHeight((int) stage.getHeight() / 100 * 90);
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
            LOGGER.error("Could not create directory", e);
        }
    }
}
