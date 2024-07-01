package org.itdhbw.futurewars.game.controllers.loaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.exceptions.InvalidFileFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {
    private static final String UNIT_FILE = "FUTURE_WARS_UNIT_FORMAT";
    private static final String TILE_FILE = "FUTURE_WARS_TILE_FORMAT";
    private static final String MAP_FILE = "FUTURE_WARS_MAP_FORMAT_V3";
    private static final Logger LOGGER = LogManager.getLogger(FileLoader.class);
    private final Map<String, LoaderFactory> loaderFactoryMap;
    private final Map<String, File> files;

    public FileLoader() {
        this.loaderFactoryMap = new HashMap<>();
        this.files = new HashMap<>();
        this.loaderFactoryMap.put(MAP_FILE, new MapLoader());
        this.loaderFactoryMap.put(TILE_FILE, new TileLoader());
        this.loaderFactoryMap.put(UNIT_FILE, new UnitLoader());
    }

    public void retrieveSystemFiles() throws FailedToRetrieveFilesException {
        for (LoaderFactory loaderFactory : loaderFactoryMap.values()) {
            files.putAll(loaderFactory.getSystemFiles());
        }
        LOGGER.info("Retrieved {} system files", files.size());
    }

    public void retrieveUserFiles() throws FailedToRetrieveFilesException {
        for (LoaderFactory loaderFactory : loaderFactoryMap.values()) {
            files.putAll(loaderFactory.getUserFiles());
        }
        LOGGER.info("Retrieved {} user files", files.size());
    }

    public void loadFiles() {
        for (File file : files.values()) {
            try {
                loadFile(file);
            } catch (FailedToLoadFileException e) {
                ErrorHandler.addException(e, "Failed to load file");
            } catch (InvalidFileFormatException e) {
                ErrorHandler.addException(e, "Invalid file format");
            }
        }
    }

    public void loadFile(File file) throws FailedToLoadFileException, InvalidFileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String validation = reader.readLine().split(",")[0];
            LoaderFactory loaderFactory = loaderFactoryMap.get(validation);
            if (loaderFactory == null) {
                throw new InvalidFileFormatException(file.toString());
            }
            loaderFactory.loadFile(reader, file);

        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to load file");
            throw new FailedToLoadFileException(file);
        }

    }

    public void loadMap(String map) throws FailedToLoadFileException {
        LOGGER.info("Loading map: {}", map);
        MapLoader mapLoader = (MapLoader) loaderFactoryMap.get(MAP_FILE);
        mapLoader.loadMap(map);
    }
}
