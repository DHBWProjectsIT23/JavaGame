package org.itdhbw.futurewars.game.controllers.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Tile repository.
 */
public class TileRepository {
    private final static Logger LOGGER =
            LogManager.getLogger(TileRepository.class);
    /**
     * The Tile types.
     */
    List<String> tileTypes;
    private Pair<TileModel, TileView>[][] tiles;

    /**
     * Instantiates a new Tile repository.
     */
    public TileRepository() {
        this.tileTypes = new ArrayList<>();
    }

    /**
     * Add tile type.
     *
     * @param type the type
     */
    public void addTileType(String type) {
        this.tileTypes.add(type);
    }

    /**
     * Initialize list.
     *
     * @param width  the width
     * @param height the height
     */
    public void initializeList(int width, int height) {
        this.tiles = new Pair[width][height];
    }

    /**
     * Get all tiles pair [ ] [ ].
     *
     * @return the pair [ ] [ ]
     */
    public Pair<TileModel, TileView>[][] getAllTiles() {
        return tiles;
    }

    /**
     * Add tile.
     *
     * @param tilePair the tile pair
     */
    public void addTile(Pair<TileModel, TileView> tilePair) {
        this.tiles[tilePair.getKey().getPosition().getX()][tilePair.getKey()
                                                                   .getPosition()
                                                                   .getY()] =
                tilePair;
    }

    /**
     * Add tile.
     *
     * @param tileModel the tile model
     * @param tileView  the tile view
     */
    public void addTile(TileModel tileModel, TileView tileView) {
        this.tiles[tileModel.getPosition().getX()][tileModel.getPosition()
                                                            .getY()] =
                new Pair<>(tileModel, tileView);
    }

    /**
     * Gets tile.
     *
     * @param position the position
     * @return the tile
     */
    public Pair<TileModel, TileView> getTile(Position position) {
        return this.tiles[position.getX()][position.getY()];
    }

    /**
     * Gets tile.
     *
     * @param x the x
     * @param y the y
     * @return the tile
     */
    public Pair<TileModel, TileView> getTile(int x, int y) {
        return this.tiles[x][y];
    }

    /**
     * Gets tile model.
     *
     * @param position the position
     * @return the tile model
     */
    public TileModel getTileModel(Position position) {
        int x = position.getX();
        int y = position.getY();
        if (x < 0 || x >= this.tiles.length || y < 0 ||
            y >= this.tiles[0].length) {
            LOGGER.error("Tile out of bounds: ({}, {}) - returning null", x, y);
            return null;
        }
        return this.tiles[x][y].getKey();
    }

    /**
     * Gets tile view.
     *
     * @param position the position
     * @return the tile view
     */
    public TileView getTileView(Position position) {
        return this.tiles[position.getX()][position.getY()].getValue();
    }

    /**
     * Add null tile.
     *
     * @param x the x
     * @param y the y
     */
    public void addNullTile(int x, int y) {
        this.tiles[x][y] = null;
    }

    /**
     * Gets tile types.
     *
     * @return the tile types
     */
    public List<String> getTileTypes() {
        return tileTypes;
    }

}
