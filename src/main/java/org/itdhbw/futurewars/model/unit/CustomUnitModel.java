package org.itdhbw.futurewars.model.unit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TileType;

public class CustomUnitModel extends UnitModel {
    private static final Logger LOGGER = LogManager.getLogger(CustomUnitModel.class);

    public CustomUnitModel(UnitType unitType, int team) {
        super(unitType, team);
        this.travelCosts.put(TileType.TEST_TILE, 1);
        this.travelCosts.put(TileType.EXPENSIVE_TILE, 1);
        this.travelCosts.put(TileType.UNPASSABLE_TILE, -1);
        this.travelCosts.put(TileType.TILE_NOT_SET, -1);
        this.movementRange = 5;
        this.attackRange = 1;
    }

    public void setMountainTileTravelCost(int cost) {
        this.travelCosts.put(TileType.MOUNTAIN_TILE, cost);
    }

    public void setSeaTileTravelCost(int cost) {
        this.travelCosts.put(TileType.SEA_TILE, cost);
    }

    public void setPlainTileTravelCost(int cost) {
        this.travelCosts.put(TileType.PLAIN_TILE, cost);
    }

    public void setWoodTileTravelCost(int cost) {
        this.travelCosts.put(TileType.WOOD_TILE, cost);
    }

    public void setMovementRange(int range) {
        this.movementRange = range;
    }

    public void setAttackRange(int range) {
        this.attackRange = range;
    }

    public void debugLog() {
        LOGGER.info("Unit model {} for team {} with id: {}", modelId, team, modelId);
        LOGGER.info("Movement range: {}", movementRange);
        LOGGER.info("Attack range: {}", attackRange);
        LOGGER.info("Travel costs: {}", travelCosts);
    }

}
