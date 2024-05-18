package org.itdhbw.futurewars.game.controllers.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.views.UnitView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Unit repository.
 */
public class UnitRepository {
    private final static Logger LOGGER =
            LogManager.getLogger(UnitRepository.class);
    private final Map<UnitModel, UnitView> units;
    private final List<String> unitTypes;

    /**
     * Instantiates a new Unit repository.
     */
    public UnitRepository() {
        this.units = new HashMap<>();
        this.unitTypes = new ArrayList<>();
    }

    /**
     * Gets all units.
     *
     * @return the all units
     */
    public Map<UnitModel, UnitView> getAllUnits() {
        return units;
    }

    /**
     * Gets active units.
     *
     * @return the active units
     */
    public List<UnitModel> getActiveUnits() {
        List<UnitModel> activeUnits = new ArrayList<>();
        for (UnitModel unit : units.keySet()) {
            if (unit.getTeam() == Context.getGameState().getCurrentPlayer()) {
                activeUnits.add(unit);
            }
        }
        return activeUnits;
    }

    /**
     * Add unit.
     *
     * @param tilePair the tile pair
     */
    public void addUnit(Pair<UnitModel, UnitView> tilePair) {
        this.units.put(tilePair.getKey(), tilePair.getValue());
    }

    /**
     * Add unit.
     *
     * @param tileModel the tile model
     * @param tileView  the tile view
     */
    public void addUnit(UnitModel tileModel, UnitView tileView) {
        this.units.put(tileModel, tileView);
    }

    /**
     * Gets unit view.
     *
     * @param unitModel the unit model
     * @return the unit view
     */
    public UnitView getUnitView(UnitModel unitModel) {
        return units.get(unitModel);
    }

    /**
     * Add unit type.
     *
     * @param type the type
     */
    public void addUnitType(String type) {
        this.unitTypes.add(type);
    }

    /**
     * Gets unit types.
     *
     * @return the unit types
     */
    public List<String> getUnitTypes() {
        return this.unitTypes;
    }

}
