package org.itdhbw.futurewars.controller.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.util.AStarPathfinder;
import org.itdhbw.futurewars.view.tile.TileView;

import java.util.ArrayList;
import java.util.List;

public class MovingUnitModeHandler implements MouseEventHandler {
    private static final List<TileModel> highlightedTiles = new ArrayList<>();
    private final GameState gameState;
    private final UnitMovementController unitMovementController;
    private final AStarPathfinder pathfinder;

    public MovingUnitModeHandler(GameState gameState, UnitMovementController unitMovementController, AStarPathfinder pathfinder) {
        this.gameState = gameState;
        this.unitMovementController = unitMovementController;
        this.pathfinder = pathfinder;
    }

    @Override
    public void handleMouseEnter(MouseEvent event, TileView tileView) {
        List<TileModel> newPath = pathfinder.findPath(gameState.getSelectedTileProperty().get(), tileView.getTileModel());

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
    }

    @Override
    public void handleMouseClick(MouseEvent event, TileView tileView) {
        UnitModel unitModel = gameState.getSelectedTileProperty().get().getOccupyingUnit();
        unitMovementController.moveUnit(unitModel, tileView.getTileModel());
        gameState.setActiveMode(ActiveMode.REGULAR);
        gameState.deselectTile();
        for (TileModel tile : highlightedTiles) {
            tile.setPartOfPath(false);
        }
    }

}

