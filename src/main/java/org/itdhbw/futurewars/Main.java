package org.itdhbw.futurewars;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.util.Constants;

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
        // If a map should be directly loaded, uncomment the following lines
        try {
            Context.getMapLoader().loadMap("resources/maps/testMaps/5x5Map.fwm");
        } catch (
                  IOException e) {
            throw new RuntimeException("Failed to load map - {}", e);
        }
        LOGGER.info("Loading FXML file...");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        stage.setTitle("Future Wars");
        stage.setScene(scene);

        LOGGER.info("Showing stage...");
        stage.show();
    }
}
