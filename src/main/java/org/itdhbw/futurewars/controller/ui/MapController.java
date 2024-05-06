package org.itdhbw.futurewars.controller.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.map.MapLoader;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.unit.UnitCreationController;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.tile.TileView;

public class MapController {
    private static final Logger LOGGER = LogManager.getLogger(MapController.class);
    private final TileCreationController tileCreationController;
    private final UnitCreationController unitCreationController;
    private final GameState gameState;
    private final MapLoader mapLoader;
    @FXML
    private GridPane gameGrid;

    public MapController() {
        this.tileCreationController = Context.getTileCreationController();
        this.unitCreationController = Context.getUnitCreationController();
        this.gameState = Context.getGameState();
        this.mapLoader = Context.getMapLoader();
    }

    private void addTileToGrid(Pair<TileModel, TileView> tile) {
        LOGGER.info("Pair: {} - Model: {} - View: {}", tile, tile.getKey(), tile.getValue());
        LOGGER.info("Tile position: {}", tile.getKey().getPosition().toString());
        Position position = tile.getKey().getPosition();
        gameGrid.add(tile.getValue(), position.getX(), position.getY());
    }

    public void initialize() {

        Context.setMapController(this);
        this.addTilesToGrid();
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
        //unitCreationController.createUnit(UnitType.TEST_UNIT, 1, 4, 1);
        //unitCreationController.createUnit(UnitType.TEST_UNIT, 0, 0, 1);
        //unitCreationController.createUnit(UnitType.TEST_UNIT, 5, 0, 1);
        //unitCreationController.createUnit(UnitType.TEST_UNIT, 7, 2, 1);
        //unitCreationController.createUnit(UnitType.TEST_UNIT, 3, 9, 1);
    }

    public void loadMap(String filename) {
        this.gameGrid.getChildren().clear();
        try {
            mapLoader.loadMap(filename);
            this.addTilesToGrid();
        } catch (Exception e) {
            LOGGER.error("Error loading map: {}", e.getMessage());
        }
    }

    private void addTilesToGrid() {
        LOGGER.info("Loading map...");

        Pair<TileModel, TileView>[][] allTiles = Context.getTileRepository().getAllTiles();
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
    }
}