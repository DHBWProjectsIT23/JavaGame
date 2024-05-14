package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.unit.factory.UnitBuilder;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.unit.UnitView;

public class UnitCreationController {
    private static final Logger LOGGER = LogManager.getLogger(UnitCreationController.class);
    private final TileRepository tileRepository;
    private final UnitBuilder unitBuilderCustom;

    public UnitCreationController() {
        this.tileRepository = Context.getTileRepository();
        this.unitBuilderCustom = new UnitBuilder();
    }

    public void createUnit(String unitType, int x, int y) {
        LOGGER.info("Spawning custom unit at position ({}, {})", x, y);
        Pair<UnitModel, UnitView> unitPair = unitBuilderCustom.createUnit(unitType);
        LOGGER.info("Trying to spawn custom unit at position ({}, {})", x, y);
        unitPair.getKey().spawn(tileRepository.getTileModel(new Position(x, y)));
    }

}
