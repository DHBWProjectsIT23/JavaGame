package org.itdhbw.futurewars.util;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.util.exceptions.CanNotLoadException;

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

public class FileHelper {
    public static final String INTERNAL_DIR_SHORT = "src/main/resources/org/itdhbw/futurewars";
    public static final String INTERNAL_DIR = INTERNAL_DIR_SHORT + "/";
    public static final String USER_DIR_SHORT = System.getProperty("user.dir") + "/resources";
    public static final String USER_DIR = USER_DIR_SHORT + "/";
    public static final String MAP_DIR = "maps/";
    public static final String UNIT_DIR = "units/";
    public static final String TILE_DIR = "tiles/";
    public static final String UNIT_TEXTURE_DIR = "textures/units/";
    public static final String TILE_TEXTURE_DIR = "textures/tiles/";
    public static final String OTHER_TEXTURE_DIR = "textures/other/";
    //public static final String USER_DIR = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/FutureWars";
    public static final List<String> SUB_DIRS = Arrays.asList(
            MAP_DIR,
            UNIT_DIR,
            TILE_DIR,
            UNIT_TEXTURE_DIR,
            TILE_TEXTURE_DIR,
            OTHER_TEXTURE_DIR
    );
    private static final Map<String, String> SHORTCUTS = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(FileHelper.class);

    static {
        SHORTCUTS.put("$USER_DIR", USER_DIR_SHORT);
        SHORTCUTS.put("$INTERNAL_DIR", INTERNAL_DIR_SHORT);
    }

    private FileHelper() {
        // private constructor to prevent instantiation
    }

    public static Image getInternalTexture(String path) {
        Image texture = new Image("file:" + INTERNAL_DIR + "textures/" + path);
        if (texture.isError()) {
            LOGGER.error("Error loading internal texture: {}", texture.getException().getMessage());
        }
        return texture;
    }

    public static URI getFxmlFile(String path) throws CanNotLoadException {
        File file = new File(INTERNAL_DIR + "fxml/" + path);
        return checkIfFileExists(file);
    }


    public static URI getFile(String path) throws CanNotLoadException {
        path = FileHelper.decodePath(path);
        File file = new File(path);
        return checkIfFileExists(file);
    }


    private static URI checkIfFileExists(File file) throws CanNotLoadException {
        if (file.exists()) {
            return file.toURI();
        } else {
            throw new CanNotLoadException("File not found: " + file);
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
        LOGGER.error("Decoding path: {}", path);
        for (Map.Entry<String, String> entry : SHORTCUTS.entrySet()) {
            path = path.replace(entry.getKey(), entry.getValue());
        }
        LOGGER.error("Decoded path: {}", path);
        return path;
    }

    public static Map<String, File> retrieveFiles(Supplier<File> pathSupplier) throws CanNotLoadException {
        LOGGER.info("Loading files for {}...", pathSupplier.get());
        Map<String, File> files = new HashMap<>();
        URI mapPath = checkIfFileExists(pathSupplier.get());

        try {
            Files.walk(Path.of(mapPath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        LOGGER.info("Found file: {}", file);
                        files.put(stripFileExtension(file.getFileName().toString()), file.toFile());
                    });
        } catch (IOException e) {
            LOGGER.error("Error retrieving files: {}", e.getMessage());
        } finally {
            LOGGER.info("Found {} files", files.size());
        }

        return files;
    }

    private static String stripFileExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf('.'));
    }
}
