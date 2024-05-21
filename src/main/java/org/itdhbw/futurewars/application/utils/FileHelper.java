package org.itdhbw.futurewars.application.utils;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The type File helper.
 */
public class FileHelper {
    /**
     * The constant INTERNAL_DIR_SHORT.
     */
    public static final String INTERNAL_DIR_SHORT = "src/main/resources/org/itdhbw/futurewars";
    /**
     * The constant INTERNAL_DIR.
     */
    public static final String INTERNAL_DIR = INTERNAL_DIR_SHORT + "/";
    /**
     * The constant USER_DIR_SHORT.
     */
    public static final String USER_DIR_SHORT = System.getProperty("user.dir") + "/resources";
    /**
     * The constant USER_DIR.
     */
    public static final String USER_DIR = USER_DIR_SHORT + "/";
    /**
     * The constant MAP_DIR.
     */
    public static final String MAP_DIR = "maps/";
    /**
     * The constant UNIT_DIR.
     */
    public static final String UNIT_DIR = "units/";
    /**
     * The constant TILE_DIR.
     */
    public static final String TILE_DIR = "tiles/";
    /**
     * The constant UNIT_TEXTURE_DIR.
     */
    public static final String UNIT_TEXTURE_DIR = "textures/units/";
    /**
     * The constant TILE_TEXTURE_DIR.
     */
    public static final String TILE_TEXTURE_DIR = "textures/tiles/";
    /**
     * The constant OTHER_TEXTURE_DIR.
     */
    public static final String OTHER_TEXTURE_DIR = "textures/other/";

    public static final List<String> SUB_DIRS = Arrays.asList(MAP_DIR, UNIT_DIR, TILE_DIR, UNIT_TEXTURE_DIR,
            TILE_TEXTURE_DIR, OTHER_TEXTURE_DIR);
    private static final Map<String, String> SHORTCUTS = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(FileHelper.class);

    static {
        SHORTCUTS.put("$USER_DIR", USER_DIR_SHORT);
        SHORTCUTS.put("$INTERNAL_DIR", INTERNAL_DIR_SHORT);
    }

    private FileHelper() {
        // private constructor to prevent instantiation
    }

    /**
     * Gets internal texture.
     *
     * @param path the path
     * @return the internal texture
     * @throws FailedToLoadTextureException the failed to load texture exception
     */
    public static Image getInternalTexture(String path) throws FailedToLoadTextureException {
        Image texture = new Image("file:" + INTERNAL_DIR + "textures/" + path);
        if (texture.isError()) {
            LOGGER.error("Error loading internal texture: {}",
                    texture.getException().getMessage());
            throw new FailedToLoadTextureException(path);
        }
        return texture;
    }

    /**
     * Gets fxml file.
     *
     * @param path the path
     * @return the fxml file
     * @throws FailedToLoadFileException the failed to load file exception
     */
    public static URI getFxmlFile(String path) throws FailedToLoadFileException {
        File file = new File(INTERNAL_DIR + "fxml/" + path);
        return checkIfFileExists(file);
    }

    /**
     * Gets file.
     *
     * @param path the path
     * @return the file
     * @throws FailedToLoadFileException the failed to load file exception
     */
    public static URI getFile(String path) throws FailedToLoadFileException {
        path = FileHelper.decodePath(path);
        File file = new File(path);
        return checkIfFileExists(file);
    }

    private static URI checkIfFileExists(File file) throws FailedToLoadFileException {
        if (file.exists()) {
            return file.toURI();
        } else {
            throw new FailedToLoadFileException("File not found: " + file);
        }
    }

    /**
     * Gets internal unit path.
     *
     * @return the internal unit path
     */
    public static File getInternalUnitPath() {
        return new File(INTERNAL_DIR + UNIT_DIR);
    }

    /**
     * Gets internal map path.
     *
     * @return the internal map path
     */
    public static File getInternalMapPath() {
        return new File(INTERNAL_DIR + MAP_DIR);
    }

    /**
     * Gets internal tile path.
     *
     * @return the internal tile path
     */
    public static File getInternalTilePath() {
        return new File(INTERNAL_DIR + TILE_DIR);
    }

    /**
     * Gets user map path.
     *
     * @return the user map path
     */
    public static File getUserMapPath() {
        return new File(USER_DIR + MAP_DIR);
    }

    /**
     * Gets user unit path.
     *
     * @return the user unit path
     */
    public static File getUserUnitPath() {
        return new File(USER_DIR + UNIT_DIR);
    }

    /**
     * Gets user tile path.
     *
     * @return the user tile path
     */
    public static File getUserTilePath() {
        return new File(USER_DIR + TILE_DIR);
    }

    private static String decodePath(String path) {
        for (Map.Entry<String, String> entry : SHORTCUTS.entrySet()) {
            path = path.replace(entry.getKey(), entry.getValue());
        }
        return path;
    }

    /**
     * Retrieve files map.
     *
     * @param pathSupplier the path supplier
     * @return the map
     * @throws FailedToLoadFileException      the failed to load file exception
     * @throws FailedToRetrieveFilesException the failed to retrieve files exception
     */
    public static Map<String, File> retrieveFiles(Supplier<File> pathSupplier) throws FailedToLoadFileException,
            FailedToRetrieveFilesException {
        LOGGER.info("Loading files for {}...", pathSupplier.get());
        Map<String, File> files = new HashMap<>();
        URI mapPath = checkIfFileExists(pathSupplier.get());

        try {
            Files.walk(Path.of(mapPath)).filter(Files::isRegularFile)
                    .forEach(file -> {
                        LOGGER.info("Found file: {}", file);
                        files.put(
                                stripFileExtension(file.getFileName().toString()),
                                file.toFile());
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
}
