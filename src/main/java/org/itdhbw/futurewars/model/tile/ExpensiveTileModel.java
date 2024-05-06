package org.itdhbw.futurewars.model.tile;

public class ExpensiveTileModel extends TileModel {

    public ExpensiveTileModel(int x, int y) {
        super(x, y, TileType.EXPENSIVE_TILE);
        this.travelCost = 5;
    }
}

