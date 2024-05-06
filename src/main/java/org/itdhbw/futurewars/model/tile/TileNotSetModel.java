package org.itdhbw.futurewars.model.tile;

public class TileNotSetModel extends TileModel {

    public TileNotSetModel(int x, int y) {
        super(x, y, TileType.TILE_NOT_SET);
        this.passable = false;
    }
}

