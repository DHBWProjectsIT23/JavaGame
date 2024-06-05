package org.itdhbw.futurewars.game.controllers.unit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.unit.TargetType;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

public class UnitAttackController {
    private static final Logger LOGGER =
            LogManager.getLogger(UnitAttackController.class);
    private final UnitRepository unitRepository;
    private final GameState gameState;

    public UnitAttackController() {
        this.unitRepository = Context.getUnitRepository();
        this.gameState = Context.getGameState();
    }

    private int calcBaseDamage(UnitModel attackingUnit, UnitModel attackedUnit) {
        return (int) (Math.round(attackingUnit.getBaseDamage() - (attackingUnit.getBaseDamage() * calcArmor(attackingUnit, attackedUnit))));
    }

    private double calcArmor(UnitModel attackingUnit, UnitModel attackedUnit) {
        if (attackedUnit.getTargetType() == TargetType.LOW_AIR) {
            return attackedUnit.getArmor() - (attackedUnit.getArmor() * attackingUnit.getLowAirPiercing());
        } else {
            return attackedUnit.getArmor() - (attackedUnit.getArmor() * attackingUnit.getPiercing());
        }
    }

    private int actualPercentDamage(UnitModel attackingUnit, UnitModel attackedUnit) {
        int baseDamage = calcBaseDamage(attackingUnit, attackedUnit);
        int rawDamage = (int) (baseDamage *
                (((double) attackingUnit.getCurrentHealth()) / ((double)attackingUnit.getMaxHealth())));
        double cover = ((int) (100 - (attackedUnit.currentTileProperty().get().getTerrainCover() *
                (((double) attackedUnit.getCurrentHealth()) / ((double) attackedUnit.getMaxHealth()))))) / 100.0;
        return (int) (rawDamage * cover);  // All these (double) casts are actually necessary as long health is int
    }

    private int calcDamagePoints(UnitModel attackingUnit, UnitModel attackedUnit) {
        return (int) Math.round((double) actualPercentDamage(attackingUnit, attackedUnit)/10);
    }

    public void attack(UnitModel attackedUnit) {
        UnitModel attackingUnit = gameState.getSelectedUnit();
        if (attackingUnit == null) {
            LOGGER.error("No unit selected for attack");
        }
        if (attackingUnit.getTeam() != gameState.getCurrentPlayer()) {
            LOGGER.error("Unit does not belong to current team");
        }

        LOGGER.info("Target type: {} - Can Attack: {}", attackedUnit.getTargetType(), attackingUnit.getCanAttackType());
        if (attackingUnit.getCanAttackType().contains(attackedUnit.getTargetType())) {
            attackedUnit.takeDamage(calcDamagePoints(attackingUnit, attackedUnit));
            if (attackedUnit.getCanAttackType().contains(attackingUnit.getTargetType())) { // Todo: Nicht machen wenn attackedUnit tot
                attackingUnit.takeDamage(calcDamagePoints(attackedUnit, attackingUnit));
            } else {
                LOGGER.error("Target Unit cannot counterattack");
            }
        } else {
            LOGGER.error("Unit cannot attack this target type");
        }

        attackingUnit.setHasMoved(true);
    }

}
