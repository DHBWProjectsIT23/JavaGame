package org.itdhbw.futurewars.game.controllers.tile.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.game.models.tile.MovementType;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.views.TileView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Tile factory.
 */
public class TileFactory {
    private static final Logger LOGGER = LogManager.getLogger(TileFactory.class);
    private final String tileType;
    private final List<Image> textures = new ArrayList<>();
    private final MovementType movementType;
    private final List<URI> texturePaths;
    private TileModel tileModel;
    private TileView tileView;

    /**
     * Instantiates a new Tile factory.
     *
     * @param tileType     the tile type
     * @param texturePaths the texture paths
     * @param movementType the movement type
     */
    public TileFactory(String tileType, List<URI> texturePaths, MovementType movementType) {
        LOGGER.info("Creating tile factory for unit type: {}", tileType);
        this.tileType = tileType;
        this.movementType = movementType;
        this.texturePaths = texturePaths;
        loadTextures();
    }

    private void loadTextures() {
        LOGGER.info("Loading textures for tile type: {}", tileType);
        for (URI path : texturePaths) {
            Image texture = new Image(path.toString());
            textures.add(texture);
        }
    }

    private void createTileModel(int x, int y) {
        LOGGER.info("Creating unit model");
        tileModel = new TileModel(tileType, x, y);
        tileModel.setMovementType(movementType);
    }

    private void createTileView(int textureVariant) {
        LOGGER.info("Creating unit view");
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

    /**
     * Create tile pair.
     *
     * @param x the x
     * @param y the y
     * @return the pair
     */
    public Pair<TileModel, TileView> createTile(int x, int y) {
        return createTile(x, y, 0);
    }

    /**
     * Create tile pair.
     *
     * @param x              the x
     * @param y              the y
     * @param textureVariant the texture variant
     * @return the pair
     */
    public Pair<TileModel, TileView> createTile(int x, int y, int textureVariant) {
        createTileModel(x, y);
        LOGGER.error("Creating tile view with texture variant: {}",
                textureVariant);
        createTileView(textureVariant);
        return new Pair<>(tileModel, tileView);
    }

    /**
     * Gets textures.
     *
     * @return the textures
     */
    public List<Image> getTextures() {
        return textures;
    }
}