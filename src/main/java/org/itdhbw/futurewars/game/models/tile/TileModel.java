package org.itdhbw.futurewars.game.models.tile;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Position;

public class TileModel {
    private static final Logger LOGGER = LogManager.getLogger(TileModel.class);
    public final int modelId = this.hashCode();
    private final Position position;
    private final ObjectProperty<UnitModel> occupyingUnit =
            new SimpleObjectProperty<>();
    private final BooleanProperty partOfPath = new SimpleBooleanProperty(false);
    private final BooleanProperty partOfPossiblePath =
            new SimpleBooleanProperty(false);
    protected MovementType movementType;
    private boolean isOccupied = false;
    private final String tileType;
    private final int terrainCover;


    public TileModel(String tileType, final int x, final int y, int terrainCover) {
        LOGGER.info("Creating tile model {} at position ({}, {}) with type {}",
                    modelId, x, y, tileType);
        this.tileType = tileType;
        this.position = new Position(x, y, true);
        this.terrainCover = terrainCover;
    }

    public int distanceTo(TileModel other) {
        return this.position.calculateDistance(other.position);
    }

    public Position getPosition() {
        return position;
    }

    public int getTerrainCover() {
        return terrainCover;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void removeOccupyingUnit() {
        LOGGER.info("Removing unit from tile {}", modelId);
        this.isOccupied = false;
        this.occupyingUnit.set(null);
    }

    public ObjectProperty<UnitModel> occupyingUnitProperty() {
        return occupyingUnit;
    }

    public UnitModel getOccupyingUnit() {
        if (isOccupied) {
            return occupyingUnit.get();
        }
        throw new IllegalStateException("Tile is not occupied!");
    }

    public void setOccupyingUnit(UnitModel unitModel) {
        LOGGER.info("Setting unit {} on tile {}", unitModel.modelId, modelId);
        this.occupyingUnit.set(unitModel);
        this.isOccupied = true;
    }

    public BooleanProperty partOfPathProperty() {
        return partOfPath;
    }

    public boolean getPartOfPath() {
        return partOfPath.get();
    }

    public void setPartOfPath(boolean partOfPath) {
        this.partOfPath.set(partOfPath);
    }

    public BooleanProperty partOfPossiblePathProperty() {
        return partOfPossiblePath;
    }

    public String getTileType() {
        return tileType;
    }
}
