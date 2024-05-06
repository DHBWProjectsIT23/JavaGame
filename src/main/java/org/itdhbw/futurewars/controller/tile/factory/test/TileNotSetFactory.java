package org.itdhbw.futurewars.controller.tile.factory.test;

import javafx.util.Pair;
import org.itdhbw.futurewars.controller.tile.factory.TileFactory;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.test.TileNotSetModel;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.tile.test.TileNotSetView;

public class TileNotSetFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        TileNotSetModel tileModel = new TileNotSetModel(x, y);
        TileNotSetView tileView = new TileNotSetView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
