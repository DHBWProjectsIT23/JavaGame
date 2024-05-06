package org.itdhbw.futurewars.model.unit;

import org.itdhbw.futurewars.model.tile.TileType;

import java.util.Set;

public class ArtilleryUnitModel extends UnitModel {
    public ArtilleryUnitModel(int team) {
        super(UnitType.ARTILLERY_UNIT, team);
        this.traversableTiles = Set.of(TileType.PLAIN_TILE, TileType.WOOD_TILE, TileType.MOUNTAIN_TILE);
    }
}

