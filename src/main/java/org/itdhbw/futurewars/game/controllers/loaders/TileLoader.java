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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TileLoader implements LoaderFactory {
    private static final Logger LOGGER = LogManager.getLogger(TileLoader.class);
    private final TileRepository tileRepository;
    private List<URI> texturePaths;
    private MovementType movementType;
    private String tileType;
    private int terrainCover;

    public TileLoader() {
        this.tileRepository = Context.getTileRepository();
    }

    public Map<String, File> getSystemFiles() throws
                                              FailedToRetrieveFilesException {
        return FileHelper.retrieveFiles(FileHelper::getInternalTilePath);
    }

    public Map<String, File> getUserFiles() throws
                                            FailedToRetrieveFilesException {
        return FileHelper.retrieveFiles(FileHelper::getUserTilePath);
    }

    public void loadFile(BufferedReader reader, File file) throws
                                                           FailedToLoadFileException {
        try {
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
                ErrorHandler.addException(e,
                                          "Failed to retrieve movement type");
            }

            // skip line
            reader.readLine();

            terrainCover = Integer.parseInt(reader.readLine());

            // skip line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                URI uri;
                uri = FileHelper.getFile(line);
                texturePaths.addLast(uri);
            }
        } catch (Exception e) {
            ErrorHandler.addException(e, "Failed to load tile");
        }
        createTileFactory();
    }

    private void createTileFactory() {
        LOGGER.info("Creating tile factory");
        TileFactory tileFactoryCustom =
                new TileFactory(tileType, terrainCover, texturePaths, movementType);
        Context.getTileBuilder().addTileFactory(tileType, tileFactoryCustom);
    }
}
