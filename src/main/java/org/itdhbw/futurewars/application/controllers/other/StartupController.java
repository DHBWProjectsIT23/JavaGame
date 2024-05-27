package org.itdhbw.futurewars.application.controllers.other;

import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.game.controllers.loaders.FileLoader;
import org.itdhbw.futurewars.game.models.gameState.GameState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StartupController {
    private static final Logger LOGGER =
            LogManager.getLogger(StartupController.class);

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

    public static void loadFonts() {
        try {
            Font.loadFont(FileHelper.getFile(
                                  "$INTERNAL_DIR/fonts/VCR_OSD_MONO_1.001.ttf").toString(),
                          12);
            Font.loadFont(FileHelper.getFile("$INTERNAL_DIR/fonts/upheavtt.ttf")
                                    .toString(), 12);
        } catch (FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to load font");
        }
    }

    public static void loadFiles() {
        FileLoader fileLoader = Context.getFileLoader();
        try {
            fileLoader.retrieveSystemFiles();
        } catch (FailedToRetrieveFilesException e) {
            ErrorHandler.addException(e, "Error while retrieving system files");
        }

        try {
            fileLoader.retrieveUserFiles();
        } catch (FailedToRetrieveFilesException e) {
            ErrorHandler.addException(e, "Error while retrieving user files");
        }

        fileLoader.loadFiles();
    }

    public static void initializeStage(Stage stage) {
        LOGGER.info("Initializing stage...");
        stage.widthProperty().addListener(
                (_, _, newValue) -> Context.getGameState().setMapWidth(
                        newValue.intValue() / 100 * 90));
        stage.heightProperty().addListener(
                (_, _, newValue) -> Context.getGameState().setMapHeight(
                        newValue.intValue() / 100 * 90));
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
            ErrorHandler.addException(e,
                                      "Failed to initialize user directories");
        }
    }

}
