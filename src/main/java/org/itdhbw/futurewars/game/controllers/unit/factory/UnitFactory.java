package org.itdhbw.futurewars.game.controllers.unit.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.views.UnitView;

import java.net.URI;

/**
 * The type Unit factory.
 */
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
    private UnitModel unitModel;
    private UnitView unitView;

    /**
     * Instantiates a new Unit factory.
     *
     * @param unitType           the unit type
     * @param attackRange        the attack range
     * @param movementRange      the movement range
     * @param travelCostPlain    the travel cost plain
     * @param travelCostWood     the travel cost wood
     * @param travelCostMountain the travel cost mountain
     * @param travelCostSea      the travel cost sea
     * @param texture1           the texture 1
     * @param texture2           the texture 2
     */
    public UnitFactory(String unitType, int attackRange, int movementRange, int travelCostPlain, int travelCostWood, int travelCostMountain, int travelCostSea, URI texture1, URI texture2) {
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
    }

    private void createUnitView() {
        LOGGER.info("Creating unit view");
        unitView = new UnitView(unitModel);
        unitView.setTexture(texture1, texture2);
    }

    /**
     * Create unit pair.
     *
     * @param team the team
     * @return the pair
     */
    public Pair<UnitModel, UnitView> createUnit(int team) {
        createUnitModel(team);
        createUnitView();
        return new Pair<>(unitModel, unitView);
    }

    /**
     * Gets unit textures.
     *
     * @return the unit textures
     */
    public Pair<Image, Image> getUnitTextures() {
        return new Pair<>(texture1, texture2);
    }
}
