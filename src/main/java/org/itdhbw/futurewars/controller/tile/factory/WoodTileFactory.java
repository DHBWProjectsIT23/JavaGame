package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.WoodTileModel;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.tile.WoodTileView;

public class WoodTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        WoodTileModel tileModel = new WoodTileModel(x, y);
        WoodTileView tileView = new WoodTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
