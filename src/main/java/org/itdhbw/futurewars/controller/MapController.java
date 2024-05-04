package org.itdhbw.futurewars.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TestTile;
import org.itdhbw.futurewars.model.tile.Tile;
import org.itdhbw.futurewars.model.unit.TestUnit;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.util.Constants;

public class MapController {
    private static final Logger LOGGER = LogManager.getLogger(MapController.class);
    private final Tile[][] tiles = new Tile[Constants.MAP_ROWS][Constants.MAP_COLUMNS];

    private MapController() {
        // private constructor to hide the implicit public one
    }

    @FXML
    private GridPane gameGrid;

    public void initialize() {
        LOGGER.info("Calculating maximum tile size...");
        int maxTileWidth = Constants.GAME_WIDTH / Constants.MAP_COLUMNS;
        int maxTileHeight = Constants.GAME_HEIGHT / Constants.MAP_ROWS;
        int maxTileSize = Math.min(maxTileWidth, maxTileHeight);

        LOGGER.info("Creating tiles...");
        for (int y = 0; y < (Constants.MAP_ROWS - 1); y++) {
            for (int x = 0; x < (Constants.MAP_COLUMNS - 1); x++) {
                TestTile testTile = new TestTile(x, y);
                tiles[y][x] = testTile;
                gameGrid.add(testTile.getStackPane(), x, y);
            }
        }

        LOGGER.info("Game grid {} initialized!", gameGrid);

        LOGGER.info("Creating Unit...");
        TestUnit _ = new TestUnit(tiles[0][0], maxTileSize, UnitType.TEST_UNIT, 1);
        TestUnit _ = new TestUnit(tiles[0][1], maxTileSize, UnitType.TEST_UNIT, 2);
    }
}