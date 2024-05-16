package org.itdhbw.futurewars.controller.tile.factory;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.MovementType;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.view.TileView;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TileFactory {
    private static final Logger LOGGER = LogManager.getLogger(TileFactory.class);
    private final String tileType;
    private final List<Image> textures = new ArrayList<>();
    private final MovementType movementType;
    private final List<String> texturePaths;
    private TileModel tileModel;
    private TileView tileView;

    public TileFactory(String tileType, List<String> texturePaths, MovementType movementType) {
        LOGGER.info("Creating tile factory for unit type: {}", tileType);
        this.tileType = tileType;
        this.movementType = movementType;
        this.texturePaths = texturePaths;
        loadTextures();
    }

    private void loadTextures() {
        LOGGER.info("Loading textures for tile type: {}", tileType);
        for (String path : texturePaths) {
            URL resource = getClass().getResource(path);
            URI file;
            try {
                file = resource == null ? URI.create(path) : resource.toURI();
            } catch (Exception e) {
                LOGGER.error("Error loading texture: {}", e.getMessage());
                return;
            }

            Image texture = new Image(file.toString());
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
            LOGGER.error("Texture variant {} not found", textureVariant);
            texture1Image = null;
        }
        tileView.setTexture(texture1Image);
    }

    public Pair<TileModel, TileView> createTile(int x, int y) {
        return createTile(x, y, 0);
    }

    public Pair<TileModel, TileView> createTile(int x, int y, int textureVariant) {
        createTileModel(x, y);
        LOGGER.error("Creating tile view with texture variant: {}", textureVariant);
        createTileView(textureVariant);
        return new Pair<>(tileModel, tileView);
    }

    public List<Image> getTextures() {
        return textures;
    }
}
