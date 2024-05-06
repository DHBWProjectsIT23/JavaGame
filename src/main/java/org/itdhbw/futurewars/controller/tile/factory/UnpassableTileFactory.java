package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.UnpassableTileModel;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.tile.UnpassableTileView;

public class UnpassableTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        UnpassableTileModel tileModel = new UnpassableTileModel(x, y);
        UnpassableTileView tileView = new UnpassableTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
