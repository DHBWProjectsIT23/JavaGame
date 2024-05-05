package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.view.unit.UnitView;

public class UnitController {
    private static final Logger LOGGER = LogManager.getLogger(UnitController.class);
    private static final UnitBuilder UNIT_FACTORY = new UnitBuilder();

    private UnitController() {
        throw new IllegalStateException("Utility class");
    }

    public static UnitView createUnit(UnitType unitType, TileModel initialTile, int team) {
        LOGGER.info("Creating unit of type {} for team {}", unitType, team);
        Pair<UnitModel, UnitView> unitPair = UNIT_FACTORY.createUnit(unitType, team);
        LOGGER.info("Spawning unit {} at tile {}", unitPair.getKey().modelId, initialTile.modelId);
        unitPair.getKey().spawn(initialTile);
        return unitPair.getValue();
    }


}
