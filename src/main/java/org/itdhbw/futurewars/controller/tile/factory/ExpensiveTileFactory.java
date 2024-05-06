package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.ExpensiveTileModel;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.tile.ExpensiveTileView;
import org.itdhbw.futurewars.view.tile.TileView;

public class ExpensiveTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        ExpensiveTileModel tileModel = new ExpensiveTileModel(x, y);
        ExpensiveTileView tileView = new ExpensiveTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
