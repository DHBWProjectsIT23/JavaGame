package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileNotSetModel;
import org.itdhbw.futurewars.view.tile.TileNotSetView;
import org.itdhbw.futurewars.view.tile.TileView;

public class TileNotSetFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        TileNotSetModel tileModel = new TileNotSetModel(x, y);
        TileNotSetView tileView = new TileNotSetView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
