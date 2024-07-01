package org.itdhbw.futurewars.game.controllers.ui;

import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileCreationController;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.TileView;

public class MapGrid extends GridPane {
    private static final Logger LOGGER = LogManager.getLogger(MapGrid.class);
    private final GameState gameState;
    private final TileCreationController tileCreationController;

    public MapGrid() {
        super();
        this.gameState = Context.getGameState();
        this.tileCreationController = Context.getTileCreationController();
    }


    private void addTileToGrid(Pair<TileModel, TileView> tile) {
        LOGGER.info("Pair: {} - Model: {} - View: {}", tile, tile.getKey(),
                    tile.getValue());
        LOGGER.info("Tile position: {}", tile.getKey().getPosition());
        Position position = tile.getKey().getPosition();
        this.add(tile.getValue(), position.getX(), position.getY());
    }

    private void addTilesToGrid() {
        LOGGER.info("Loading map...");

        Pair<TileModel, TileView>[][] allTiles =
                Context.getTileRepository().getAllTiles();
        for (int x = 0; x < (gameState.getMapWidthTiles()); x++) {
            for (int y = 0; y < (gameState.getMapHeightTiles()); y++) {
                LOGGER.error("x: {} of {}, y: {} of {}", x,
                             gameState.getMapWidthTiles(), y,
                             gameState.getMapHeightTiles());
                Pair<TileModel, TileView> tilePair = allTiles[x][y];
                if (tilePair == null) {
                    LOGGER.warn("tilePair was null");
                    tilePair =
                            tileCreationController.createTile("NOT_SET_TILE", x,
                                                              y);
                }
                this.addTileToGrid(tilePair);
            }
        }


    }
}
