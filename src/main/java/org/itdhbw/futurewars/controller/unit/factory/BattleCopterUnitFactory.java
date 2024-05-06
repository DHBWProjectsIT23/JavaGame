package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.unit.BattleCopterUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.BattleCopterUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

public class BattleCopterUnitFactory implements UnitFactory {
    @Override
    public Pair<UnitModel, UnitView> createUnit(int team) {
        BattleCopterUnitModel unitModel = new BattleCopterUnitModel(team);
        BattleCopterUnitView unitView = new BattleCopterUnitView(unitModel);
        return new Pair<>(unitModel, unitView);
    }
}
