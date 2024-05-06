package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.PlainTileModel;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.tile.PlainTileView;
import org.itdhbw.futurewars.view.tile.TileView;

public class PlainTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        PlainTileModel tileModel = new PlainTileModel(x, y);
        PlainTileView tileView = new PlainTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
