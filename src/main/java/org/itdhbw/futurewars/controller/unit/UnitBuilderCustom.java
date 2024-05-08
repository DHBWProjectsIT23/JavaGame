package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.unit.CustomUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.CustomUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

import java.util.Map;

public class UnitBuilderCustom {
    private static final Logger LOGGER = LogManager.getLogger(UnitBuilderCustom.class);
    private final UnitRepository unitRepository;
    private final Map<String, UnitFactoryCustom> unitFactories;
    private final TileRepository tileRepository;

    public UnitBuilderCustom() {
        this.unitFactories = Context.getUnitLoader().getUnitFactories();

        this.unitRepository = Context.getUnitRepository();
        this.tileRepository = Context.getTileRepository();

    }

    public Pair<UnitModel, UnitView> createUnit(String unitType) {
        UnitFactoryCustom factory = unitFactories.get(unitType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for unit type " + unitType);
        }
        Pair<CustomUnitModel, CustomUnitView> unitPairCustom = factory.getUnit();
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
                tileRepository.getTileView(newTile.getPosition()).removeFromStack(unitView);
            }
            if (newTile != null) {
                newTile.setOccupyingUnit(unitModel);
                tileRepository.getTileView(newTile.getPosition()).addToStack(unitView);
            }
        });
    }
}

