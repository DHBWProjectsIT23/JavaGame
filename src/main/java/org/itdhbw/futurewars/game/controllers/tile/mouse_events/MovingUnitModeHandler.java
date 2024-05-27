package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.game.models.gameState.ActiveMode;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.AStarPathfinder;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.ArrayList;
import java.util.List;

public class MovingUnitModeHandler implements MouseEventHandler {
    private static final List<TileModel> highlightedTiles = new ArrayList<>();
    private final GameState gameState;
    private final AStarPathfinder pathfinder;

    public MovingUnitModeHandler(GameState gameState, AStarPathfinder pathfinder) {
        this.gameState = gameState;
        this.pathfinder = pathfinder;
    }

    @Override
    public void handleMouseEnter(MouseEvent event, TileView tileView) {
        Task<List<TileModel>> task = new Task<>() {
            @Override
            protected List<TileModel> call() {
                return pathfinder.findPath(
                        gameState.selectedTileProperty().get(),
                        tileView.getTileModel());
            }
        };

        task.setOnSucceeded(taskEvent -> {
            List<TileModel> newPath = task.getValue();

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

        new Thread(task).start();

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

