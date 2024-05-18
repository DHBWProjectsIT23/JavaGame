package org.itdhbw.futurewars.game.controllers.loaders;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.game.controllers.tile.TileCreationController;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.unit.UnitCreationController;
import org.itdhbw.futurewars.game.models.gameState.GameState;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Map loader.
 */
public class MapLoader {
    private static final Logger LOGGER = LogManager.getLogger(MapLoader.class);
    private static final String V1_MAP_VALIDATION = "FUTURE_WARS_MAP_FORMAT";
    private static final String V2_MAP_VALIDATION =
            "FUTURE_WARS_MAP_FORMAT_NEW";
    private static final String V3_MAP_VALIDATION = "FUTURE_WARS_MAP_FORMAT_V3";
    private final Map<String, File> mapFiles;
    private final TileRepository tileRepository;
    private final TileCreationController tileCreationController;
    private final UnitCreationController unitCreationController;
    private final GameState gameState;

    /**
     * Instantiates a new Map loader.
     */
    public MapLoader() {
        this.tileRepository = Context.getTileRepository();
        this.tileCreationController = Context.getTileCreationController();
        this.unitCreationController = Context.getUnitCreationController();
        this.gameState = Context.getGameState();
        this.mapFiles = new HashMap<>();
    }

    /**
     * Number of map files int.
     *
     * @return the int
     */
    public int numberOfMapFiles() {
        return mapFiles.size();
    }

    /**
     * Gets map names.
     *
     * @return the map names
     */
    public List<String> getMapNames() {
        return new ArrayList<>(mapFiles.keySet());
    }

    /**
     * Retrieve system maps.
     *
     * @throws FailedToLoadFileException      the failed to load file exception
     * @throws FailedToRetrieveFilesException the failed to retrieve files exception
     */
    public void retrieveSystemMaps() throws FailedToLoadFileException,
                                            FailedToRetrieveFilesException {
        LOGGER.info("Retrieving system maps");
        mapFiles.putAll(
                FileHelper.retrieveFiles(FileHelper::getInternalMapPath));
        LOGGER.info("Retrieved system maps - total of {} maps",
                    mapFiles.size());
    }

    /**
     * Retrieve user maps.
     *
     * @throws FailedToLoadFileException      the failed to load file exception
     * @throws FailedToRetrieveFilesException the failed to retrieve files exception
     */
    public void retrieveUserMaps() throws FailedToLoadFileException,
                                          FailedToRetrieveFilesException {
        LOGGER.info("Retrieving user maps");
        mapFiles.putAll(FileHelper.retrieveFiles(FileHelper::getUserMapPath));
        LOGGER.info("Retrieved user maps - total of {} maps", mapFiles.size());
    }

    /**
     * Load map.
     *
     * @param filename the filename
     * @throws IOException the io exception
     */
    public void loadMap(String filename) throws IOException {
        LOGGER.info("Loading map from file: {}", filename);
        File mapFile = mapFiles.get(filename);
        try (BufferedReader reader = new BufferedReader(
                new FileReader(mapFile))) {
            String[] validation = reader.readLine().split(",");
            LOGGER.info("Validation: {}", validation[0]);
            switch (validation[0]) {
                case V2_MAP_VALIDATION -> loadV2Map(reader);
                case V1_MAP_VALIDATION -> loadV1Map(reader);
                case V3_MAP_VALIDATION -> loadV3Map(reader);
                case null, default -> throw new IllegalArgumentException(
                        "Given file is not a map file");
            }
        }
    }

    private void loadV3Map(BufferedReader reader) throws IOException {
        LOGGER.info("Loading V3 map");
        initializeMap(reader);
        String line;
        int y = 0;
        while ((line = reader.readLine()) != null) {
            if (y >= gameState.getMapHeightTiles()) {
                LOGGER.error(
                        "Too many lines in the map file, ignoring the rest");
                break;
            }
            String[] tileData = line.split(",");
            int x = 0;
            LOGGER.info("Line: {}", line);
            for (String typeString : tileData) {
                if (x >= (gameState.getMapWidthTiles() * 4)) {
                    LOGGER.warn(
                            "Too many tiles in the line, ignoring the rest");
                    break;
                }
                if (x % 4 == 0) {
                    LOGGER.info("Loading tile type {} with variant {}",
                                typeString, Integer.parseInt(tileData[x + 1]));
                    int finalX = x;
                    int finalY = y;
                    Task<Void> loadTileTask = new Task<>() {
                        @Override
                        protected Void call() {
                            loadTile(typeString, finalX / 2, finalY,
                                     Integer.parseInt(tileData[finalX + 1]));
                            return null;
                        }
                    };
                    loadTileTask.setOnSucceeded(
                            event -> LOGGER.info("Tile loaded"));
                    loadTileTask.setOnFailed(
                            event -> LOGGER.error("Failed to load tile"));
                    new Thread(loadTileTask).start();

                } else if (x % 4 == 2) {
                    int finalX = x;
                    int finalY = y;
                    Task<Void> loadUnitTask = new Task<>() {
                        @Override
                        protected Void call() {
                            loadUnit(typeString, finalX, finalY,
                                     Integer.parseInt(tileData[finalX + 1]));
                            return null;
                        }
                    };
                    loadUnitTask.setOnSucceeded(
                            event -> LOGGER.info("Unit loaded"));
                    loadUnitTask.setOnFailed(
                            event -> LOGGER.error("Failed to load unit"));
                    new Thread(loadUnitTask).start();
                }
                x++;
            }
            y++;
        }
    }

