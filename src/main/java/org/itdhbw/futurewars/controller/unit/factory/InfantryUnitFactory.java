package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.unit.InfantryUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.InfantryUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

public class InfantryUnitFactory implements UnitFactory {
    @Override
    public Pair<UnitModel, UnitView> createUnit(int team) {
        InfantryUnitModel unitModel = new InfantryUnitModel(team);
        InfantryUnitView unitView = new InfantryUnitView(unitModel);
        return new Pair<>(unitModel, unitView);
    }
}
