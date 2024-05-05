package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.util.AStarPathfinder;
import org.itdhbw.futurewars.view.unit.UnitView;

import java.util.List;

public class UnitController {
    private static final Logger LOGGER = LogManager.getLogger(UnitController.class);
    private UnitBuilder unitBuilder;
    private AStarPathfinder pathfinder;

    public UnitController() {
        // empty constructor
    }

    public void initialize() {
        this.unitBuilder = Context.getUnitBuilder();
        this.pathfinder = Context.getPathfinder();
    }

    public UnitView createUnit(UnitType unitType, TileModel initialTile, int team) {
        LOGGER.info("Creating unit of type {} for team {}", unitType, team);
        Pair<UnitModel, UnitView> unitPair = unitBuilder.createUnit(unitType, team);
        LOGGER.info("Spawning unit {} at tile {}", unitPair.getKey().modelId, initialTile.modelId);
        unitPair.getKey().spawn(initialTile);
        return unitPair.getValue();
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