    private void loadV2Map(BufferedReader reader) throws IOException {
        LOGGER.info("Loading V2 map");
        initializeMap(reader);
        String line;
        int y = 0;
        while ((line = reader.readLine()) != null) {
            if (y >= gameState.getMapHeightTiles()) {
                LOGGER.error(
                        "Too many lines in the map file, ignoring the rest");
                break;
            }
            String[] tileData = line.split(",");
            int x = 0;
            for (String typeString : tileData) {
                if (x >= (gameState.getMapWidthTiles() * 2)) {
                    LOGGER.warn(
                            "Too many tiles in the line, ignoring the rest");
                    break;
                }
                if (x % 2 != 0) {
                    loadUnit(typeString, x, y);
                } else {
                    loadTile(typeString, x, y);
                }
                x++;
            }
            y++;
        }
    }


    private void loadV1Map(BufferedReader reader) throws IOException {
        initializeMap(reader);
        String line;
        int y = 0;
        while ((line = reader.readLine()) != null) {
            if (y >= gameState.getMapHeightTiles()) {
                LOGGER.error(
                        "Too many lines in the map file, ignoring the rest");
                break;
            }
            String[] tileData = line.split(",");
            int x = 0;
            for (String tileTypeString : tileData) {
                if (x >= gameState.getMapWidthTiles()) {
                    LOGGER.error(
                            "Too many tiles in the line, ignoring the rest");
                    break;
                }
                loadTile(tileTypeString, x, y);
                x++;
            }
            y++;
        }
    }

    private void initializeMap(BufferedReader reader) throws IOException {
        reader.readLine(); // Discard the first line
        String[] size = reader.readLine().split(",");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);
        gameState.setMapWidthTiles(width);
        gameState.setMapHeightTiles(height);

        gameState.mapHeightProperty()
                 .addListener((observable, oldValue, newValue) -> {
                     resizeTile(gameState.getMapWidth(), newValue.intValue());
                 });
        gameState.mapWidthProperty()
                 .addListener((observable, oldValue, newValue) -> {
                     resizeTile(newValue.intValue(), gameState.getMapHeight());
                 });

        resizeTile(gameState.getMapWidth(), gameState.getMapHeight());

        tileRepository.initializeList(width, height);
        LOGGER.info("Map size: {}x{}", width, height);
        reader.readLine(); // Discard the next line
    }

    private void resizeTile(int mapWidth, int mapHeight) {
        int mapHightTiles = gameState.getMapHeightTiles();
        int mapWidthTiles = gameState.getMapWidthTiles();

        int maxTileSize =
                Math.min(mapHeight / mapHightTiles, mapWidth / mapWidthTiles);
        gameState.setTileSize(maxTileSize);
    }

    private void loadTile(String tileType, int x, int y, int textureVariant) {
        if (!tileType.equals("NONE")) {
            LOGGER.info(
                    "Creating custom tile of type {} - variant {} - at x: {} - y: {}",
                    tileType, textureVariant, x, y);
            tileCreationController.createTile(tileType, (x / 2), y,
                                              textureVariant);
        }
    }

    private void loadTile(String tileType, int x, int y) {
        if (!tileType.equals("NONE")) {
            tileCreationController.createTile(tileType, (x / 2), y);
        }
    }

    private void loadUnit(String unitType, int x, int y, int team) {
        if (!unitType.equals("NONE")) {
            unitCreationController.createUnit(unitType, ((x - 1) / 2), y, team);
        }
    }

    private void loadUnit(String unitType, int x, int y) {
        if (!unitType.equals("NONE")) {
            unitCreationController.createUnit(unitType, ((x - 1) / 2), y, 1);
        }
    }
}
