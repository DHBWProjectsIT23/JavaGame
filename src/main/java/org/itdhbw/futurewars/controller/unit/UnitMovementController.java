package org.itdhbw.futurewars.controller.unit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.util.AStarPathfinder;

import java.util.List;

public class UnitMovementController {
    private static final Logger LOGGER = LogManager.getLogger(UnitMovementController.class);
    private AStarPathfinder pathfinder;

    public UnitMovementController() {
        // empty constructor
    }

    public void initialize() {
        this.pathfinder = Context.getPathfinder();
    }

    public void moveUnit(UnitModel unit, TileModel targetTile) {
        if (targetTile.isOccupied()) {
            LOGGER.error("Tile {} is already occupied!", targetTile.modelId);
            return;
        }

        List<TileModel> path = pathfinder.findPath(unit.currentTileProperty().get(), targetTile);

        if (path.isEmpty()) {
            LOGGER.error("No path found from tile {} to tile {}", unit.currentTileProperty().get().modelId, targetTile.modelId);
            return;
        }

        for (TileModel tile : path) {
            LOGGER.info("Moving unit {} from tile {} to tile {}", unit.modelId, unit.currentTileProperty().get().modelId, tile.modelId);
            unit.currentTileProperty().get().removeOccupyingUnit();
            unit.currentTileProperty().set(tile);
            tile.setOccupyingUnit(unit);
        }
    }


}
