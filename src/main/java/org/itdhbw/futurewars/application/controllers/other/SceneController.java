package org.itdhbw.futurewars.application.controllers.other;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;

import java.io.IOException;

public class SceneController {
    private SceneController() {
        // Prevent instantiation
    }

    public static void loadScene(String sceneName) throws FailedToLoadSceneException {
        Stage stage = Context.getPrimaryStage();
        if (stage.getScene() != null) {
            Context.getGameState().setPreviousRoot(stage.getScene().getRoot());
        }
        try {
            Parent view = FXMLLoader.load(FileHelper.getFxmlFile(sceneName).toURL());

            Scene currentScene = stage.getScene();
            if (currentScene == null) {
                currentScene = new Scene(view);
                stage.setScene(currentScene);
            }
            currentScene.getStylesheets().add(FileHelper.getFile("$INTERNAL_DIR/css/styles.css").toString());
            stage.getScene().setRoot(view);
        } catch (IOException | FailedToLoadFileException e) {
            ErrorHandler.addException(e, "Failed to load file: " + sceneName);
            throw new FailedToLoadSceneException(sceneName);
        }
    }
}
