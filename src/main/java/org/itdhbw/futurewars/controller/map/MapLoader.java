package org.itdhbw.futurewars.controller.map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class MapLoader {
    private static final Logger LOGGER = LogManager.getLogger(MapLoader.class);
    private static final String MAP_VALIDATION = "FUTURE_WARS_MAP_FORMAT";
    private final TileRepository tileRepository;
    private final TileCreationController tileCreationController;
    private final GameState gameState;

    public MapLoader() {
        this.tileRepository = Context.getTileRepository();
        this.tileCreationController = Context.getTileCreationController();
        this.gameState = Context.getGameState();
    }

    public void loadMap(String filename) throws IOException {
        LOGGER.info("Loading map from file: {}", filename);
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        // Read map size
        String[] validation = reader.readLine().split(",");
        if (!Objects.equals(validation[0], MAP_VALIDATION)) {
            throw new IllegalArgumentException("Given file is not a map file");
        }

        String _ = reader.readLine(); // Discard the first line
        String[] size = reader.readLine().split(",");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);
        gameState.setMapWidth(width);
        gameState.setMapHeight(height);
        tileRepository.initializeList(width, height);
        LOGGER.info("Map size: {}x{}", width, height);

        String _ = reader.readLine(); // Discard the next line
        // Read tiles
        String line;
        int y = 0;
        while ((line = reader.readLine()) != null) {
            String[] tileData = line.split(",");
            int x = 0;
            for (String tileTypeString : tileData) {
                TileType tileType;
                try {
                    tileType = TileType.valueOf(tileTypeString.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    LOGGER.error("Wrong tile type, ignoring {} at x: {} - y: {} ", tileTypeString, x, y);
                    continue;
                }
                LOGGER.info("Creating tile of type {} at x: {} - y: {}", tileTypeString, x, y);
                tileCreationController.createTile(tileType, x, y);
                x++;
            }
            y++;
        }
    }
}



