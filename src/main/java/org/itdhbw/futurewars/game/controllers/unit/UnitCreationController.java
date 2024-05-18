package org.itdhbw.futurewars.game.controllers.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitBuilder;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.UnitView;

/**
 * The type Unit creation controller.
 */
public class UnitCreationController {
    private static final Logger LOGGER =
            LogManager.getLogger(UnitCreationController.class);
    private final TileRepository tileRepository;
    private final UnitBuilder unitBuilderCustom;

    /**
     * Instantiates a new Unit creation controller.
     */
    public UnitCreationController() {
        this.tileRepository = Context.getTileRepository();
        this.unitBuilderCustom = Context.getUnitBuilder();
    }

    /**
     * Create unit.
     *
     * @param unitType the unit type
     * @param x        the x
     * @param y        the y
     * @param team     the team
     */
    public void createUnit(String unitType, int x, int y, int team) {
        LOGGER.info("Spawning custom unit at position ({}, {})", x, y);
        Pair<UnitModel, UnitView> unitPair =
                unitBuilderCustom.createUnit(unitType, team);
        LOGGER.info("Trying to spawn custom unit at position ({}, {})", x, y);
        unitPair.getKey()
                .spawn(tileRepository.getTileModel(new Position(x, y)));
    }

}
