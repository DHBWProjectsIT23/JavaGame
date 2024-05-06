package org.itdhbw.futurewars.model.unit;

import org.itdhbw.futurewars.model.tile.TileType;

import java.util.Set;

public class MechanizedUnitModel extends UnitModel {
    public MechanizedUnitModel(int team) {
        super(UnitType.MECHANIZED_UNIT, team);
        this.traversableTiles = Set.of(TileType.PLAIN_TILE, TileType.WOOD_TILE);
    }
}

