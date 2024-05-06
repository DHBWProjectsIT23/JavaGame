package org.itdhbw.futurewars.controller.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.map.MapLoader;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.unit.UnitCreationController;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.unit.TestUnitView;

import java.io.IOException;

public class MapController {
    private static final Logger LOGGER = LogManager.getLogger(MapController.class);
    private final TileCreationController tileCreationController;
    private final UnitCreationController unitCreationController;
    private final TileRepository tileRepository;
    private final GameState gameState;
    @FXML
    private GridPane gameGrid;

    public MapController() {
        this.tileCreationController = Context.getTileCreationController();
        this.unitCreationController = Context.getUnitCreationController();
        this.tileRepository = Context.getTileRepository();
        this.gameState = Context.getGameState();
    }

    private void addTileToGrid(Pair<TileModel, TileView> tile) {
        LOGGER.info("Pair: {} - Model: {} - View: {}", tile, tile.getKey(), tile.getValue());
        LOGGER.info("Tile position: {}", tile.getKey().getPosition().toString());
        Position position = tile.getKey().getPosition();
        gameGrid.add(tile.getValue(), position.getX(), position.getY());
    }

    public void initialize() {
        LOGGER.info("Loading map...");
        MapLoader mapLoader = new MapLoader();
        try {
            mapLoader.loadMap("testMap1.csv");
        } catch (
                  IOException e) {
            throw new RuntimeException("Failed to load map - {}", e);
        }


        Pair<TileModel, TileView>[][] allTiles = tileRepository.getAllTiles();
        for (int x = 0; x < (gameState.getMapWidth() - 1); x++) {
            for (int y = 0; y < (gameState.getMapHeight() - 1); y++) {
                LOGGER.error("x: {} of {}, y: {} of {}", x, gameState.getMapWidth(), y, gameState.getMapHeight());
                //LOGGER.info("{}", row);
                Pair<TileModel, TileView> tilePair = allTiles[x][y];
                if (tilePair == null) {
                    LOGGER.warn("tilePair was null");
                    tilePair = tileCreationController.createTile(TileType.TILE_NOT_SET, x, y);
                }
                this.addTileToGrid(tilePair);
            }
        }
        /*for (int y = 0; y < (Constants.MAP_ROWS - 1); y++) {
            for (int x = 0; x < (Constants.MAP_COLUMNS - 1); x++) {
                TileView testTile;
                if (x == 3 && y == 1) {
                    testTile = tileCreationController.createTile(TileType.UNPASSABLE_TILE, x, y);
                } else {
                    testTile = tileCreationController.createTile(TileType.TEST_TILE, x, y);
                }
                tiles[y][x] = testTile;
                gameGrid.add(testTile, x, y);
            }
        }
        LOGGER.info("Game grid {} initialized!", gameGrid);*/

        LOGGER.info("Creating Units...");
        TestUnitView testUnitView = (TestUnitView) unitCreationController.createUnit(UnitType.TEST_UNIT, tileRepository.getAllTiles()[0][0].getKey(), 1);
        TestUnitView testUnitView2 = (TestUnitView) unitCreationController.createUnit(UnitType.TEST_UNIT, tileRepository.getAllTiles()[1][4].getKey(), 1);
        TestUnitView testUnitView3 = (TestUnitView) unitCreationController.createUnit(UnitType.TEST_UNIT, tileRepository.getAllTiles()[5][0].getKey(), 1);
        TestUnitView testUnitView4 = (TestUnitView) unitCreationController.createUnit(UnitType.TEST_UNIT, tileRepository.getAllTiles()[7][2].getKey(), 1);
        TestUnitView testUnitView5 = (TestUnitView) unitCreationController.createUnit(UnitType.TEST_UNIT, tileRepository.getAllTiles()[3][9].getKey(), 1);
    }
}