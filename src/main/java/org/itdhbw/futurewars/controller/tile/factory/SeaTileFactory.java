package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.SeaTileModel;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.tile.SeaTileView;
import org.itdhbw.futurewars.view.tile.TileView;

public class SeaTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        SeaTileModel tileModel = new SeaTileModel(x, y);
        SeaTileView tileView = new SeaTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
