package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.UnitView;

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
