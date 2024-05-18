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

public class UnitRepository {
    private final static Logger LOGGER = LogManager.getLogger(UnitRepository.class);
    private final Map<UnitModel, UnitView> units;
    private final List<String> unitTypes;

    public UnitRepository() {
        this.units = new HashMap<>();
        this.unitTypes = new ArrayList<>();
    }

    public Map<UnitModel, UnitView> getAllUnits() {
        return units;
    }

    public List<UnitModel> getActiveUnits() {
        List<UnitModel> activeUnits = new ArrayList<>();
        for (UnitModel unit : units.keySet()) {
            if (unit.getTeam() == Context.getGameState().getCurrentPlayer()) {
                activeUnits.add(unit);
            }
        }
        return activeUnits;
    }

    public void addUnit(Pair<UnitModel, UnitView> tilePair) {
        this.units.put(tilePair.getKey(), tilePair.getValue());
    }

    public void addUnit(UnitModel tileModel, UnitView tileView) {
        this.units.put(tileModel, tileView);
    }

    public UnitView getUnitView(UnitModel unitModel) {
        return units.get(unitModel);
    }

    public void addUnitType(String type) {
        this.unitTypes.add(type);
    }

    public List<String> getUnitTypes() {
        return this.unitTypes;
    }

}
