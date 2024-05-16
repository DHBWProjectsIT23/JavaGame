package org.itdhbw.futurewars.controller.unit.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.UnitView;

import java.net.URI;
import java.net.URL;

public class UnitFactory {
    private static final Logger LOGGER = LogManager.getLogger(UnitFactory.class);
    private final String unitType;
    private final int attackRange;
    private final int movementRange;
    private final int travelCostPlain;
    private final int travelCostWood;
    private final int travelCostMountain;
    private final int travelCostSea;
    private final Image texture1;
    private final Image texture2;
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
        URL urlTexture1 = getClass().getResource(texture1);
        URL urlTexture2 = getClass().getResource(texture2);

        URI uriTexture1 = null;
        URI uriTexture2 = null;

        try {
            uriTexture1 = urlTexture1 == null ? URI.create(texture1) : urlTexture1.toURI();
            uriTexture2 = urlTexture2 == null ? URI.create(texture2) : urlTexture2.toURI();
        } catch (Exception e) {
            LOGGER.error("Error loading texture: {}", e.getMessage());
            this.texture1 = null;
            this.texture2 = null;
            return;
        }

        this.texture1 = new Image(uriTexture1.toString());
        this.texture2 = new Image(uriTexture2.toString());
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
