package org.itdhbw.futurewars.game.controllers.unit;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Pathfinder;

import java.util.List;

public class UnitMovementController {
    private static final Logger LOGGER = LogManager.getLogger(UnitMovementController.class);
    private Pathfinder pathfinder;

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

        Task<List<TileModel>> task = new Task<>() {
            @Override
            protected List<TileModel> call() {
                return pathfinder.findPath(unit.currentTileProperty().get(), targetTile);
            }
        };

        task.setOnSucceeded(event -> {
            List<TileModel> path = task.getValue();
            if (path.isEmpty()) {
                LOGGER.error("No path found from tile {} to tile {}", unit.currentTileProperty().get().modelId,
                             targetTile.modelId);
                return;
            }

            for (TileModel tile : path) {
                LOGGER.info("Moving unit {} from tile {} to tile {}", unit.modelId,
                            unit.currentTileProperty().get().modelId, tile.modelId);
                unit.currentTileProperty().get().removeOccupyingUnit();
                unit.currentTileProperty().set(tile);
                tile.setOccupyingUnit(unit);
            }
            unit.setHasMadeAnAction(true);
        });

        new Thread(task).start();
    }

    public void mergeUnit(UnitModel unit, TileModel targetTile) {
        UnitModel targetUnit = targetTile.getOccupyingUnit();
        unit.mergeInto(targetUnit);
    }


}
