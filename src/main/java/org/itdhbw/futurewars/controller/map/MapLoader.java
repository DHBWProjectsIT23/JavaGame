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

public class MapLoader {
    private static final Logger LOGGER = LogManager.getLogger(MapLoader.class);
    private final TileRepository tileRepository;
    private final TileCreationController tileCreationController;
    private final GameState gameState;

    public MapLoader() {
        this.tileRepository = Context.getTileRepository();
        this.tileCreationController = Context.getTileCreationController();
        this.gameState = Context.getGameState();
    }

    public void loadMap(String filename) {
        LOGGER.info("Loading map from file: {}", filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read map size
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
            while ((line = reader.readLine()) != null) {
                String[] tileData = line.split(",");
                String type = tileData[0];
                TileType tileType;
                try {
                    tileType = TileType.valueOf(type.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Wrong tile type");
                }
                int x = Integer.parseInt(tileData[1]);
                int y = Integer.parseInt(tileData[2]);
                LOGGER.info("Tile at ({},{}): {}", x, y, type);
                tileCreationController.createTile(tileType, x, y);
                // Create TileModel and TileView based on the type
                // This is just an example, replace with your actual implementation
                //TileModel tileModel = new TileModel(type, new Position(x, y));
                //TileView tileView = new TileView(tileModel);

                // Add the tile to the repository
                //tileRepository.addTile(tileModel, tileView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



