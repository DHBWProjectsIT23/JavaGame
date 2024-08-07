package org.itdhbw.futurewars.game.controllers.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileEventController;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TileBuilder {
    private static final Logger LOGGER = Logger.getLogger(TileBuilder.class.getSimpleName());
    private final Map<String, TileFactory> tileFactories;
    private final TileRepository tileRepository;
    private final TileEventController tileEventController;

    public TileBuilder() {
        this.tileFactories = new HashMap<>();
        this.tileRepository = Context.getTileRepository();
        this.tileEventController = Context.getTileEventController();
    }

    public void addTileFactory(String tileType, TileFactory factory) {
        LOGGER.info("Adding tile factory for tile type " + tileType);
        tileFactories.put(tileType, factory);
    }

    // Package-private by default
    Pair<TileModel, TileView> createTile(String tileType, int x, int y, int textureVariant) {
        TileFactory factory = tileFactories.get(tileType);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for unit type " + tileType);
        }
        Pair<TileModel, TileView> tilePairCustom = factory.createTile(x, y, textureVariant);
        setEventHandlers(tilePairCustom.getValue());
        TileModel tileModel = tilePairCustom.getKey();
        TileView tileView = tilePairCustom.getValue();
        Pair<TileModel, TileView> tilePair = new Pair<>(tileModel, tileView);
        tileRepository.addTile(tilePair);
        return tilePair;
    }

    private void setEventHandlers(TileView tileView) {
        tileView.setOnMouseClicked(tileEventController::handleMouseClick);
        tileView.setOnMouseEntered(tileEventController::handleMouseEnter);
    }

    public Map<String, TileFactory> getTileFactories() {
        return tileFactories;
    }
}

