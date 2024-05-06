package org.itdhbw.futurewars.controller.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.tile.TileView;

public class TileRepository {
    private final static Logger LOGGER = LogManager.getLogger(TileRepository.class);
    private Pair<TileModel, TileView>[][] tiles;

    public TileRepository() {
    }

    public void initializeList(int width, int height) {
        this.tiles = new Pair[width][height];
    }

    public Pair<TileModel, TileView>[][] getAllTiles() {
        return tiles;
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
        LOGGER.info("TileModel at ({}, {})", x, y);
        if (x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[0].length) {
            return null;
        }
        LOGGER.info("TilePair at ({}, {}): {}", x, y, this.tiles[x][y]);
        return this.tiles[x][y].getKey();
    }

    public TileView getTileView(Position position) {
        return this.tiles[position.getX()][position.getY()].getValue();
    }

}
