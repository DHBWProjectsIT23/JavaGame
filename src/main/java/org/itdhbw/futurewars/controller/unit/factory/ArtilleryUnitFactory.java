package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.unit.ArtilleryUnitModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.ArtilleryUnitView;
import org.itdhbw.futurewars.view.unit.UnitView;

public class ArtilleryUnitFactory implements UnitFactory {
    @Override
    public Pair<UnitModel, UnitView> createUnit(int team) {
        ArtilleryUnitModel unitModel = new ArtilleryUnitModel(team);
        ArtilleryUnitView unitView = new ArtilleryUnitView(unitModel);
        return new Pair<>(unitModel, unitView);
    }
}
