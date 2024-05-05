package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileController;
import org.itdhbw.futurewars.model.unit.TestUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.view.unit.TestUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

public class UnitBuilder {
    private static final Logger LOGGER = LogManager.getLogger(UnitBuilder.class);

    public Pair<UnitModel, UnitView> createUnit(UnitType unitType, int team) {
        switch (unitType) {
            case TEST_UNIT:
                LOGGER.info("Creating test unit for team {}", team);
                TestUnitModel unitModel = new TestUnitModel(team);
                TestUnitView unitView = new TestUnitView(unitModel);
                LOGGER.info("Created test unit model {} and view {}", unitModel.modelId, unitView.viewId);
                addListeners(unitModel, unitView);
                return new Pair<>(unitModel, unitView);
            default:
                throw new IllegalArgumentException("Unexpected value: " + unitType);
        }
    }

    private void addListeners(UnitModel unitModel, UnitView unitView) {
        LOGGER.info("Adding listeners to unit {} view", unitModel.modelId);
        unitModel.currentTileProperty().addListener((_, oldTile, newTile) -> {
            if (oldTile != null) {
                oldTile.removeOccupyingUnit();
                TileController.getTileView(newTile).removeFromStack(unitView);
            }
            if (newTile != null) {
                newTile.setOccupyingUnit(unitModel);
                TileController.getTileView(newTile).addToStack(unitView);
            }
        });
    }
}

