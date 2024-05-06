package org.itdhbw.futurewars.model.tile;

public class SeaTileModel extends TileModel {

    public SeaTileModel(int x, int y) {
        super(x, y, TileType.SEA_TILE);
        this.passable = false;
    }
}

