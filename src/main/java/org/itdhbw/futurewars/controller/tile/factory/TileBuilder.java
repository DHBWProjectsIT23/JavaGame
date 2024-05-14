package org.itdhbw.futurewars.controller.tile.factory;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileEventController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.TileView;

import java.util.Map;

public class TileBuilder {
    private static final Logger LOGGER = LogManager.getLogger(TileBuilder.class);
    private final Map<String, TileFactory> tileFactories;
    private final TileRepository tileRepository;
    private final TileEventController tileEventController;

    public TileBuilder() {
        this.tileFactories = Context.getTileLoader().getTileFactories();
        this.tileRepository = Context.getTileRepository();
        this.tileEventController = Context.getTileEventController();
    }

    public Pair<TileModel, TileView> createTile(String tileType, int x, int y) {
        TileFactory factory = tileFactories.get(tileType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for unit type " + tileType);
        }
        Pair<TileModel, TileView> tilePairCustom = factory.getUnit(x, y);
        setEventHandlers(tilePairCustom.getValue());
        TileModel tileModel = tilePairCustom.getKey();
        TileView tileView = tilePairCustom.getValue();
        Pair<TileModel, TileView> tilePair = new Pair<>(tileModel, tileView);
        tileRepository.addTile(tilePair);
        return tilePair;
    }

    private void setEventHandlers(TileView tileView) {
        LOGGER.info("Setting event handlers for tile view {}", tileView);
        tileView.setOnMouseClicked(tileEventController::handleMouseClick);
        tileView.setOnMouseEntered(tileEventController::handleMouseEnter);
    }
}

