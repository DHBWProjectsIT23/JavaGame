package org.itdhbw.futurewars.controller.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileController;
import org.itdhbw.futurewars.controller.unit.UnitController;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.util.Constants;
import org.itdhbw.futurewars.view.tile.TestTileView;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.unit.TestUnitView;

public class MapController {
    private static final Logger LOGGER = LogManager.getLogger(MapController.class);
    private final TileView[][] tiles = new TileView[Constants.MAP_ROWS][Constants.MAP_COLUMNS];
    private final TileController tileController;
    private final UnitController unitController;

    public MapController() {
        this.tileController = Context.getTileController();
        this.unitController = Context.getUnitController();
    }

    @FXML
    private GridPane gameGrid;

    public void initialize() {
        LOGGER.info("Creating tiles...");

        for (int y = 0; y < (Constants.MAP_ROWS - 1); y++) {
            for (int x = 0; x < (Constants.MAP_COLUMNS - 1); x++) {
                TestTileView testTile = (TestTileView) tileController.createTile(TileType.TEST_TILE, x, y);
                tiles[y][x] = testTile;
                gameGrid.add(testTile, x, y);
            }
        }
        LOGGER.info("Game grid {} initialized!", gameGrid);

        LOGGER.info("Creating Units...");
        TestUnitView testUnitView = (TestUnitView) unitController.createUnit(UnitType.TEST_UNIT, tiles[0][0].getTileModel(), 1);
        TestUnitView testUnitView2 = (TestUnitView) unitController.createUnit(UnitType.TEST_UNIT, tiles[0][1].getTileModel(), 2);
    }
}