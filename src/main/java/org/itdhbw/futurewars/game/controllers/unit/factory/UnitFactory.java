package org.itdhbw.futurewars.game.controllers.unit.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.itdhbw.futurewars.game.models.unit.TargetType;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.views.UnitView;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class UnitFactory {
    private static final Logger LOGGER = Logger.getLogger(UnitFactory.class.getSimpleName());
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
    private final double armor;
    private final double piercing;
    private final double lowAirPiercing;
    private final TargetType targetType;
    private final List<TargetType> canAttackType;
    private UnitModel unitModel;
    private UnitView unitView;

    public UnitFactory(String unitType, int attackRange, int movementRange, int travelCostPlain, int travelCostWood, int travelCostMountain, int travelCostSea, URI texture1, URI texture2, int baseDamage, double armor, double piercing, double lowAirPiercing, TargetType targetType, List<TargetType> canAttackType) {
        LOGGER.info("Creating unit factory for unit type: " + unitType);
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

    public Pair<UnitModel, UnitView> createUnit(int team) {
        createUnitModel(team);
        createUnitView();
        return new Pair<>(unitModel, unitView);
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
        unitModel.setVulnerableTypes(canAttackType);
    }

    private void createUnitView() {
        LOGGER.info("Creating unit view");
        unitView = new UnitView(unitModel);
        unitView.setTexture(texture1, texture2);
    }

    public Pair<Image, Image> getUnitTextures() {
        return new Pair<>(texture1, texture2);
    }

    @Override
    public String toString() {
        return "UnitFactory{" + "unitType='" + unitType + '\'' + ", attackRange=" + attackRange + ", movementRange=" +
               movementRange + ", travelCostPlain=" + travelCostPlain + ", travelCostWood=" + travelCostWood +
               ", travelCostMountain=" + travelCostMountain + ", travelCostSea=" + travelCostSea + ", texture1=" +
               texture1 + ", texture2=" + texture2 + ", baseDamage=" + baseDamage + ", armor=" + armor + ", piercing=" +
               piercing + ", lowAirPiercing=" + lowAirPiercing + ", targetType=" + targetType + ", canAttackType=" +
               canAttackType + ", unitModel=" + unitModel + ", unitView=" + unitView + '}';
    }
}
