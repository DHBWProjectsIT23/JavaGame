package org.itdhbw.futurewars.controller.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.tile.factory.TileFactory;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.MovementType;
import org.itdhbw.futurewars.util.FileHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TileLoader {
    private static final Logger LOGGER = LogManager.getLogger(TileLoader.class);
    private static final String TILE_VALIDATION = "FUTURE_WARS_TILE_FORMAT";
    private final TileRepository tileRepository;
    private final Map<String, TileFactory> tileFactories;
    private final Map<String, File> tileFiles;
    private List<String> texturePaths;
    private MovementType movementType;
    private String tileType;

    public TileLoader() {
        this.tileRepository = Context.getTileRepository();
        tileFactories = new HashMap<>();
        tileFiles = new HashMap<>();
    }

    public int numberOfTileFiles() {
        return tileFiles.size();
    }

    public void retrieveSystemTiles() {
        LOGGER.info("Retrieving system tiles");
        tileFiles.putAll(FileHelper.retrieveFiles(FileHelper::getInternalTilePath));
        LOGGER.info("Retrieved system tiles - total of {} tiles", tileFiles.size());
    }

    public void retrieveUserTiles() {
        LOGGER.info("Retrieving user tiles");
        tileFiles.putAll(FileHelper.retrieveFiles(FileHelper::getUserTilePath));
        LOGGER.info("Retrieved user tiles - total of {} tiles", tileFiles.size());
    }

    public void loadTilesFromFiles() {
        for (File file : tileFiles.values()) {
            LOGGER.info("Loading tile from file: {}", file);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String[] validation = reader.readLine().split(",");
                LOGGER.info("Validation: {}", validation[0]);
                if (Objects.equals(validation[0], TILE_VALIDATION)) {
                    loadTile(reader);
                } else {
                    throw new IllegalArgumentException("Given file is not a tile file");
                }
            } catch (IOException e) {
                LOGGER.error("Error loading tile from file: {}", file);
            }

            createTileFactory();

        }
    }

    private void loadTile(BufferedReader reader) throws IOException {
        texturePaths = new ArrayList<>();
        LOGGER.info("Loading tile from file...");
        // now on second line - skipping
        reader.readLine();
        tileType = reader.readLine();
        tileRepository.addTileType(tileType);

        // skip line
        reader.readLine();

        String movementTypeString = reader.readLine();
        try {
            movementType = MovementType.valueOf(movementTypeString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid movement type: {}", movementTypeString);
        }

        // skip line
        reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            texturePaths.addLast(FileHelper.decodePath(line));
        }
    }

    private void createTileFactory() {
        LOGGER.info("Creating unit factory");
        TileFactory tileFactoryCustom = new TileFactory(tileType, texturePaths, movementType);
        tileFactories.put(tileType, tileFactoryCustom);
    }

    public Map<String, TileFactory> getTileFactories() {
        LOGGER.info("Returning unit factories: {}", tileFactories);
        return tileFactories;
    }
}
