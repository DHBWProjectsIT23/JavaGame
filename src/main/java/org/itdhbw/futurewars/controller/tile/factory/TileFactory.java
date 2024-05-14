package org.itdhbw.futurewars.controller.tile.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.MovementType;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.TileView;

public class TileFactory {
    private static final Logger LOGGER = LogManager.getLogger(TileFactory.class);
    private final String tileType;
    private final String texture;
    private final MovementType movementType;
    private TileModel tileModel;
    private TileView tileView;

    public TileFactory(String tileType, String texture, MovementType movementType) {
        LOGGER.info("Creating tile factory for unit type: {}", tileType);
        this.tileType = tileType;
        this.texture = texture;
        this.movementType = movementType;
    }

    private void createTileModel(int x, int y) {
        LOGGER.info("Creating unit model");
        tileModel = new TileModel(tileType, x, y);
        tileModel.setMovementType(movementType);
    }

    private void createTileView() {
        LOGGER.info("Creating unit view");
        tileView = new TileView(tileModel);
        Image texture1Image = new Image("file:" + this.texture);
        tileView.setTexture(texture1Image);
    }

    public Pair<TileModel, TileView> getUnit(int x, int y) {
        createTileModel(x, y);
        createTileView();
        return new Pair<>(tileModel, tileView);
    }
}
