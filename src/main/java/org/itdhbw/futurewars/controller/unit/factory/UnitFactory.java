package org.itdhbw.futurewars.controller.unit.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.view.unit.UnitView;

public interface UnitFactory {
    Pair<UnitModel, UnitView> createUnit(int team);
}
