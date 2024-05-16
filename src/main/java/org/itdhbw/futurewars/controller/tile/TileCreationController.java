// TileCreationController.java
package org.itdhbw.futurewars.controller.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.factory.TileBuilder;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.TileView;

public class TileCreationController {
    private static final Logger LOGGER = LogManager.getLogger(TileCreationController.class);
    private final TileBuilder tileBuilder;

    public TileCreationController() {
        this.tileBuilder = Context.getTileBuilder();
    }

    public Pair<TileModel, TileView> createTile(String tileType, int x, int y, int textureVariant) {
        LOGGER.info("Creating custom tile of type {} at position ({}, {})", tileType, x, y, textureVariant);
        return tileBuilder.createTile(tileType, x, y, textureVariant);
    }

    public Pair<TileModel, TileView> createTile(String tileType, int x, int y) {
        return createTile(tileType, x, y, 0);
    }
}
