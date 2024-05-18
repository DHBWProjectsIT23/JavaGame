// TileCreationController.java
package org.itdhbw.futurewars.game.controllers.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileBuilder;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.views.TileView;

/**
 * The type Tile creation controller.
 */
public class TileCreationController {
    private static final Logger LOGGER =
            LogManager.getLogger(TileCreationController.class);
    private final TileBuilder tileBuilder;

    /**
     * Instantiates a new Tile creation controller.
     */
    public TileCreationController() {
        this.tileBuilder = Context.getTileBuilder();
    }

    /**
     * Create tile pair.
     *
     * @param tileType       the tile type
     * @param x              the x
     * @param y              the y
     * @param textureVariant the texture variant
     * @return the pair
     */
    public Pair<TileModel, TileView> createTile(String tileType, int x, int y, int textureVariant) {
        LOGGER.info("Creating custom tile of type {} at position ({}, {})",
                    tileType, x, y, textureVariant);
        return tileBuilder.createTile(tileType, x, y, textureVariant);
    }

    /**
     * Create tile pair.
     *
     * @param tileType the tile type
     * @param x        the x
     * @param y        the y
     * @return the pair
     */
    public Pair<TileModel, TileView> createTile(String tileType, int x, int y) {
        return createTile(tileType, x, y, 0);
    }
}
