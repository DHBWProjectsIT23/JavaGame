package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.MountainTileModel;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.tile.MountainTileView;
import org.itdhbw.futurewars.view.tile.TileView;

public class MountainTileFactory implements TileFactory {
    @Override
    public Pair<TileModel, TileView> createTile(int x, int y) {
        MountainTileModel tileModel = new MountainTileModel(x, y);
        MountainTileView tileView = new MountainTileView(tileModel);
        return new Pair<>(tileModel, tileView);
    }
}
