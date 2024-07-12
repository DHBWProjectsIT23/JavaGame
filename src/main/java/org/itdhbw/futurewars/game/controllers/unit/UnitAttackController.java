package org.itdhbw.futurewars.game.controllers.unit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.exceptions.NoUnitSelectedException;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.unit.TargetType;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

public class UnitAttackController {
    private static final Logger LOGGER = LogManager.getLogger(UnitAttackController.class);
    private final GameState gameState;

    public UnitAttackController() {
        this.gameState = Context.getGameState();
    }

    public static int calculateDamagePoints(UnitModel attackingUnit, UnitModel attackedUnit) {
        return (int) Math.round((double) calculateActualPercentDamage(attackingUnit, attackedUnit) / 10);
    }

    private static int calculateActualPercentDamage(UnitModel attackingUnit, UnitModel attackedUnit) {
        int baseDamage = calcBaseDamage(attackingUnit, attackedUnit);
        int rawDamage = (int) (baseDamage *
                               (((double) attackingUnit.getCurrentHealth()) / ((double) attackingUnit.getMaxHealth())));
        double cover = ((int) (100 - (attackedUnit.currentTileProperty().get().getTerrainCover() *
                                      (((double) attackedUnit.getCurrentHealth()) /
                                       ((double) attackedUnit.getMaxHealth()))))) / 100.0;
        return (int) (rawDamage * cover);  // All these (double) casts are actually necessary as long health is int
    }

    public void attack(UnitModel attackedUnit) {
        // Throw properly
        UnitModel attackingUnit;
        try {
            attackingUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, cannot attack");
            return;
        }

        if (attackingUnit.getTeam() != gameState.getCurrentPlayer()) {
            LOGGER.error("Unit does not belong to current team");
        }

        LOGGER.info("Target type: {} - Can Attack: {}", attackedUnit.getTargetType(),
                    attackingUnit.getVulnerableTypes());
        if (attackingUnit.getVulnerableTypes().contains(attackedUnit.getTargetType())) {
            attackedUnit.takeDamage(calculateDamagePoints(attackingUnit, attackedUnit));
            if (!attackedUnit.isDead()) {
                if (attackedUnit.getVulnerableTypes()
                        .contains(attackingUnit.getTargetType())) {
                    attackingUnit.takeDamage(calculateDamagePoints(attackedUnit, attackingUnit));
                } else {
                    LOGGER.error("Target Unit cannot counterattack");
                }
            } else {
            LOGGER.info("Target Unit cant counterattack cause it is dead");
            }
        } else {
            LOGGER.error("Unit cannot attack this target type");
        }


        attackingUnit.setHasMadeAnAction(true);
    }

    private static int calcBaseDamage(UnitModel attackingUnit, UnitModel attackedUnit) {
        return (int) (Math.round(attackingUnit.getBaseDamage() -
                                 (attackingUnit.getBaseDamage() * calcArmor(attackingUnit, attackedUnit))));
    }

    private static double calcArmor(UnitModel attackingUnit, UnitModel attackedUnit) {
        if (attackedUnit.getTargetType() == TargetType.LOW_AIR) {
            return attackedUnit.getArmor() - (attackedUnit.getArmor() * attackingUnit.getLowAirPiercing());
        } else {
            return attackedUnit.getArmor() - (attackedUnit.getArmor() * attackingUnit.getPiercing());
        }
    }

}
