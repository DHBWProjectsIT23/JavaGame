package org.itdhbw.futurewars.model.unit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.util.Position;

import java.util.EnumMap;


public class UnitModel {
    private static final Logger LOGGER = LogManager.getLogger(UnitModel.class);
    public final int modelId = this.hashCode();
    protected final int team;
    private final ObjectProperty<TileModel> currentTile = new SimpleObjectProperty<>();
    protected int movementRange;
    protected int attackRange;
    protected EnumMap<TileType, Integer> travelCosts = new EnumMap<>(TileType.class);
    protected String unitType;
    protected int maxHealth = 10;
    protected int currentHealth = 10;
    private boolean hasMoved = false;

    public UnitModel(String unitType, final int team) {
        LOGGER.info("Creating unit model {} for team {} with id: {}", modelId, team, modelId);
        this.team = team;
        this.travelCosts.put(TileType.TEST_TILE, 1);
        this.travelCosts.put(TileType.EXPENSIVE_TILE, 1);
        this.travelCosts.put(TileType.UNPASSABLE_TILE, -1);
        this.travelCosts.put(TileType.TILE_NOT_SET, -1);
        this.unitType = unitType;
        this.movementRange = 5;
        this.attackRange = 1;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public void setMovementRange(int range) {
        this.movementRange = range;
    }

    public void spawn(TileModel initialTile) {
        LOGGER.info("Spawning unit {} at tile {}", modelId, initialTile.modelId);
        currentTile.set(initialTile);
    }

    public int getTeam() {
        return team;
    }

    public ObjectProperty<TileModel> currentTileProperty() {
        return this.currentTile;
    }

    public int getTravelCost(TileType tileType) {
        return travelCosts.get(tileType);
    }

    public boolean canNotTraverse(TileType tileType) {
        return travelCosts.get(tileType) == -1;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int range) {
        this.attackRange = range;
    }

    public Position getPosition() {
        return currentTile.get().getPosition();
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            //die();
        }
    }

    public void setHasMoved(boolean b) {
        hasMoved = b;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public String getUnitType() {
        return unitType;
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

    public void setNameTyoe(String nameType) {
        this.unitType = nameType;
    }

    public void debugLog() {
        LOGGER.info("Unit model {} for team {} with id: {}", modelId, team, modelId);
        LOGGER.info("Movement range: {}", movementRange);
        LOGGER.info("Attack range: {}", attackRange);
        LOGGER.info("Travel costs: {}", travelCosts);
    }
}

