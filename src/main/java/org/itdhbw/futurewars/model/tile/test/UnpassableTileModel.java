package org.itdhbw.futurewars.model.tile.test;

import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;

public class UnpassableTileModel extends TileModel {


    public UnpassableTileModel(int x, int y) {
        super(x, y, TileType.UNPASSABLE_TILE);
    }

}

