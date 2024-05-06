package org.itdhbw.futurewars.controller.unit.factory.test;

import javafx.util.Pair;
import org.itdhbw.futurewars.controller.unit.factory.UnitFactory;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.test.TestUnitModel;
import org.itdhbw.futurewars.view.unit.UnitView;
import org.itdhbw.futurewars.view.unit.test.TestUnitView;

public class TestUnitFactory implements UnitFactory {
    @Override
    public Pair<UnitModel, UnitView> createUnit(int team) {
        TestUnitModel unitModel = new TestUnitModel(team);
        TestUnitView unitView = new TestUnitView(unitModel);
        return new Pair<>(unitModel, unitView);
    }
}
