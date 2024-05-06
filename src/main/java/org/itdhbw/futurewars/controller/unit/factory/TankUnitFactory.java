package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.unit.TankUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.TankUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

public class TankUnitFactory implements UnitFactory {
    @Override
    public Pair<UnitModel, UnitView> createUnit(int team) {
        TankUnitModel unitModel = new TankUnitModel(team);
        TankUnitView unitView = new TankUnitView(unitModel);
        return new Pair<>(unitModel, unitView);
    }
}
