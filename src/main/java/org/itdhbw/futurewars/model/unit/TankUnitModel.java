package org.itdhbw.futurewars.model.unit;

import org.itdhbw.futurewars.model.tile.TileType;

import java.util.Set;

public class TankUnitModel extends UnitModel {
    public TankUnitModel(int team) {
        super(UnitType.TANK_UNIT, team);
        this.traversableTiles = Set.of(TileType.PLAIN_TILE, TileType.WOOD_TILE);
    }
}

