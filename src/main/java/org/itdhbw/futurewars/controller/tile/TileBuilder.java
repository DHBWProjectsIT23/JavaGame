package org.itdhbw.futurewars.controller.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.TestTileModel;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.view.tile.TestTileView;
import org.itdhbw.futurewars.view.tile.TileView;

public class TileBuilder {
    private static final Logger LOGGER = LogManager.getLogger(TileBuilder.class);

    private static void setEventHandlers(TestTileView tileView) {
        LOGGER.info("Setting event handlers for tile view {}", tileView);
        tileView.setOnMouseClicked(TileController::handleMouseClick);
        tileView.setOnMouseEntered(TileController::handleMouseEntered);
    }

    public Pair<TileModel, TileView> createTile(TileType tileType, int x, int y) {
        switch (tileType) {
            case TEST_TILE:
                LOGGER.info("Creating tile of type {} at position ({}, {})", tileType, x, y);
                TestTileModel tileModel = new TestTileModel(x, y);
                TestTileView tileView = new TestTileView(tileModel);
                setEventHandlers(tileView);
                return new Pair<>(tileModel, tileView);
            default:
                throw new IllegalArgumentException("Unexpected value: " + tileType);
        }
    }
}

