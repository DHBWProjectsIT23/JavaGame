package org.itdhbw.futurewars.model.tile.test;

import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;

public class ExpensiveTileModel extends TileModel {

    public ExpensiveTileModel(int x, int y) {
        super(x, y, TileType.EXPENSIVE_TILE);
        this.travelCost = 5;
    }
}

