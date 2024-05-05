package org.itdhbw.futurewars.controller.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileController;
import org.itdhbw.futurewars.controller.unit.UnitController;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.util.Constants;
import org.itdhbw.futurewars.view.tile.TestTileView;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.unit.TestUnitView;

public class MapController {
    private static final Logger LOGGER = LogManager.getLogger(MapController.class);
    private final TileView[][] tiles = new TileView[Constants.MAP_ROWS][Constants.MAP_COLUMNS];

    public MapController() {
        // Called by FXMLLoader
    }

    @FXML
    private GridPane gameGrid;

    public void initialize() {
        LOGGER.info("Creating tiles...");
        for (int y = 0; y < (Constants.MAP_ROWS - 1); y++) {
            for (int x = 0; x < (Constants.MAP_COLUMNS - 1); x++) {
                TestTileView testTile = (TestTileView) TileController.createTile(TileType.TEST_TILE, x, y);
                tiles[y][x] = testTile;
                gameGrid.add(testTile, x, y);
            }
        }
        LOGGER.info("Game grid {} initialized!", gameGrid);

        LOGGER.info("Creating Units...");
        TestUnitView testUnitView = (TestUnitView) UnitController.createUnit(UnitType.TEST_UNIT, tiles[0][0].getTileModel(), 1);
        TestUnitView testUnitView2 = (TestUnitView) UnitController.createUnit(UnitType.TEST_UNIT, tiles[0][1].getTileModel(), 2);
    }
}