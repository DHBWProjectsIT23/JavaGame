package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileEventController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.view.tile.TileView;

import java.util.HashMap;
import java.util.Map;

public class TileBuilder {
    private static final Logger LOGGER = LogManager.getLogger(TileBuilder.class);
    private final TileEventController tileEventController;
    private final TileRepository tileRepository;
    private final Map<TileType, TileFactory> tileFactories;

    public TileBuilder() {
        this.tileFactories = new HashMap<>();
        this.tileFactories.put(TileType.TEST_TILE, new TestTileFactory());
        this.tileFactories.put(TileType.UNPASSABLE_TILE, new UnpassableTileFactory());
        this.tileFactories.put(TileType.TILE_NOT_SET, new TileNotSetFactory());
        this.tileFactories.put(TileType.EXPENSIVE_TILE, new ExpensiveTileFactory());

        this.tileEventController = Context.getTileEventController();
        this.tileRepository = Context.getTileRepository();
    }


    private void setEventHandlers(TileView tileView) {
        LOGGER.info("Setting event handlers for tile view {}", tileView);
        tileView.setOnMouseClicked(tileEventController::handleMouseClick);
        tileView.setOnMouseEntered(tileEventController::handleMouseEntered);
    }

    public Pair<TileModel, TileView> createTile(TileType tileType, int x, int y) {
        TileFactory factory = tileFactories.get(tileType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for tile type " + tileType);
        }
        Pair<TileModel, TileView> tilePair = factory.createTile(x, y);
        setEventHandlers(tilePair.getValue());
        tileRepository.addTile(tilePair);
        return tilePair;
    }
}

