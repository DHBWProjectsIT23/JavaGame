package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.TestTileModel;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.tile.TestTileView;
import org.itdhbw.futurewars.view.tile.TileView;

public class TestTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        TestTileModel tileModel = new TestTileModel(x, y);
        TestTileView tileView = new TestTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
