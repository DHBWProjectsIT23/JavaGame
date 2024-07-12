package org.itdhbw.futurewars.game.controllers.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.ArrayList;
import java.util.List;

public class TileRepository {
    private static final Logger LOGGER = LogManager.getLogger(TileRepository.class);
    List<String> tileTypes;
    private Pair<TileModel, TileView>[][] tiles;

    public TileRepository() {
        this.tileTypes = new ArrayList<>();
    }

    public int getTileCount() {
        return tileTypes.size();
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

    public TileView getTileView(TileModel tileModel) {
        return this.tiles[tileModel.getPosition().getX()][tileModel.getPosition().getY()].getValue();
    }

}
