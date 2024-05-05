package org.itdhbw.futurewars.model.unit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TileModel;


public abstract class UnitModel {
    private static final Logger LOGGER = LogManager.getLogger(UnitModel.class);
    public final int modelId = this.hashCode();
    protected final int team;
    private final UnitType unitType;
    private final ObjectProperty<TileModel> currentTile = new SimpleObjectProperty<>();

    protected UnitModel(UnitType unitType, final int team) {
        LOGGER.info("Creating unit model {} for team {} with id: {}", modelId, team, modelId);
        this.team = team;
        this.unitType = unitType;
    }

    public void spawn(TileModel initialTile) {
        LOGGER.info("Spawning unit {} at tile {}", modelId, initialTile.modelId);
        currentTile.set(initialTile);
    }

    public int getTeam() {
        return team;
    }

    //public void moveTo(TileModel newTile) {
    //    if (newTile.isOccupied()) {
    //        LOGGER.error("Tile {} is already occupied!", newTile.modelId);
    //        return;
    //    }
    //    LOGGER.info("Moving unit {} from tile {} to tile {}", modelId, currentTile.get().modelId, newTile.modelId);
    //    int distance = currentTile.get().distanceTo(newTile);
    //    LOGGER.info("Distance: {}", distance);
    //    //this.currentTile.get().removeOccupyingUnit();
    //    //this.currentTile.set(newTile);
    //   //newTile.setOccupyingUnit(this);
    //}

    public ObjectProperty<TileModel> currentTileProperty() {
        return this.currentTile;
    }

    public UnitType getUnitType() {
        return unitType;
    }
}
