package org.itdhbw.futurewars.game.controllers.loaders;

import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.exceptions.InvalidFileFormatException;
import org.itdhbw.futurewars.game.controllers.map.MapRepository;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileCreationController;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitCreationController;
import org.itdhbw.futurewars.game.models.game_state.GameState;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class MapLoader implements LoaderFactory {
    private static final Logger LOGGER = Logger.getLogger(MapLoader.class.getSimpleName());
    private static final String MAP_FORMAT_INVALID = "Invalid map file format";
    private final TileRepository tileRepository;
    private final TileCreationController tileCreationController;
    private final UnitCreationController unitCreationController;
    private final GameState gameState;
    private final MapRepository mapRepository;

    public MapLoader() {
        this.tileRepository = Context.getTileRepository();
        this.mapRepository = Context.getMapRepository();
        this.tileCreationController = Context.getTileCreationController();
        this.gameState = Context.getGameState();

        this.unitCreationController = new UnitCreationController();
    }

    public void loadFile(BufferedReader reader, File file) throws FailedToLoadFileException {
        mapRepository.addMap(file.getName(), file);
    }

    public Map<String, File> getUserFiles() throws FailedToRetrieveFilesException {
        return FileHelper.retrieveFiles(FileHelper::getUserMapPath, FileHelper.MAP_FILE_ENDING);
    }

    public Map<String, File> getSystemFiles() throws FailedToRetrieveFilesException {

        return FileHelper.retrieveFiles(FileHelper::getInternalMapPath, FileHelper.MAP_FILE_ENDING);
    }

    public void loadMap(String map) throws FailedToLoadFileException {
        LOGGER.info("Loading map: " + map);
        File mapFile = mapRepository.getMapFile(map);
        try (BufferedReader reader = new BufferedReader(new FileReader(mapFile))) {
            LOGGER.info("Loading V3 map");
            String validation = reader.readLine().split(",")[0];
            if (!validation.equals("FUTURE_WARS_MAP_FORMAT_V3")) {
                throw new InvalidFileFormatException(MAP_FORMAT_INVALID);
            }
            initializeMap(reader);
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                if (y >= gameState.getMapHeightTiles()) {
                    LOGGER.warning("Too many lines in the map file, ignoring the rest");
                    break;
                }
                String[] tileData = line.split(",");
                for (int x = 0; x < tileData.length / 4; x++) {
                    if (x >= (gameState.getMapWidthTiles())) {
                        LOGGER.warning("Too many tiles in the line, ignoring the rest");
                        break;
                    }
                    loadTile(tileData[x * 4], x, y, Integer.parseInt(tileData[x * 4 + 1]));
                    loadUnit(tileData[x * 4 + 2], x, y, Integer.parseInt(tileData[x * 4 + 3]));
                }
                y++;
            }
        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to load map file");
            throw new FailedToLoadFileException("Failed to load map file");
        } catch (InvalidFileFormatException e) {
            ErrorHandler.addException(e, MAP_FORMAT_INVALID);
            throw new FailedToLoadFileException(MAP_FORMAT_INVALID);
        }
    }

    private void initializeMap(BufferedReader reader) throws IOException {
        reader.readLine(); // Discard the first line
        String[] size = reader.readLine().split(",");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);
        gameState.setMapWidthTiles(width);
        gameState.setMapHeightTiles(height);

        gameState.mapHeightProperty().addListener(
                (observable, oldValue, newValue) -> resizeTile(gameState.getMapWidth(), newValue.intValue()));
        gameState.mapWidthProperty().addListener(
                (observable, oldValue, newValue) -> resizeTile(newValue.intValue(), gameState.getMapHeight()));

        resizeTile(gameState.getMapWidth(), gameState.getMapHeight());

        tileRepository.initializeList(width, height);
        LOGGER.info("Map size: " + width + "x" + height);
        reader.readLine(); // Discard the next line
    }

    private void loadTile(String tileType, int x, int y, int textureVariant) {
        if (!tileType.equals("NONE")) {
            tileCreationController.createUnsetTile(tileType, x, y, textureVariant);
        }
    }

    private void loadUnit(String unitType, int x, int y, int team) {
        if (!unitType.equals("NONE")) {
            unitCreationController.createUnit(unitType, x, y, team);
        }
    }

    private void resizeTile(int mapWidth, int mapHeight) {
        int mapHightTiles = gameState.getMapHeightTiles();
        int mapWidthTiles = gameState.getMapWidthTiles();

        int maxTileSize = Math.min(mapHeight / mapHightTiles, mapWidth / mapWidthTiles);
        gameState.setTileSize(maxTileSize);
    }
}
