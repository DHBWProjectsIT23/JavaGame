package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.unit.UnitRepository;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.view.unit.UnitView;

import java.util.HashMap;
import java.util.Map;

public class UnitBuilder {
    private static final Logger LOGGER = LogManager.getLogger(UnitBuilder.class);
    private final UnitRepository unitRepository;
    private final Map<UnitType, UnitFactory> unitFactories;
    private final TileRepository tileRepository;

    public UnitBuilder() {
        this.unitFactories = new HashMap<>();
        this.unitFactories.put(UnitType.TEST_UNIT, new TestUnitFactory());
        this.unitRepository = Context.getUnitRepository();
        this.tileRepository = Context.getTileRepository();
    }


    public Pair<UnitModel, UnitView> createUnit(UnitType unitType, int team) {
        UnitFactory factory = unitFactories.get(unitType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for unit type " + unitType);
        }
        Pair<UnitModel, UnitView> unitPair = factory.createUnit(team);
        addListeners(unitPair.getKey(), unitPair.getValue());
        unitRepository.addUnit(unitPair);
        return unitPair;
    }

    private void addListeners(UnitModel unitModel, UnitView unitView) {
        LOGGER.info("Adding listeners to unit {} view", unitModel.modelId);
        unitModel.currentTileProperty().addListener((_, oldTile, newTile) -> {
            if (oldTile != null) {
                oldTile.removeOccupyingUnit();
                tileRepository.getTileView(newTile.getPosition()).removeFromStack(unitView);
            }
            if (newTile != null) {
                newTile.setOccupyingUnit(unitModel);
                tileRepository.getTileView(newTile.getPosition()).addToStack(unitView);
            }
        });
    }
}

