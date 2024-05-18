package org.itdhbw.futurewars.game.controllers.unit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

/**
 * The type Unit attack controller.
 */
public class UnitAttackController {
    private static final Logger LOGGER =
            LogManager.getLogger(UnitAttackController.class);
    private final UnitRepository unitRepository;
    private final GameState gameState;

    /**
     * Instantiates a new Unit attack controller.
     */
    public UnitAttackController() {
        this.unitRepository = Context.getUnitRepository();
        this.gameState = Context.getGameState();
    }

    /**
     * Attack.
     *
     * @param attackedUnit the attacked unit
     */
    public void attack(UnitModel attackedUnit) {
        UnitModel attackingUnit = gameState.getSelectedUnit();
        if (attackingUnit == null) {
            LOGGER.error("No unit selected for attack");
        }
        if (attackingUnit.getTeam() != gameState.getCurrentPlayer()) {
            LOGGER.error("Unit does not belong to current team");
        }
        attackedUnit.takeDamage(1);
        attackingUnit.setHasMoved(true);
    }

}
