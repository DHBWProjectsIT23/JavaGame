package org.itdhbw.futurewars.application.utils;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.exceptions.FileDoesNotExistException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FileHelper {
    public static final String INTERNAL_DIR_SHORT = "src/main/resources/org/itdhbw/futurewars";
    public static final String INTERNAL_DIR = INTERNAL_DIR_SHORT + "/";
    public static final String USER_DIR_SHORT = System.getProperty("user.dir") + "/resources";
    public static final String USER_DIR = USER_DIR_SHORT + "/";
    public static final String MAP_DIR = "maps/";
    public static final String UNIT_DIR = "units/";
    public static final String TILE_DIR = "tiles/";
    public static final String OTHER_TEXTURE_DIR = "textures/other/";
    public static final String TEXTURES_DIR = "textures/";
    public static final String MAP_FILE_ENDING = ".fwm";
    public static final String TILE_FILE_ENDING = ".fwt";
    public static final String UNIT_FILE_ENDING = ".fwu";
    public static final List<String> SUB_DIRS = Arrays.asList(MAP_DIR, UNIT_DIR, TILE_DIR, OTHER_TEXTURE_DIR);
    public static final String PROPERTIES_FILE = "textures.properties";
    private static final Map<String, String> SHORTCUTS = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(FileHelper.class);

    public static Image getMiscTexture(MiscTextures texture) throws FailedToLoadTextureException {
        Properties userTextures = loadTextureProperties(USER_DIR + OTHER_TEXTURE_DIR);
        Properties internalTextures = loadTextureProperties(INTERNAL_DIR + OTHER_TEXTURE_DIR);

        if (userTextures.containsKey(texture.toString())) {
            String textureFile = userTextures.get(texture.toString()).toString();
            return getUserTexture("other/" + textureFile);
        } else if (internalTextures.containsKey(texture.toString())) {
            String textureFile = internalTextures.get(texture.toString()).toString();
            return getInternalTexture("other/" + textureFile);
        } else {
            throw new FailedToLoadTextureException(texture.toString());
        }
    }

    static {
        SHORTCUTS.put("$USER_DIR", USER_DIR_SHORT);
        SHORTCUTS.put("$INTERNAL_DIR", INTERNAL_DIR_SHORT);
    }

    private FileHelper() {
        // private constructor to prevent instantiation
    }

    public static Image getInternalTexture(String path) throws FailedToLoadTextureException {
        Image texture = new Image("file:" + INTERNAL_DIR + TEXTURES_DIR + path);
        if (texture.isError()) {
            LOGGER.error("Error loading internal texture: {}", texture.getException().getMessage());
            throw new FailedToLoadTextureException(path);
        }
        return texture;
    }

    private static Properties loadTextureProperties(String dir) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dir + PROPERTIES_FILE))) {
            Properties properties = new Properties();
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to load texture properties");
            return new Properties();
        }

    }

    public static URI getFxmlFile(String path) throws FailedToLoadFileException {
        File file = new File(INTERNAL_DIR + "fxml/" + path);
        try {
            return checkIfFileExists(file);
        } catch (FileDoesNotExistException e) {
            throw new FailedToLoadFileException(path);
        }
    }

    public static URI getFile(String path) throws FailedToLoadFileException {
        path = FileHelper.decodePath(path);
        File file = new File(path);
        try {
            return checkIfFileExists(file);
        } catch (FileDoesNotExistException e) {
            throw new FailedToLoadFileException(path);
        }
    }

    public static URI getTexture(File object, String texture) throws FailedToLoadTextureException {
        // texture is at the location of the oject + "textures/" + texture
        Path path = Paths.get(
                object.getAbsolutePath().substring(0, object.getAbsolutePath().lastIndexOf(File.separator) + 1) +
                TEXTURES_DIR + texture);
        try {
            return checkIfFileExists(path.toFile());
        } catch (FileDoesNotExistException e) {
            throw new FailedToLoadTextureException(texture);
        }
    }

    private static URI checkIfFileExists(File file) throws FileDoesNotExistException {
        if (file.exists()) {
            return file.toURI();
        } else {
            FileDoesNotExistException e = new FileDoesNotExistException(file.toString());
            ErrorHandler.addException(e, "File does not exist");
            throw e;
        }
    }

    public static File getInternalUnitPath() {
        return new File(INTERNAL_DIR + UNIT_DIR);
    }

    public static File getInternalMapPath() {
        return new File(INTERNAL_DIR + MAP_DIR);
    }

    public static File getInternalTilePath() {
        return new File(INTERNAL_DIR + TILE_DIR);
    }

    public static File getUserMapPath() {
        return new File(USER_DIR + MAP_DIR);
    }

    public static File getUserUnitPath() {
        return new File(USER_DIR + UNIT_DIR);
    }

    public static File getUserTilePath() {
        return new File(USER_DIR + TILE_DIR);
    }

    private static String decodePath(String path) {
        for (Map.Entry<String, String> entry : SHORTCUTS.entrySet()) {
            path = path.replace(entry.getKey(), entry.getValue());
        }
        return path;
    }

    public static Map<String, File> retrieveFiles(Supplier<File> pathSupplier, String fileEnding) throws
                                                                                                  FailedToRetrieveFilesException {
        LOGGER.info("Loading files for {}...", pathSupplier.get());
        Map<String, File> files = new HashMap<>();
        URI mapPath;
        try {
            mapPath = checkIfFileExists(pathSupplier.get());
        } catch (FileDoesNotExistException e) {
            ErrorHandler.addException(e, "File does not exist");
            throw new FailedToRetrieveFilesException(pathSupplier.get().toString());
        }

        try (Stream<Path> stream = Files.walk(Path.of(mapPath))) {
            stream.filter(Files::isRegularFile).filter(file -> file.getFileName().toString().endsWith(fileEnding))
                  .forEach(file -> {
                      LOGGER.info("Found file: {}", file);
                      files.put(stripFileExtension(file.getFileName().toString()), file.toFile());
                  });
        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to retrieve files");
            throw new FailedToRetrieveFilesException(mapPath.toString());
        }
        return files;
    }

    private static String stripFileExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    public static Image getUserTexture(String path) throws FailedToLoadTextureException {
        Image texture = new Image("file:" + USER_DIR + TEXTURES_DIR + path);
        if (texture.isError()) {
            LOGGER.error("Error loading user texture: {}", texture.getException().getMessage());
            throw new FailedToLoadTextureException(path);
        }
        return texture;
    }

    public enum MiscTextures {
        FALLBACK, HIGHLIGHTED, SELECTED, ATTACKABLE, HOVERED, HOVERED_OCCUPIED
    }

}
