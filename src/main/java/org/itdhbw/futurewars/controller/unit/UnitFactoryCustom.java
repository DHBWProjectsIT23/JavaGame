package org.itdhbw.futurewars.controller.unit;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.unit.CustomUnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.view.unit.CustomUnitView;

public class UnitFactoryCustom {
    private static final Logger LOGGER = LogManager.getLogger(UnitFactoryCustom.class);
    private final String unitName;
    private final int attackRange;
    private final int movementRange;
    private final int travelCostPlain;
    private final int travelCostWood;
    private final int travelCostMountain;
    private final int travelCostSea;
    private final String texture1;
    private final String texture2;
    private final UnitType unitType;
    private CustomUnitModel unitModel;
    private CustomUnitView unitView;

    public UnitFactoryCustom(String unitName, int attackRange, int movementRange, int travelCostPlain, int travelCostWood, int travelCostMountain, int travelCostSea, String texture1, String texture2, UnitType unitType) {
        this.unitName = unitName;
        this.attackRange = attackRange;
        this.movementRange = movementRange;
        this.travelCostPlain = travelCostPlain;
        this.travelCostWood = travelCostWood;
        this.travelCostMountain = travelCostMountain;
        this.travelCostSea = travelCostSea;
        this.texture1 = texture1;
        this.texture2 = texture2;
        this.unitType = unitType;
    }

    private void createUnitModel() {
        LOGGER.info("Creating unit model");
        unitModel = new CustomUnitModel(unitType, 1);
        unitModel.setAttackRange(attackRange);
        unitModel.setMovementRange(movementRange);
        unitModel.setPlainTileTravelCost(travelCostPlain);
        unitModel.setWoodTileTravelCost(travelCostWood);
        unitModel.setMountainTileTravelCost(travelCostMountain);
        unitModel.setSeaTileTravelCost(travelCostSea);
        unitModel.debugLog();
    }

    private void createUnitView() {
        LOGGER.info("Creating unit view");
        unitView = new CustomUnitView(unitModel);
        Image texture1Image = new Image("file:" + this.texture1);
        Image texture2Image = new Image("file:" + this.texture2);
        unitView.setTexture(texture1Image, texture2Image);
    }

    public Pair<CustomUnitModel, CustomUnitView> getUnit() {
        createUnitModel();
        createUnitView();
        return new Pair<>(unitModel, unitView);
    }
}
