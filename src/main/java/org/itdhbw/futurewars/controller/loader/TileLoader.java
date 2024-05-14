package org.itdhbw.futurewars.controller.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.tile.factory.TileFactory;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.MovementType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TileLoader {
    private static final Logger LOGGER = LogManager.getLogger(TileLoader.class);
    private static final String TILE_VALIDATION = "FUTURE_WARS_TILE_FORMAT";
    private final TileRepository tileRepository;
    private final HashMap<String, TileFactory> tileFactories;
    private MovementType movementType;
    private String texture;
    private String tileType;

    public TileLoader() {
        this.tileRepository = Context.getTileRepository();
        tileFactories = new HashMap<>();

        Path dir = Paths.get("resources/testTile");
        List<String> files = new ArrayList<>();

        try {
            Files.walk(dir)
                    .filter(Files::isRegularFile)
                    .forEach(file -> files.add(file.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String file : files) {
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
                e.printStackTrace();
            }

            createTileFactory();

        }
    }

    private void loadTile(BufferedReader reader) throws IOException {
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

        texture = reader.readLine();
    }

    private void createTileFactory() {
        LOGGER.info("Creating unit factory");
        TileFactory tileFactoryCustom = new TileFactory(tileType, texture, movementType);
        tileFactories.put(tileType, tileFactoryCustom);
    }

    public Map<String, TileFactory> getTileFactories() {
        LOGGER.info("Returning unit factories: {}", tileFactories);
        return tileFactories;
    }
}
