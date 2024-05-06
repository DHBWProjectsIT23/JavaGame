package org.itdhbw.futurewars.model.unit;

import org.itdhbw.futurewars.model.tile.TileType;

import java.util.Set;

public class BattleCopterUnitModel extends UnitModel {
    public BattleCopterUnitModel(int team) {
        super(UnitType.BATTLE_COPTER_UNIT, team);
        this.traversableTiles = Set.of(TileType.PLAIN_TILE, TileType.WOOD_TILE, TileType.MOUNTAIN_TILE, TileType.SEA_TILE);
    }
}

