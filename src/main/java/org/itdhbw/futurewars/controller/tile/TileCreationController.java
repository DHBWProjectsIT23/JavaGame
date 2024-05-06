// TileCreationController.java
package org.itdhbw.futurewars.controller.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.view.tile.TileView;

public class TileCreationController {
    private static final Logger LOGGER = LogManager.getLogger(TileCreationController.class);
    private final TileBuilder tileBuilder;

    public TileCreationController() {
        this.tileBuilder = Context.getTileBuilder();
    }

    public Pair<TileModel, TileView> createTile(TileType tileType, int x, int y) {
        LOGGER.info("Creating tile of type {} at position ({}, {})", tileType, x, y);
        Pair<TileModel, TileView> tilePair = tileBuilder.createTile(tileType, x, y);
        return tilePair;
    }
}
