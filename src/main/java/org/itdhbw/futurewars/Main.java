package org.itdhbw.futurewars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;

import java.io.IOException;

public class Main extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(final String[] args) {
        launch();
    }

    @Override
    public void start(final Stage stage) throws IOException {

        LOGGER.info("Initializing application...");
        Context.initialize();
        Context.getOptionsController().initializeSettings(stage);
        Context.getGameState().setMapWidth((int) (stage.getWidth() / 100 * 90));
        Context.getGameState().setMapHeight((int) stage.getHeight() / 100 * 90);
        Context.setPrimaryStage(stage);
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            Context.getGameState().setMapWidth(newValue.intValue() / 100 * 90);
        });
        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            Context.getGameState().setMapHeight(newValue.intValue() / 100 * 90);
        });

        LOGGER.info("Loading FXML file...");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //Rectangle2D visualBounds = Screen.getPrimary().getBounds();
        //double width = visualBounds.getWidth();
        //double height = visualBounds.getHeight();
        //LOGGER.info("Setting stage size to {}x{}...", width, height);
        //stage.setWidth(width);
        //stage.setHeight(height);
        //stage.setMaximized(true);


        //stage.setResizable(false);
        stage.setTitle("Future Wars");
        stage.setScene(scene);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        LOGGER.info("Showing stage...");
        stage.show();
    }
}
