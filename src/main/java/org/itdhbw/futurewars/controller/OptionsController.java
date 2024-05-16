package org.itdhbw.futurewars.controller;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OptionsController {
    private static final Logger LOGGER = LogManager.getLogger(OptionsController.class);
    private static final String SETTINGS_FILE = "settings.properties";
    private static final String RESOLUTION = "resolution";
    private static final String VIEW_MODE = "viewMode";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String FULLSCREEN = "Fullscreen";
    private static final String WINDOWED = "Windowed";
    private Properties settings;
    private Stage stage;
    private List<String> resolutions;

    public List<String> getResolutions() {
        return resolutions;
    }

    public void saveSettings() {
        try (FileOutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            settings.store(output, "Settings for Future Wars");
        } catch (Exception e) {
            LOGGER.error("Failed to save settings", e);
        }
    }

    public String getResolution() {
        return settings.getProperty(RESOLUTION);
    }

    public void setResolution(String resolution) {
        String[] parts = resolution.split("x");
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);
        setResolution(width, height);
    }

    public String getViewMode() {
        return settings.getProperty(VIEW_MODE);
    }

    private void calculateResolutions() {
        int[] widths = {800, 1024, 1280, 1366, 1440, 1600, 1920, 2560, 3840};
        int[] heights = {600, 768, 720, 800, 900, 1024, 1080, 1440, 2160};
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        resolutions = new ArrayList<>();
        double aspectRatio = calculateAspectRatio();

        for (int i = 0; i < widths.length; i++) {
            int width = widths[i];
            int height = heights[i];

            if (height > bounds.getHeight() || width > bounds.getWidth()) {
                height = (int) (widths[i - 1] * aspectRatio / 16);
                resolutions.add(widths[i - 1] + "x" + height);
                break;
            }

            if (i > 0) {
                resolutions.add(widths[i - 1] + "x" + heights[i - 1]);
            }
        }

    }

    public int getCurrentWidth() {
        return Integer.parseInt(settings.getProperty(WIDTH));
    }

    public int getCurrentHeight() {
        return Integer.parseInt(settings.getProperty(WIDTH));
    }

    private double calculateAspectRatio() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        return (height / width) * 16;
    }

    public boolean isFullscreen() {
        return settings.getProperty(VIEW_MODE).equals(FULLSCREEN);
    }

    public void setFullscreen() {
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        settings.setProperty(VIEW_MODE, FULLSCREEN);
    }

    private void loadSettingsFromFile() {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(SETTINGS_FILE))) {
            settings = new Properties();
            settings.load(reader);
        } catch (Exception e) {
            LOGGER.error("Failed to load settings from file", e);
        }
    }

    public void loadSettings() {
        loadResolution();
        loadViewMode();
    }

    public void initializeSettings(Stage stage) {
        this.stage = stage;
        if (!Files.exists(Path.of(SETTINGS_FILE))) {
            LOGGER.info("Creating new settings file");
            createNewDefaults();
        }
        loadSettingsFromFile();
        loadSettings();
        calculateResolutions();

        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> settings.setProperty("maximized", String.valueOf(newValue)));
    }

    private void loadViewMode() {
        String viewMode = settings.getProperty(VIEW_MODE);
        switch (viewMode) {
            case FULLSCREEN:
                setFullscreen();
                break;
            case WINDOWED:
                setWindowed();
                break;
            default:
                LOGGER.error("Invalid view mode: {}", viewMode);
        }
    }

    private void loadResolution() {
        String resolution = settings.getProperty(RESOLUTION);
        String[] parts = resolution.split("x");
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);
        setResolution(width, height);
    }

    public void setWindowed() {
        stage.setFullScreen(false);
        int width = Integer.parseInt(settings.getProperty(WIDTH));
        int height = Integer.parseInt(settings.getProperty(HEIGHT));
        setResolution(width, height);
        settings.setProperty(VIEW_MODE, WINDOWED);
    }

    public void setResolution(int width, int height) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        //LOGGER.info("Bounds: {}", bounds);
        int maxWidth = (int) bounds.getWidth();
        int maxHeight = (int) bounds.getHeight();

        if (width >= (maxWidth - 100) || height >= (maxHeight - 100)) {
            //LOGGER.info("Setting windowed mode to maximized");
            stage.setMaximized(true);
            width = maxWidth;
            height = maxHeight;
        }

        //LOGGER.info("Max width: {}, max height: {}", maxWidth, maxHeight);
        //LOGGER.info("Setting resolution to {}x{}", width, height);
        stage.setWidth(width);
        stage.setHeight(height);
        //LOGGER.info("Centering stage on screen");
        stage.centerOnScreen();
        settings.setProperty(RESOLUTION, width + "x" + height);
        settings.setProperty(WIDTH, String.valueOf(width));
        settings.setProperty(HEIGHT, String.valueOf(height));
    }

    private void createNewDefaults() {
        settings = new Properties();
        settings.setProperty(VIEW_MODE, FULLSCREEN);
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        settings.setProperty(RESOLUTION, (int) bounds.getWidth() + "x" + (int) bounds.getHeight());
        this.saveSettings();
    }
}
