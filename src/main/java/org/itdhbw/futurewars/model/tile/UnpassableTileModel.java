package org.itdhbw.futurewars.model.tile;

public class UnpassableTileModel extends TileModel {


    public UnpassableTileModel(int x, int y) {
        super(x, y, TileType.UNPASSABLE_TILE);
        this.passable = false;
    }

}

