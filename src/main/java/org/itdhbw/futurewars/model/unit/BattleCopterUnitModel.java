package org.itdhbw.futurewars.model.unit;

import org.itdhbw.futurewars.model.tile.TileType;

public class BattleCopterUnitModel extends UnitModel {
    public BattleCopterUnitModel(int team) {
        super(UnitType.BATTLE_COPTER_UNIT, team);
        this.travelCosts.put(TileType.MOUNTAIN_TILE, 4);
        this.travelCosts.put(TileType.SEA_TILE, 1);
        this.travelCosts.put(TileType.PLAIN_TILE, 1);
        this.travelCosts.put(TileType.WOOD_TILE, 1);
        this.travelCosts.put(TileType.TEST_TILE, 1);
        this.travelCosts.put(TileType.EXPENSIVE_TILE, 1);
        this.travelCosts.put(TileType.UNPASSABLE_TILE, -1);
        this.travelCosts.put(TileType.TILE_NOT_SET, -1);
        this.movementRange = 7;
        this.attackRange = 2;
    }
}

