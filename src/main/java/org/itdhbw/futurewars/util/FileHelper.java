package org.itdhbw.futurewars.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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

    public static Optional<URI> getFile(String path) {
        path = FileHelper.decodePath(path);
        File file = new File(path);
        if (file.exists()) {
            try {
                return Optional.of(file.toURI());
            } catch (Exception e) {
                LOGGER.error("Error getting file: {}", e.getMessage());
                return Optional.empty();
            }
        } else {
            LOGGER.error("File does not exist: {}", path);
            return Optional.empty();
        }
    }

    static {
        SHORTCUTS.put("$USER_DIR", USER_DIR_SHORT);
        SHORTCUTS.put("$INTERNAL_DIR", INTERNAL_DIR_SHORT);
    }

    private FileHelper() {
        // private constructor to prevent instantiation
    }

    public static URI getInternalUnitPath() {
        try {
            return new File(INTERNAL_DIR + UNIT_DIR).toURI();
        } catch (Exception e) {
            LOGGER.error("Error getting internal unit path: {}", e.getMessage());
            return null;
        }
    }




    public static URI getInternalPath(String dir) {
        try {
            return Objects.requireNonNull(FileHelper.class.getResource(INTERNAL_DIR + dir)).toURI();
        } catch (Exception e) {
            LOGGER.error("Error getting internal path: {}", e.getMessage());
            return null;
        }
    }

    public static String getInternalPathString(String dir) {
        return String.valueOf(FileHelper.getInternalPath(dir));
    }

    public static URI getInternalMapPath() {
        try {
            return Objects.requireNonNull(FileHelper.class.getResource(INTERNAL_DIR + MAP_DIR)).toURI();
        } catch (Exception e) {
            LOGGER.error("Error getting internal map path: {}", e.getMessage());
            return null;
        }
    }

    public static URI getInternalTilePath() {
        try {
            return new File(INTERNAL_DIR + TILE_DIR).toURI();
        } catch (Exception e) {
            LOGGER.error("Error getting internal tile path: {}", e.getMessage());
            return null;
        }
    }

    public enum FileLocation {
        INTERNAL,
        USER
    }

    public static URI getUserMapPath() {
        try {
            return new File(USER_DIR + MAP_DIR).toURI();
        } catch (Exception e) {
            LOGGER.error("Error getting user map path: {}", e.getMessage());
            return null;
        }
    }

    public static URI getUserUnitPath() {
        try {
            return new File(USER_DIR + UNIT_DIR).toURI();
        } catch (Exception e) {
            LOGGER.error("Error getting user unit path: {}", e.getMessage());
            return null;
        }
    }

    public static URI getUserTilePath() {
        try {
            return new File(USER_DIR + TILE_DIR).toURI();
        } catch (Exception e) {
            LOGGER.error("Error getting user tile path: {}", e.getMessage());
            return null;
        }
    }

    public static String decodePath(String path) {
        LOGGER.error("Decoding path: {}", path);
        for (Map.Entry<String, String> entry : SHORTCUTS.entrySet()) {
            path = path.replace(entry.getKey(), entry.getValue());
        }
        LOGGER.error("Decoded path: {}", path);
        return path;
    }

    public static Map<String, File> retrieveFiles(Supplier<URI> pathSupplier) {
        LOGGER.info("Loading files for {}...", pathSupplier.get());
        Map<String, File> files = new HashMap<>();
        URI mapPath = pathSupplier.get();
        if (mapPath == null) {
            LOGGER.error("Error retrieving files: path is null");
            return files;
        }

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
