package org.itdhbw.futurewars.controller.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.TileView;

import java.util.ArrayList;
import java.util.List;

public class TileRepository {
    private final static Logger LOGGER = LogManager.getLogger(TileRepository.class);
    List<String> tileTypes;
    private Pair<TileModel, TileView>[][] tiles;

    public TileRepository() {
        this.tileTypes = new ArrayList<>();
    }

    public void addTileType(String type) {
        this.tileTypes.add(type);
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

    public Pair<TileModel, TileView> getTile(int x, int y) {
        return this.tiles[x][y];
    }

    public TileModel getTileModel(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (x < 0 || x >= this.tiles.length || y < 0 || y >= this.tiles[0].length) {
            LOGGER.error("Tile out of bounds: ({}, {}) - returning null", x, y);
            return null;
        }
        return this.tiles[x][y].getKey();
    }

    public TileView getTileView(Position position) {
        return this.tiles[position.getX()][position.getY()].getValue();
    }

    public void addNullTile(int x, int y) {
        this.tiles[x][y] = null;
    }

    public List<String> getTileTypes() {
        return tileTypes;
    }

}
