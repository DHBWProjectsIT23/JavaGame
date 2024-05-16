package org.itdhbw.futurewars.controller.unit.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.UnitView;

public class UnitFactory {
    private static final Logger LOGGER = LogManager.getLogger(UnitFactory.class);
    private final String unitType;
    private final int attackRange;
    private final int movementRange;
    private final int travelCostPlain;
    private final int travelCostWood;
    private final int travelCostMountain;
    private final int travelCostSea;
    private final String texture1;
    private final String texture2;
    private UnitModel unitModel;
    private UnitView unitView;

    public UnitFactory(String unitType, int attackRange, int movementRange, int travelCostPlain, int travelCostWood, int travelCostMountain, int travelCostSea, String texture1, String texture2) {
        LOGGER.info("Creating unit factory for unit type: {}", unitType);
        this.unitType = unitType;
        this.attackRange = attackRange;
        this.movementRange = movementRange;
        this.travelCostPlain = travelCostPlain;
        this.travelCostWood = travelCostWood;
        this.travelCostMountain = travelCostMountain;
        this.travelCostSea = travelCostSea;
        this.texture1 = texture1;
        this.texture2 = texture2;
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
        unitModel.debugLog();
    }

    private void createUnitView() {
        LOGGER.info("Creating unit view");
        unitView = new UnitView(unitModel);
        Image texture1Image = new Image("file:" + this.texture1);
        Image texture2Image = new Image("file:" + this.texture2);
        unitView.setTexture(texture1Image, texture2Image);
    }

    public Pair<UnitModel, UnitView> createUnit(int team) {
        createUnitModel(team);
        createUnitView();
        return new Pair<>(unitModel, unitView);
    }

    public Pair<String, String> getUnitTextures() {
        return new Pair<>(texture1, texture2);
    }
}
