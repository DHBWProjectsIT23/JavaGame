package org.itdhbw.futurewars.game.controllers.loaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileFactory;
import org.itdhbw.futurewars.game.models.tile.MovementType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * The type Tile loader.
 */
public class TileLoader {
    private static final Logger LOGGER = LogManager.getLogger(TileLoader.class);
    private static final String TILE_VALIDATION = "FUTURE_WARS_TILE_FORMAT";
    private final TileRepository tileRepository;
    private final Map<String, TileFactory> tileFactories;
    private final Map<String, File> tileFiles;
    private List<URI> texturePaths;
    private MovementType movementType;
    private String tileType;

    /**
     * Instantiates a new Tile loader.
     */
    public TileLoader() {
        this.tileRepository = Context.getTileRepository();
        tileFactories = new HashMap<>();
        tileFiles = new HashMap<>();
    }

    /**
     * Number of tile files int.
     *
     * @return the int
     */
    public int numberOfTileFiles() {
        return tileFiles.size();
    }

    /**
     * Retrieve system tiles.
     *
     * @throws FailedToLoadFileException      the failed to load file exception
     * @throws FailedToRetrieveFilesException the failed to retrieve files exception
     */
    public void retrieveSystemTiles() throws FailedToLoadFileException,
            FailedToRetrieveFilesException {
        LOGGER.info("Retrieving system tiles");
        tileFiles.putAll(
                FileHelper.retrieveFiles(FileHelper::getInternalTilePath));
        LOGGER.info("Retrieved system tiles - total of {} tiles",
                tileFiles.size());
    }

    /**
     * Retrieve user tiles.
     *
     * @throws FailedToLoadFileException      the failed to load file exception
     * @throws FailedToRetrieveFilesException the failed to retrieve files exception
     */
    public void retrieveUserTiles() throws FailedToLoadFileException,
            FailedToRetrieveFilesException {
        LOGGER.info("Retrieving user tiles");
        tileFiles.putAll(FileHelper.retrieveFiles(FileHelper::getUserTilePath));
        LOGGER.info("Retrieved user tiles - total of {} tiles",
                tileFiles.size());
    }

    /**
     * Load tiles from files.
     */
    public void loadTilesFromFiles() {
        for (File file : tileFiles.values()) {
            LOGGER.info("Loading tile from file: {}", file);
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(file))) {
                String[] validation = reader.readLine().split(",");
                LOGGER.info("Validation: {}", validation[0]);
                if (Objects.equals(validation[0], TILE_VALIDATION)) {
                    loadTile(reader);
                } else {
                    throw new IllegalArgumentException(
                            "Given file is not a tile file");
                }
            } catch (IOException | FailedToLoadFileException e) {
                ErrorHandler.addException(e, "Failed to load tile");
            }

            createTileFactory();

        }
    }

    private void loadTile(BufferedReader reader) throws IOException,
            FailedToLoadFileException {
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
            ErrorHandler.addException(e, "Failed to retrieve movement type");
        }

        // skip line
        reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            URI uri;
            uri = FileHelper.getFile(line);
            texturePaths.addLast(uri);
        }
    }

    private void createTileFactory() {
        LOGGER.info("Creating unit factory");
        TileFactory tileFactoryCustom = new TileFactory(tileType, texturePaths, movementType);
        tileFactories.put(tileType, tileFactoryCustom);
    }

    /**
     * Gets tile factories.
     *
     * @return the tile factories
     */
    public Map<String, TileFactory> getTileFactories() {
        LOGGER.info("Returning unit factories: {}", tileFactories);
        return tileFactories;
    }
}
