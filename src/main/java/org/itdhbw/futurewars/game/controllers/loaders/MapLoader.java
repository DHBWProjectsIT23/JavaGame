package org.itdhbw.futurewars.game.controllers.loaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.exceptions.InvalidFileFormatException;
import org.itdhbw.futurewars.game.controllers.MapRepository;
import org.itdhbw.futurewars.game.controllers.tile.TileCreationController;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.unit.UnitCreationController;
import org.itdhbw.futurewars.game.models.gameState.GameState;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class MapLoader implements LoaderFactory {
    private static final Logger LOGGER = LogManager.getLogger(MapLoader.class);
    private final TileRepository tileRepository;
    private final TileCreationController tileCreationController;
    private final UnitCreationController unitCreationController;
    private final GameState gameState;
    private final MapRepository mapRepository;

    public MapLoader() {
        this.tileRepository = Context.getTileRepository();
        this.mapRepository = Context.getMapRepository();
        this.tileCreationController = Context.getTileCreationController();
        this.unitCreationController = Context.getUnitCreationController();
        this.gameState = Context.getGameState();
    }

    public Map<String, File> getSystemFiles() throws
                                              FailedToRetrieveFilesException {

        return FileHelper.retrieveFiles(FileHelper::getInternalMapPath);
    }

    public Map<String, File> getUserFiles() throws
                                            FailedToRetrieveFilesException {
        return FileHelper.retrieveFiles(FileHelper::getUserMapPath);
    }

    public void loadFile(BufferedReader reader, File file) throws
                                                           FailedToLoadFileException {
        mapRepository.addMap(file.getName(), file);
    }

    public void loadMap(String map) throws FailedToLoadFileException {
        LOGGER.info("Loading map: {}", map);
        File mapFile = mapRepository.getMapFile(map);
        try (BufferedReader reader = new BufferedReader(
                new FileReader(mapFile))) {
            LOGGER.info("Loading V3 map");
            String validation = reader.readLine().split(",")[0];
            if (!validation.equals("FUTURE_WARS_MAP_FORMAT_V3")) {
                throw new InvalidFileFormatException("Invalid map file format");
            }
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
                LOGGER.info("Line: {}", line);
                for (int x = 0; x < tileData.length / 4; x++) {
                    if (x >= (gameState.getMapWidthTiles())) {
                        LOGGER.warn(
                                "Too many tiles in the line, ignoring the rest");
                        break;
                    }
                    loadTile(tileData[x * 4], x, y,
                             Integer.parseInt(tileData[x * 4 + 1]));
                    loadUnit(tileData[x * 4 + 2], x, y,
                             Integer.parseInt(tileData[x * 4 + 3]));
                }
                y++;
            }
        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to load map file");
            throw new FailedToLoadFileException("Failed to load map file");
        } catch (InvalidFileFormatException e) {
            ErrorHandler.addException(e, "Invalid map file format");
            throw new FailedToLoadFileException("Invalid map file format");
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
            tileCreationController.createTile(tileType, x, y, textureVariant);
        }
    }

    private void loadUnit(String unitType, int x, int y, int team) {
        if (!unitType.equals("NONE")) {
            unitCreationController.createUnit(unitType, x, y, team);
        }
    }

}
