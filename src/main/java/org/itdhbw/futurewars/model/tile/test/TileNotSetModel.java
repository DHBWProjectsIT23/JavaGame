package org.itdhbw.futurewars.model.tile.test;

import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;

public class TileNotSetModel extends TileModel {

    public TileNotSetModel(int x, int y) {
        super(x, y, TileType.TILE_NOT_SET);
        this.passable = false;
    }
}

