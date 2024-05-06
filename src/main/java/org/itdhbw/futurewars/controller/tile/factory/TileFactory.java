package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.tile.TileView;

public interface TileFactory {
    Pair<TileModel, TileView> createTile(int x, int y);
}
