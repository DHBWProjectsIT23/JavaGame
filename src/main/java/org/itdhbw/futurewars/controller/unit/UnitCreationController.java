package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.view.unit.UnitView;

public class UnitCreationController {
    private static final Logger LOGGER = LogManager.getLogger(UnitCreationController.class);
    private final UnitBuilder unitBuilder;

    public UnitCreationController() {
        unitBuilder = Context.getUnitBuilder();
    }

    public UnitView createUnit(UnitType unitType, TileModel initialTile, int team) {
        LOGGER.info("Creating unit of type {} for team {}", unitType, team);
        Pair<UnitModel, UnitView> unitPair = unitBuilder.createUnit(unitType, team);
        LOGGER.info("Spawning unit {} at tile {}", unitPair.getKey().modelId, initialTile.modelId);
        unitPair.getKey().spawn(initialTile);
        return unitPair.getValue();
    }
}
