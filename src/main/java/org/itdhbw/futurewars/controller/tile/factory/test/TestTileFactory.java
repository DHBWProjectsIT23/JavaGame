package org.itdhbw.futurewars.controller.tile.factory.test;

import javafx.util.Pair;
import org.itdhbw.futurewars.controller.tile.factory.TileFactory;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.test.TestTileModel;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.tile.test.TestTileView;

public class TestTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        TestTileModel tileModel = new TestTileModel(x, y);
        TestTileView tileView = new TestTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
