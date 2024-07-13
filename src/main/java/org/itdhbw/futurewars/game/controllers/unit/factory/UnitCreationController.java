package org.itdhbw.futurewars.game.controllers.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.UnitView;

import java.util.logging.Logger;

public class UnitCreationController {
    private static final Logger LOGGER = Logger.getLogger(UnitCreationController.class.getSimpleName());
    private final TileRepository tileRepository;
    private final UnitBuilder unitBuilderCustom;

    public UnitCreationController() {
        this.tileRepository = Context.getTileRepository();
        this.unitBuilderCustom = Context.getUnitBuilder();
    }

    public void createUnit(String unitType, int x, int y, int team) {
        LOGGER.info("Spawning unit at position (" + x + ", " + y + ")");
        Pair<UnitModel, UnitView> unitPair = unitBuilderCustom.createUnit(unitType, team);
        unitPair.getKey().spawn(tileRepository.getTileModel(new Position(x, y)));
    }

    @Override
    public String toString() {
        return "UnitCreationController{" + "tileRepository=" + tileRepository + ", unitBuilderCustom=" +
               unitBuilderCustom + '}';
    }
}
