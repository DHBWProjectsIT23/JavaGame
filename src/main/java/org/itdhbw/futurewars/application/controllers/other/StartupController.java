package org.itdhbw.futurewars.application.controllers.other;

import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.game.controllers.loaders.FileLoader;
import org.itdhbw.futurewars.game.models.game_state.GameState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class StartupController {
    private static final Logger LOGGER = Logger.getLogger(StartupController.class.getSimpleName());

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

    private static void initializeGameState(GameState gameState, Stage stage) {
        LOGGER.info("Initializing game state...");
        gameState.setMapWidth((int) (stage.getWidth() / 100 * 80));
        gameState.setMapHeight((int) stage.getHeight() / 100 * 80);
    }

    public static void loadFonts() {
        LOGGER.info("Loading fonts...");
        try {
            Font.loadFont(FileHelper.getFile("$INTERNAL_DIR/fonts/upheavtt.ttf").toString(), 12);
        } catch (FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to load font");
        }
    }

    public static void loadFiles() {
        LOGGER.info("Loading files...");
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
                (observable, oldValue, newValue) -> Context.getGameState().setMapWidth(newValue.intValue() / 100 * 90));
        stage.heightProperty().addListener((observable, oldValue, newValue) -> Context.getGameState().setMapHeight(
                newValue.intValue() / 100 * 90));
        Context.setPrimaryStage(stage);
        stage.setTitle("Future Wars");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        try {
            stage.getIcons().add(FileHelper.getMiscTexture(FileHelper.MiscTextures.ICON));
        } catch (FailedToLoadTextureException e) {
            ErrorHandler.addException(e, "Failed to load icon");
        }
        stage.setResizable(false);

        initializeScene();

    }

    private static void initializeScene() {
        LOGGER.info("Initializing menu scene...");
        try {
            SceneController.loadScene("menu-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to load menu view");
        }
    }

    public static void initializeUserDirectory() {
        LOGGER.info("Initializing user directory...");

        try {
            for (String subDir : FileHelper.SUB_DIRS) {
                Path dirPath = Paths.get(FileHelper.USER_DIR, subDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                    LOGGER.info("Directory " + dirPath + " created");
                } else {
                    LOGGER.info("Directory " + dirPath + " exists");
                }
            }
        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to initialize user directories");
        }

        File userProperties = new File(FileHelper.USER_DIR + FileHelper.OTHER_TEXTURE_DIR + FileHelper.PROPERTIES_FILE);
        if (!userProperties.exists()) {
            try {
                if (!userProperties.createNewFile()) {
                    throw new IOException("Failed to create user properties file");
                }
            } catch (IOException e) {
                ErrorHandler.addException(e, "Failed to create user properties file");
            }
        }
    }
}
