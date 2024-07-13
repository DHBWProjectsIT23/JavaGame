package org.itdhbw.futurewars.game.models.unit;

import javafx.beans.property.*;
import org.itdhbw.futurewars.game.models.tile.MovementType;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.Position;

import java.util.EnumMap;
import java.util.List;
import java.util.logging.Logger;


public class UnitModel {
    private static final Logger LOGGER = Logger.getLogger(UnitModel.class.getSimpleName());
    protected final int team;
    protected final IntegerProperty currentHealthProperty = new SimpleIntegerProperty(10);
    private final ObjectProperty<TileModel> currentTile = new SimpleObjectProperty<>();
    private final BooleanProperty isDead = new SimpleBooleanProperty(false);
    private final BooleanProperty hasMadeAnAction = new SimpleBooleanProperty(false);
    protected int movementRange;
    protected int attackRange;
    protected EnumMap<MovementType, Integer> travelCosts = new EnumMap<>(MovementType.class);
    protected String unitType;
    protected int baseDamage;
    protected double armor;
    protected double piercing;
    protected double lowAirPiercing;
    protected TargetType targetType;
    protected List<TargetType> vulnerableTypes;
    protected int maxHealth = 10;
    private boolean canAttack = false;
    private boolean canMove = false;
    private boolean canMerge = false;

    public UnitModel(String unitType, final int team) {
        LOGGER.info("Creating unit model for unit type: " + unitType);
        this.team = team;
        this.unitType = unitType;
        this.movementRange = 5;
        this.attackRange = 1;
    }

    public boolean canAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public IntegerProperty currentHealthProperty() {
        return currentHealthProperty;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public void setMovementRange(int range) {
        this.movementRange = range;
    }

    public void spawn(TileModel initialTile) {
        LOGGER.info("Unit spawned at " + initialTile.getPosition());
        currentTile.set(initialTile);
    }

    public ObjectProperty<TileModel> currentTileProperty() {
        return this.currentTile;
    }

    public int getTravelCost(MovementType movementType) {
        return travelCosts.get(movementType);
    }

    public boolean canNotTraverse(MovementType movementType) {
        return travelCosts.get(movementType) == -1;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int range) {
        this.attackRange = range;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int damage) {
        this.baseDamage = damage;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getPiercing() {
        return piercing;
    }

    public void setPiercing(double piercing) {
        this.piercing = piercing;
    }

    public double getLowAirPiercing() {
        return lowAirPiercing;
    }

    public void setLowAirPiercing(double lowAirPiercing) {
        this.lowAirPiercing = lowAirPiercing;
    }

    public List<TargetType> getVulnerableTypes() {
        return vulnerableTypes;
    }

    public void setVulnerableTypes(List<TargetType> vulnerableTypes) {
        this.vulnerableTypes = vulnerableTypes;
    }

    public Position getPosition() {
        return currentTile.get().getPosition();
    }

    public void takeDamage(int damage) {
        currentHealthProperty.set(currentHealthProperty.get() - damage);
        if (currentHealthProperty.get() <= 0) {
            die();
        }
    }

    public void die() {
        LOGGER.info("Unit died");
        isDead.set(true);
    }

    public BooleanProperty isDeadProperty() {
        return isDead;
    }

    public boolean isDead() {
        return isDead.get();
    }

    public void setHasMadeAnAction(boolean b) {
        hasMadeAnAction.set(b);
    }

    public boolean hasMadeAnAction() {
        return hasMadeAnAction.get();
    }

    public BooleanProperty hasMadeAnActionProperty() {
        return hasMadeAnAction;
    }

    public boolean canMove() {
        return canMove;
    }

    public boolean canMerge() {
        return canMerge;
    }

    public void setCanMerge(boolean canMerge) {
        this.canMerge = canMerge;
    }

    public void setMountainTravelCost(int cost) {
        this.travelCosts.put(MovementType.MOUNTAIN, cost);
    }

    public void setSeaTravelCost(int cost) {
        this.travelCosts.put(MovementType.SEA, cost);
    }

    public void setPlainTravelCost(int cost) {
        this.travelCosts.put(MovementType.PLAIN, cost);
    }

    public void setWoodsTravelCost(int cost) {
        this.travelCosts.put(MovementType.WOODS, cost);
    }

    public void mergeInto(UnitModel unitModel) {
        if (!this.canMergeWith(unitModel)) {
            return;
        }
        int newHealth = unitModel.getCurrentHealth() + this.getCurrentHealth();
        if (newHealth > this.maxHealth) {
            newHealth = this.maxHealth;
        }
        unitModel.setCurrentHealth(newHealth);
        unitModel.setCanMove(false);
        this.die();
    }

    public boolean canMergeWith(UnitModel unitModel) {
        return unitModel.getTeam() == this.team && unitModel.getUnitType().equals(this.unitType) && this != unitModel;
    }

    public int getCurrentHealth() {
        return currentHealthProperty.get();
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public int getTeam() {
        return team;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setCurrentHealth(int newHealth) {
        currentHealthProperty.set(newHealth);
    }

    public boolean canAttackUnit(UnitModel unitModel) {
        return vulnerableTypes.contains(unitModel.getTargetType());
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public boolean isInRange(TileModel tile) {
        return currentTile.get().getPosition().calculateDistance(tile.getPosition()) <= movementRange;
    }
}

