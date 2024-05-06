package org.itdhbw.futurewars.model.unit;

import org.itdhbw.futurewars.model.tile.TileType;

import java.util.Set;

public class InfantryUnitModel extends UnitModel {
    public InfantryUnitModel(int team) {
        super(UnitType.INFANTRY_UNIT, team);
        this.traversableTiles = Set.of(TileType.PLAIN_TILE, TileType.WOOD_TILE, TileType.MOUNTAIN_TILE);

    }
}

