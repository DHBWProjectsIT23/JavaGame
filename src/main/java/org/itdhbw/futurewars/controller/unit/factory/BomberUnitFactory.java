package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.unit.BomberUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.BomberUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

public class BomberUnitFactory implements UnitFactory {
    @Override
    public Pair<UnitModel, UnitView> createUnit(int team) {
        BomberUnitModel unitModel = new BomberUnitModel(team);
        BomberUnitView unitView = new BomberUnitView(unitModel);
        return new Pair<>(unitModel, unitView);
    }
}
