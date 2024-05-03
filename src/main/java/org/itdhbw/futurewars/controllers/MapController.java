package org.itdhbw.futurewars.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.itdhbw.futurewars.models.tiles.TestTile;

import java.util.logging.Logger;

public class MapController {
    private static final Logger LOGGER = Logger.getLogger(MapController.class.getName());

    @FXML
    private GridPane gameGrid;
    @FXML
    private Button closeButton;
    @FXML
    private GridPane uiGrid;

    public void initialize() {

        LOGGER.info("Initialize method called");
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                TestTile testTile = new TestTile(x, y);
                gameGrid.add(testTile.getStackPane(), x, y);
            }
        }
        LOGGER.info("Game grid \"" + gameGrid + "\" initialized!");

        uiGrid.prefWidthProperty().bind(gameGrid.widthProperty());
        uiGrid.prefHeightProperty().bind(gameGrid.heightProperty());
    }

    @FXML
    private void closeApplication(ActionEvent actionEvent) {
        LOGGER.info("Closing application...");
        Platform.exit();
    }
}