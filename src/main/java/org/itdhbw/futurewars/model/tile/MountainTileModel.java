package org.itdhbw.futurewars.model.tile;

public class MountainTileModel extends TileModel {

    public MountainTileModel(int x, int y) {
        super(x, y, TileType.MOUNTAIN_TILE);
        this.travelCost = 5;
    }
}

