package org.itdhbw.futurewars.model.unit.test;

import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;

public class TestUnitModel extends UnitModel {
    public TestUnitModel(int team) {
        super(UnitType.TEST_UNIT, team);
    }
}

