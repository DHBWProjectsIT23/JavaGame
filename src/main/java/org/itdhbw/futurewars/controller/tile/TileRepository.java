package org.itdhbw.futurewars.controller.tile;

import javafx.util.Pair;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.util.Constants;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.tile.TileView;

public class TileRepository {
    private final Pair<TileModel, TileView>[][] tiles;

    public TileRepository() {
        this.tiles = new Pair[Constants.MAP_COLUMNS][Constants.MAP_ROWS];
    }

    public void addTile(Pair<TileModel, TileView> tilePair) {
        this.tiles[tilePair.getKey().getPosition().getX()][tilePair.getKey().getPosition().getY()] = tilePair;
    }

    public void addTile(TileModel tileModel, TileView tileView) {
        this.tiles[tileModel.getPosition().getX()][tileModel.getPosition().getY()] = new Pair<>(tileModel, tileView);
    }

    public Pair<TileModel, TileView> getTile(Position position) {
        return this.tiles[position.getX()][position.getY()];
    }

    public TileModel getTileModel(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[0].length) {
            return null;
        }
        return this.tiles[x][y].getKey();
    }

    public TileView getTileView(Position position) {
        return this.tiles[position.getX()][position.getY()].getValue();
    }
}