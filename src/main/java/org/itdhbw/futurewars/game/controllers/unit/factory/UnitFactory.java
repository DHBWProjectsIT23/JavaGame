package org.itdhbw.futurewars.game.controllers.unit.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.unit.TargetType;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.views.UnitView;

import java.net.URI;
import java.util.List;

public class UnitFactory {
    private static final Logger LOGGER =
            LogManager.getLogger(UnitFactory.class);
    private final String unitType;
    private final int attackRange;
    private final int movementRange;
    private final int travelCostPlain;
    private final int travelCostWood;
    private final int travelCostMountain;
    private final int travelCostSea;
    private final Image texture1;
    private final Image texture2;
    private final int baseDamage;
    private final int armor;
    private final int piercing;
    private final int lowAirPiercing;
    private final TargetType targetType;
    private final List<TargetType> canAttackType;
    private UnitModel unitModel;
    private UnitView unitView;

    public UnitFactory(String unitType, int attackRange, int movementRange, int travelCostPlain, int travelCostWood, int travelCostMountain, int travelCostSea, URI texture1, URI texture2, int baseDamage, int armor, int piercing, int lowAirPiercing, TargetType targetType, List<TargetType> canAttackType) {
        LOGGER.info("Creating unit factory for unit type: {}", unitType);
        this.unitType = unitType;
        this.attackRange = attackRange;
        this.movementRange = movementRange;
        this.travelCostPlain = travelCostPlain;
        this.travelCostWood = travelCostWood;
        this.travelCostMountain = travelCostMountain;
        this.travelCostSea = travelCostSea;

        this.texture1 = new Image(texture1.toString());
        this.texture2 = new Image(texture2.toString());

        this.baseDamage = baseDamage;
        this.armor = armor;
        this.piercing = piercing;
        this.lowAirPiercing = lowAirPiercing;
        this.targetType = targetType;
        this.canAttackType = canAttackType;
    }

    private void createUnitModel(int team) {
        LOGGER.info("Creating unit model");
        unitModel = new UnitModel(unitType, team);
        unitModel.setAttackRange(attackRange);
        unitModel.setMovementRange(movementRange);
        unitModel.setPlainTravelCost(travelCostPlain);
        unitModel.setWoodsTravelCost(travelCostWood);
        unitModel.setMountainTravelCost(travelCostMountain);
        unitModel.setSeaTravelCost(travelCostSea);
        unitModel.setBaseDamage(baseDamage);
        unitModel.setArmor(armor);
        unitModel.setPiercing(piercing);
        unitModel.setLowAirPiercing(lowAirPiercing);
        unitModel.setTargetType(targetType);
        unitModel.setCanAttackType(canAttackType);
    }

    private void createUnitView() {
        LOGGER.info("Creating unit view");
        unitView = new UnitView(unitModel);
        unitView.setTexture(texture1, texture2);
    }

    public Pair<UnitModel, UnitView> createUnit(int team) {
        createUnitModel(team);
        createUnitView();
        return new Pair<>(unitModel, unitView);
    }

    public Pair<Image, Image> getUnitTextures() {
        return new Pair<>(texture1, texture2);
    }
}
