package org.itdhbw.futurewars.controller.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.unit.UnitCreationController;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class MapLoader {
    private static final Logger LOGGER = LogManager.getLogger(MapLoader.class);
    private static final String OLD_MAP_VALIDATION = "FUTURE_WARS_MAP_FORMAT";
    private static final String NEW_MAP_VALIDATION = "FUTURE_WARS_MAP_FORMAT_NEW";
    private final TileRepository tileRepository;
    private final TileCreationController tileCreationController;
    private final UnitCreationController unitCreationController;
    private final GameState gameState;

    public MapLoader() {
        this.tileRepository = Context.getTileRepository();
        this.tileCreationController = Context.getTileCreationController();
        this.unitCreationController = Context.getUnitCreationController();
        this.gameState = Context.getGameState();
    }

    public void loadMap(String filename) throws IOException {
        LOGGER.info("Loading map from file: {}", filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] validation = reader.readLine().split(",");
            LOGGER.info("Validation: {}", validation[0]);
            if (Objects.equals(validation[0], NEW_MAP_VALIDATION)) {
                loadNewMap(reader);
            } else if (Objects.equals(validation[0], OLD_MAP_VALIDATION)) {
                loadOldMap(reader);
            } else {
                throw new IllegalArgumentException("Given file is not a map file");
            }
        }
    }

    private void loadNewMap(BufferedReader reader) throws IOException {
        initializeMap(reader);
        String line;
        int y = 0;
        while ((line = reader.readLine()) != null) {
            if (y >= gameState.getMapHeightTiles()) {
                LOGGER.error("Too many lines in the map file, ignoring the rest");
                break;
            }
            String[] tileData = line.split(",");
            int x = 0;
            for (String typeString : tileData) {
                if (x >= (gameState.getMapWidthTiles() * 2)) {
                    LOGGER.warn("Too many tiles in the line, ignoring the rest");
                    break;
                }
                if (x % 2 != 0) {
                    loadCustomUnit(typeString, x, y);
                } else {
                    loadCustomTile(typeString, x, y);
                }
                x++;
            }
            y++;
        }
    }

    private void loadOldMap(BufferedReader reader) throws IOException {
        initializeMap(reader);
        String line;
        int y = 0;
        while ((line = reader.readLine()) != null) {
            if (y >= gameState.getMapHeightTiles()) {
                LOGGER.error("Too many lines in the map file, ignoring the rest");
                break;
            }
            String[] tileData = line.split(",");
            int x = 0;
            for (String tileTypeString : tileData) {
                if (x >= gameState.getMapWidthTiles()) {
                    LOGGER.error("Too many tiles in the line, ignoring the rest");
                    break;
                }
                loadCustomTile(tileTypeString, x, y);
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

        gameState.mapHeightProperty().addListener((observable, oldValue, newValue) -> {
            resizeTile(gameState.getMapWidth(), newValue.intValue());
        });
        gameState.mapWidthProperty().addListener((observable, oldValue, newValue) -> {
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

        int maxTileSize = Math.min(mapHeight / mapHightTiles, mapWidth / mapWidthTiles);
        gameState.setTileSize(maxTileSize);
    }

    private void loadCustomTile(String tileType, int x, int y) {
        if (!tileType.equals("NONE")) {
            LOGGER.info("Creating custom tile of type {} at x: {} - y: {}", tileType, x, y);
            tileCreationController.createTile(tileType, (x / 2), y);
        }
    }

    private void loadCustomUnit(String unitType, int x, int y) {
        if (!unitType.equals("NONE")) {
            LOGGER.info("Creating custom unit of type {} at x: {} - y: {}", unitType, x, y);
            unitCreationController.createUnit(unitType, ((x - 1) / 2), y);
        }
    }
}