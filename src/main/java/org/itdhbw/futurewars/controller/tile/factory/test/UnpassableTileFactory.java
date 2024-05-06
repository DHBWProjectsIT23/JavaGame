package org.itdhbw.futurewars.controller.tile.factory.test;

import javafx.util.Pair;
import org.itdhbw.futurewars.controller.tile.factory.TileFactory;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.test.UnpassableTileModel;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.tile.test.UnpassableTileView;

public class UnpassableTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        UnpassableTileModel tileModel = new UnpassableTileModel(x, y);
        UnpassableTileView tileView = new UnpassableTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
