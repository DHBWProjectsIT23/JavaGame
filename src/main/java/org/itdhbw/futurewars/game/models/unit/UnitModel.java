package org.itdhbw.futurewars.game.models.unit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.tile.MovementType;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.Position;

import java.util.EnumMap;


/**
 * The type Unit model.
 */
public class UnitModel {
    private static final Logger LOGGER = LogManager.getLogger(UnitModel.class);
    /**
     * The Model id.
     */
    public final int modelId = this.hashCode();
    /**
     * The Team.
     */
    protected final int team;
    private final ObjectProperty<TileModel> currentTile =
            new SimpleObjectProperty<>();
    /**
     * The Movement range.
     */
    protected int movementRange;
    /**
     * The Attack range.
     */
    protected int attackRange;
    /**
     * The Travel costs.
     */
    protected EnumMap<MovementType, Integer> travelCosts =
            new EnumMap<>(MovementType.class);
    /**
     * The Unit type.
     */
    protected String unitType;
    /**
     * The Max health.
     */
    protected int maxHealth = 10;
    /**
     * The Current health.
     */
    protected int currentHealth = 10;
    private boolean hasMoved = false;

    /**
     * Instantiates a new Unit model.
     *
     * @param unitType the unit type
     * @param team     the team
     */
    public UnitModel(String unitType, final int team) {
        LOGGER.info("Creating unit model {} for team {} with id: {}", modelId,
                    team, modelId);
        this.team = team;
        this.unitType = unitType;
        this.movementRange = 5;
        this.attackRange = 1;
    }

    /**
     * Gets max health.
     *
     * @return the max health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets current health.
     *
     * @return the current health
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets movement range.
     *
     * @return the movement range
     */
    public int getMovementRange() {
        return movementRange;
    }

    /**
     * Sets movement range.
     *
     * @param range the range
     */
    public void setMovementRange(int range) {
        this.movementRange = range;
    }

    /**
     * Spawn.
     *
     * @param initialTile the initial tile
     */
    public void spawn(TileModel initialTile) {
        LOGGER.info("Spawning unit {} at tile {}", modelId,
                    initialTile.modelId);
        currentTile.set(initialTile);
    }

    /**
     * Gets team.
     *
     * @return the team
     */
    public int getTeam() {
        return team;
    }

    /**
     * Current tile property object property.
     *
     * @return the object property
     */
    public ObjectProperty<TileModel> currentTileProperty() {
        return this.currentTile;
    }

    /**
     * Gets travel cost.
     *
     * @param movementType the movement type
     * @return the travel cost
     */
    public int getTravelCost(MovementType movementType) {
        LOGGER.info("Getting travel cost for movement type: {}", movementType);
        LOGGER.info("Travel costs: {}", travelCosts);
        return travelCosts.get(movementType);
    }

    /**
     * Can not traverse boolean.
     *
     * @param movementType the movement type
     * @return the boolean
     */
    public boolean canNotTraverse(MovementType movementType) {
        return travelCosts.get(movementType) == -1;
    }

    /**
     * Gets attack range.
     *
     * @return the attack range
     */
    public int getAttackRange() {
        return attackRange;
    }

    /**
     * Sets attack range.
     *
     * @param range the range
     */
    public void setAttackRange(int range) {
        this.attackRange = range;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Position getPosition() {
        return currentTile.get().getPosition();
    }

    /**
     * Take damage.
     *
     * @param damage the damage
     */
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            //die();
        }
    }

    /**
     * Sets has moved.
     *
     * @param b the b
     */
    public void setHasMoved(boolean b) {
        hasMoved = b;
    }

    /**
     * Has moved boolean.
     *
     * @return the boolean
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * Gets unit type.
     *
     * @return the unit type
     */
    public String getUnitType() {
        return unitType;
    }

    /**
     * Sets mountain travel cost.
     *
     * @param cost the cost
     */
    public void setMountainTravelCost(int cost) {
        this.travelCosts.put(MovementType.MOUNTAIN, cost);
    }

    /**
     * Sets sea travel cost.
     *
     * @param cost the cost
     */
    public void setSeaTravelCost(int cost) {
        this.travelCosts.put(MovementType.SEA, cost);
    }

    /**
     * Sets plain travel cost.
     *
     * @param cost the cost
     */
    public void setPlainTravelCost(int cost) {
        this.travelCosts.put(MovementType.PLAIN, cost);
    }

    /**
     * Sets woods travel cost.
     *
     * @param cost the cost
     */
    public void setWoodsTravelCost(int cost) {
        this.travelCosts.put(MovementType.WOODS, cost);
    }
}

