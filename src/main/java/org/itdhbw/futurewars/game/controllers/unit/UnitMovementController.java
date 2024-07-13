package org.itdhbw.futurewars.game.controllers.unit;

import javafx.concurrent.Task;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Pathfinder;

import java.util.List;
import java.util.logging.Logger;

public class UnitMovementController {
    private static final Logger LOGGER = Logger.getLogger(UnitMovementController.class.getSimpleName());
    private Pathfinder pathfinder;

    public UnitMovementController() {
        // empty constructor
    }

    public void initialize() {
        this.pathfinder = Context.getPathfinder();
    }

    public void moveUnit(UnitModel unit, TileModel targetTile) {
        if (targetTile.isOccupied()) {
            LOGGER.warning("Tile is already occupied!");
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
                LOGGER.warning("No Path found!");
                return;
            }

            for (TileModel tile : path) {
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

    @Override
    public String toString() {
        return "UnitMovementController{" + "pathfinder=" + pathfinder + '}';
    }
}
