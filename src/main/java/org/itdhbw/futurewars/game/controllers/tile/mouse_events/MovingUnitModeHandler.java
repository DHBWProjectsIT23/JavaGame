package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.gameState.ActiveMode;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.AStarPathfinder;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MovingUnitModeHandler implements MouseEventHandler {
    private static final List<TileModel> highlightedTiles = new ArrayList<>();
    private static final Logger LOGGER =
            LogManager.getLogger(MovingUnitModeHandler.class);
    private final GameState gameState;
    private final AStarPathfinder pathfinder;

    public MovingUnitModeHandler(GameState gameState, AStarPathfinder pathfinder) {
        this.gameState = gameState;
        this.pathfinder = pathfinder;
    }

    @Override
    public void handleMouseEnter(MouseEvent event, TileView tileView) {
        Task<List<TileModel>> getMovableTiles = new Task<>() {
            @Override
            protected List<TileModel> call() {
                return pathfinder.findPath(
                        gameState.selectedTileProperty().get(),
                        tileView.getTileModel());
            }
        };

        getMovableTiles.setOnSucceeded(taskEvent -> {
            List<TileModel> newPath = getMovableTiles.getValue();

            for (TileModel tile : new ArrayList<>(highlightedTiles)) {
                if (!newPath.contains(tile)) {
                    tile.setPartOfPath(false);
                    highlightedTiles.remove(tile);
                }
            }

            for (TileModel tile : newPath) {
                if (!highlightedTiles.contains(tile)) {
                    tile.setPartOfPath(true);
                    highlightedTiles.add(tile);
                }
            }
        });

        Task<Set<TileModel>> getAttackableTiles = new Task<>() {
            @Override
            protected Set<TileModel> call() {
                return pathfinder.getAttackableTiles(tileView.getTileModel(),
                                                     gameState.selectedUnitProperty()
                                                              .get());
            }
        };

        getAttackableTiles.setOnSucceeded(_ -> {
            Set<TileModel> attackableTiles = getAttackableTiles.getValue();
            for (TileModel tile : attackableTiles) {
                LOGGER.info("Tile {} is attackable", tile.modelId);
            }
            gameState.selectedUnitProperty().get()
                     .setCanAttack(!attackableTiles.isEmpty());
        });

        new Thread(getMovableTiles).start();
        new Thread(getAttackableTiles).start();

    }

    @Override
    public void handleMouseClick(MouseEvent event, TileView tileView) {
        gameState.setActiveMode(ActiveMode.OVERLAY);
        gameState.selectTile(tileView.getTileModel());

        for (TileModel tile : highlightedTiles) {
            tile.setPartOfPath(false);
        }
    }

}

