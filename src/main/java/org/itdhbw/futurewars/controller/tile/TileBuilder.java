package org.itdhbw.futurewars.controller.tile;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.*;
import org.itdhbw.futurewars.view.tile.TestTileView;
import org.itdhbw.futurewars.view.tile.TileNotSetView;
import org.itdhbw.futurewars.view.tile.TileView;
import org.itdhbw.futurewars.view.tile.UnpassableTileView;

public class TileBuilder {
    private static final Logger LOGGER = LogManager.getLogger(TileBuilder.class);
    private final TileEventController tileEventController;
    private final TileRepository tileRepository;

    public TileBuilder() {
        this.tileEventController = Context.getTileEventController();
        this.tileRepository = Context.getTileRepository();
    }


    private void setEventHandlers(TileView tileView) {
        LOGGER.info("Setting event handlers for tile view {}", tileView);
        tileView.setOnMouseClicked(tileEventController::handleMouseClick);
        tileView.setOnMouseEntered(tileEventController::handleMouseEntered);
    }

    public Pair<TileModel, TileView> createTile(TileType tileType, int x, int y) {
        switch (tileType) {
            case TEST_TILE:
                LOGGER.info("Creating tile of type {} at position ({}, {})", tileType, x, y);
                TestTileModel tileModel = new TestTileModel(x, y);
                TestTileView tileView = new TestTileView(tileModel);
                setEventHandlers(tileView);
                tileRepository.addTile(tileModel, tileView);
                return new Pair<>(tileModel, tileView);
            case UNPASSABLE_TILE:
                LOGGER.info("Creating tile of type {} at position ({}, {})", tileType, x, y);
                UnpassableTileModel tileModel2 = new UnpassableTileModel(x, y);
                UnpassableTileView tileView2 = new UnpassableTileView(tileModel2);
                setEventHandlers(tileView2);
                tileRepository.addTile(tileModel2, tileView2);
                return new Pair<>(tileModel2, tileView2);
            case TILE_NOT_SET:
                LOGGER.info("Creating tile of type {} at position ({}, {})", tileType, x, y);
                TileNotSetModel tileModel3 = new TileNotSetModel(x, y);
                TileNotSetView tileView3 = new TileNotSetView(tileModel3);
                setEventHandlers(tileView3);
                tileRepository.addTile(tileModel3, tileView3);
                return new Pair<>(tileModel3, tileView3);
            default:
                throw new IllegalArgumentException("Unexpected value: " + tileType);
        }
    }
}

