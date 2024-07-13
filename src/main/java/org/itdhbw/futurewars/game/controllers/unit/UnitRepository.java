package org.itdhbw.futurewars.game.controllers.unit;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.views.UnitView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitRepository {
    private final Map<UnitModel, UnitView> units;
    private final IntegerProperty team1UnitCount = new SimpleIntegerProperty(0);
    private final IntegerProperty team2UnitCount = new SimpleIntegerProperty(0);
    private final List<String> unitTypes;

    public UnitRepository() {
        this.units = new HashMap<>();
        this.unitTypes = new ArrayList<>();
    }

    public int getUnitCount() {
        return unitTypes.size();
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

    public void addUnit(Pair<UnitModel, UnitView> unitPair) {
        UnitModel unitModel = unitPair.getKey();

        unitModel.isDeadProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                units.remove(unitModel);
                if (unitModel.getTeam() == 1) {
                    team1UnitCount.set(team1UnitCount.get() - 1);
                } else {
                    team2UnitCount.set(team2UnitCount.get() - 1);
                }
            }
        });

        if (unitModel.getTeam() == 1) {
            team1UnitCount.set(team1UnitCount.get() + 1);
        } else {
            team2UnitCount.set(team2UnitCount.get() + 1);
        }

        this.units.put(unitPair.getKey(), unitPair.getValue());
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

    public IntegerProperty team1UnitCountProperty() {
        return team1UnitCount;
    }

    public IntegerProperty team2UnitCountProperty() {
        return team2UnitCount;
    }

    public void reset() {
        units.clear();
        team1UnitCount.set(0);
        team2UnitCount.set(0);
    }

}
