package org.itdhbw.futurewars.game.controllers.tile.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.game.models.tile.MovementType;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.views.TileView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TileFactory {
    private static final Logger LOGGER = Logger.getLogger(TileFactory.class.getSimpleName());
    private final String tileType;
    private final List<Image> textures = new ArrayList<>();
    private final MovementType movementType;
    private final List<URI> texturePaths;
    private final int terrainCover;
    private TileModel tileModel;
    private TileView tileView;

    public TileFactory(String tileType, int terrainCover, List<URI> texturePaths, MovementType movementType) {
        LOGGER.info("Creating tile factory for unit type: " + tileType);
        this.tileType = tileType;
        this.terrainCover = terrainCover;
        this.movementType = movementType;
        this.texturePaths = texturePaths;
        loadTextures();
    }

    private void loadTextures() {
        for (URI path : texturePaths) {
            Image texture = new Image(path.toString());
            textures.add(texture);
        }
    }

    public Pair<TileModel, TileView> createTile(int x, int y, int textureVariant) {
        createTileModel(x, y);
        createTileView(textureVariant);
        return new Pair<>(tileModel, tileView);
    }

    private void createTileModel(int x, int y) {
        tileModel = new TileModel(tileType, x, y, terrainCover);
        tileModel.setMovementType(movementType);
    }

    private void createTileView(int textureVariant) {
        tileView = new TileView(tileModel);
        Image texture1Image;
        try {
            texture1Image = textures.get(textureVariant);
        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.addException(e, "Texture variant not found");
            texture1Image = null;
        }
        tileView.setTexture(texture1Image);
    }

    public List<Image> getTextures() {
        return textures;
    }
}
