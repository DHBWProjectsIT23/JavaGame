package org.itdhbw.futurewars.model.unit;

import org.itdhbw.futurewars.model.tile.TileType;

public class ArtilleryUnitModel extends UnitModel {
    public ArtilleryUnitModel(int team) {
        super(UnitType.ARTILLERY_UNIT, team);
        this.travelCosts.put(TileType.MOUNTAIN_TILE, 2);
        this.travelCosts.put(TileType.SEA_TILE, -1);
        this.travelCosts.put(TileType.PLAIN_TILE, 1);
        this.travelCosts.put(TileType.WOOD_TILE, 1);
        this.travelCosts.put(TileType.TEST_TILE, 1);
        this.travelCosts.put(TileType.EXPENSIVE_TILE, 1);
        this.travelCosts.put(TileType.UNPASSABLE_TILE, -1);
        this.travelCosts.put(TileType.TILE_NOT_SET, -1);
        this.movementRange = 2;
        this.attackRange = 8;

        this.nameType = "ArtilleryUnit";
    }
}

