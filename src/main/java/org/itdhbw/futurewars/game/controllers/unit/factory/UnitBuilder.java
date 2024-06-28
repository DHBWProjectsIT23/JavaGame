package org.itdhbw.futurewars.game.controllers.unit.factory;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.unit.UnitRepository;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.views.UnitView;

import java.util.HashMap;
import java.util.Map;

public class UnitBuilder {
    private static final Logger LOGGER =
            LogManager.getLogger(UnitBuilder.class);
    private final UnitRepository unitRepository;
    private final Map<String, UnitFactory> unitFactories;
    private final TileRepository tileRepository;

    public UnitBuilder() {
        this.unitFactories = new HashMap<>();

        this.unitRepository = Context.getUnitRepository();
        this.tileRepository = Context.getTileRepository();

    }

    public void addUnitFactory(String unitType, UnitFactory factory) {
        unitFactories.put(unitType, factory);
    }

    public Pair<UnitModel, UnitView> createUnit(String unitType) {
        return createUnit(unitType, 1);
    }

    public Pair<UnitModel, UnitView> createUnit(String unitType, int team) {
        UnitFactory factory = unitFactories.get(unitType);
        if (factory == null) {
            throw new IllegalArgumentException(
                    "No factory found for unit type " + unitType);
        }
        Pair<UnitModel, UnitView> unitPairCustom = factory.createUnit(team);
        addListeners(unitPairCustom.getKey(), unitPairCustom.getValue());
        UnitModel unitModel = unitPairCustom.getKey();
        UnitView unitView = unitPairCustom.getValue();
        Pair<UnitModel, UnitView> unitPair = new Pair<>(unitModel, unitView);
        unitRepository.addUnit(unitPair);
        return unitPair;
    }

    private void addListeners(UnitModel unitModel, UnitView unitView) {
        LOGGER.info("Adding listeners to unit {} view", unitModel.modelId);
        unitModel.currentTileProperty().addListener((_, oldTile, newTile) -> {
            if (oldTile != null) {
                oldTile.removeOccupyingUnit();
                tileRepository.getTileView(newTile.getPosition())
                              .removeFromStack(unitView);
            }
            if (newTile != null) {
                newTile.setOccupyingUnit(unitModel);
                tileRepository.getTileView(newTile.getPosition())
                              .addToStack(unitView);
            }
        });

        unitModel.isDeadProperty().addListener((_, _, isDead) -> {
            if (Boolean.TRUE.equals(isDead)) {
                unitModel.currentTileProperty().get().removeOccupyingUnit();
                unitView.setVisible(false);
            }
        });
    }

    public Map<String, UnitFactory> getUnitFactories() {
        return unitFactories;
    }
}

