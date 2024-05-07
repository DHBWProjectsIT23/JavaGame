package org.itdhbw.futurewars.model.unit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;

import java.util.EnumMap;


public abstract class UnitModel {
    private static final Logger LOGGER = LogManager.getLogger(UnitModel.class);
    public final int modelId = this.hashCode();
    protected final int team;
    private final UnitType unitType;
    private final ObjectProperty<TileModel> currentTile = new SimpleObjectProperty<>();
    protected int movementRange;
    protected int attackRange;
    protected EnumMap<TileType, Integer> travelCosts = new EnumMap<>(TileType.class);

    protected UnitModel(UnitType unitType, final int team) {
        LOGGER.info("Creating unit model {} for team {} with id: {}", modelId, team, modelId);
        this.team = team;
        this.unitType = unitType;
    }

    public int getMovementRange() {
        return movementRange;
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

    public UnitType getUnitType() {
        return unitType;
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
}
