package org.itdhbw.futurewars.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.itdhbw.futurewars.model.tile.TestTile;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MapController {
    private static final Logger LOGGER = Logger.getLogger(MapController.class.getName());

    private static final int SCREEN_WIDTH = 1600;
    private static final int SCREEN_HEIGHT = 900;
    private static final int ROWS = 14;
    private static final int COLUMNS = 26;
    @FXML
    private GridPane gameGrid;

    public void initialize() {
        LOGGER.info("Calculating maximum tile size...");
        int maxTileWidth = SCREEN_WIDTH / COLUMNS;
        int maxTileHeight = SCREEN_HEIGHT / ROWS;
        int maxTileSize = Math.min(maxTileWidth, maxTileHeight);

        LOGGER.info("Creating tiles...");
        for (int y = 0; y < (ROWS - 1); y++) {
            for (int x = 0; x < (COLUMNS - 1); x++) {
                TestTile testTile = new TestTile(x, y, maxTileSize);
                gameGrid.add(testTile.getStackPane(), x, y);
            }
        }

        LOGGER.log(Level.INFO, "Game grid {0} initialized!", gameGrid);
    }
}