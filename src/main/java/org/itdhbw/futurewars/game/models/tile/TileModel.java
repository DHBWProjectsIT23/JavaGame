package org.itdhbw.futurewars.game.models.tile;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Position;

/**
 * The type Tile model.
 */
public class TileModel {
    private static final Logger LOGGER = LogManager.getLogger(TileModel.class);
    /**
     * The Model id.
     */
    public final int modelId = this.hashCode();
    private final Position position;
    private final ObjectProperty<UnitModel> occupyingUnit =
            new SimpleObjectProperty<>();
    private final BooleanProperty partOfPath = new SimpleBooleanProperty(false);
    private final BooleanProperty partOfPossiblePath =
            new SimpleBooleanProperty(false);
    /**
     * The Movement type.
     */
    protected MovementType movementType;
    private boolean isOccupied = false;

    /**
     * Instantiates a new Tile model.
     *
     * @param tileType the tile type
     * @param x        the x
     * @param y        the y
     */
    public TileModel(String tileType, final int x, final int y) {
        LOGGER.info("Creating tile model {} at position ({}, {}) with type {}",
                    modelId, x, y, tileType);
        this.position = new Position(x, y, true);
    }

    /**
     * Distance to int.
     *
     * @param other the other
     * @return the int
     */
    public int distanceTo(TileModel other) {
        return this.position.calculateDistance(other.position);
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets movement type.
     *
     * @return the movement type
     */
    public MovementType getMovementType() {
        return movementType;
    }

    /**
     * Sets movement type.
     *
     * @param movementType the movement type
     */
    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    /**
     * Is occupied boolean.
     *
     * @return the boolean
     */
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * Remove occupying unit.
     */
    public void removeOccupyingUnit() {
        LOGGER.info("Removing unit from tile {}", modelId);
        this.isOccupied = false;
        this.occupyingUnit.set(null);
    }

    /**
     * Occupying unit property object property.
     *
     * @return the object property
     */
    public ObjectProperty<UnitModel> occupyingUnitProperty() {
        return occupyingUnit;
    }

    /**
     * Gets occupying unit.
     *
     * @return the occupying unit
     */
    public UnitModel getOccupyingUnit() {
        if (isOccupied) {
            return occupyingUnit.get();
        }
        throw new IllegalStateException("Tile is not occupied!");
    }

    /**
     * Sets occupying unit.
     *
     * @param unitModel the unit model
     */
    public void setOccupyingUnit(UnitModel unitModel) {
        LOGGER.info("Setting unit {} on tile {}", unitModel.modelId, modelId);
        this.occupyingUnit.set(unitModel);
        this.isOccupied = true;
    }

    /**
     * Part of path property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty partOfPathProperty() {
        return partOfPath;
    }

    /**
     * Gets part of path.
     *
     * @return the part of path
     */
    public boolean getPartOfPath() {
        return partOfPath.get();
    }

    /**
     * Sets part of path.
     *
     * @param partOfPath the part of path
     */
    public void setPartOfPath(boolean partOfPath) {
        this.partOfPath.set(partOfPath);
    }

    /**
     * Part of possible path property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty partOfPossiblePathProperty() {
        return partOfPossiblePath;
    }
}
