package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.unit.TestUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.TestUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

public class TestUnitFactory implements UnitFactory {
    @Override
    public Pair<UnitModel, UnitView> createUnit(int team) {
        TestUnitModel unitModel = new TestUnitModel(team);
        TestUnitView unitView = new TestUnitView(unitModel);
        return new Pair<>(unitModel, unitView);
    }
}
