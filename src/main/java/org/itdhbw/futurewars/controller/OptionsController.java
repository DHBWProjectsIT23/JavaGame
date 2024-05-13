package org.itdhbw.futurewars.controller;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OptionsController {
    private static final Logger LOGGER = LogManager.getLogger(OptionsController.class);
    private Properties settings;
    private Stage stage;
    private List<String> resolutions;

    public List<String> getResolutions() {
        return resolutions;
    }

    public void saveSettings() {
        try (FileOutputStream output = new FileOutputStream("settings.properties")) {
            settings.store(output, "Settings for Future Wars");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResolution() {
        return settings.getProperty("resolution");
    }

    public String getViewMode() {
        return settings.getProperty("viewMode");
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
        return Integer.parseInt(settings.getProperty("width"));
    }

    public int getCurrentHeight() {
        return Integer.parseInt(settings.getProperty("height"));
    }

    private double calculateAspectRatio() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        return (height / width) * 16;
    }

    public boolean isFullscreen() {
        return settings.getProperty("viewMode").equals("Fullscreen");
    }

    public void setFullscreen(Stage stage) {
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        settings.setProperty("viewMode", "Fullscreen");
    }

    private void loadSettingsFromFile() {
        try {
            settings = new Properties();
            settings.load(Files.newBufferedReader(Path.of("settings.properties")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSettings(Stage stage) {
        loadResolution(stage);
        loadViewMode(stage);
    }

    public void initializeSettings(Stage stage) {
        this.stage = stage;
        if (!Files.exists(Path.of("settings.properties"))) {
            LOGGER.info("Creating new settings file");
            createNewDefaults();
        }
        loadSettingsFromFile();
        loadSettings(stage);
        calculateResolutions();

        stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            settings.setProperty("maximized", String.valueOf(newValue));
        });
    }

    private void loadViewMode(Stage stage) {
        String viewMode = settings.getProperty("viewMode");
        switch (viewMode) {
            case "Fullscreen":
                setFullscreen(stage);
                break;
            case "Windowed":
                setWindowed(stage);
                break;
        }
    }

    private void loadResolution(Stage stage) {
        String resolution = settings.getProperty("resolution");
        String[] parts = resolution.split("x");
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);
        setResolution(stage, width, height);
    }

    public void setWindowed(Stage stage) {
        stage.setFullScreen(false);
        int width = Integer.parseInt(settings.getProperty("width"));
        int height = Integer.parseInt(settings.getProperty("height"));
        setResolution(stage, width, height);
        settings.setProperty("viewMode", "Windowed");
    }

    public void setResolution(Stage stage, String resolution) {
        String[] parts = resolution.split("x");
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);
        setResolution(stage, width, height);
    }

    public void setResolution(Stage stage, int width, int height) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        LOGGER.info("Bounds: {}", bounds);
        int maxWidth = (int) bounds.getWidth();
        int maxHeight = (int) bounds.getHeight();

        if (width >= (maxWidth - 100) || height >= (maxHeight - 100)) {
            LOGGER.info("Setting windowed mode to maximized");
            stage.setMaximized(true);
            width = maxWidth;
            height = maxHeight;
        }

        LOGGER.info("Max width: {}, max height: {}", maxWidth, maxHeight);
        LOGGER.info("Setting resolution to {}x{}", width, height);
        stage.setWidth(width);
        stage.setHeight(height);
        LOGGER.info("Centering stage on screen");
        stage.centerOnScreen();
        settings.setProperty("resolution", width + "x" + height);
        settings.setProperty("width", String.valueOf(width));
        settings.setProperty("height", String.valueOf(height));
    }

    private void createNewDefaults() {
        settings = new Properties();
        settings.setProperty("viewMode", "Fullscreen");
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        settings.setProperty("resolution", (int) bounds.getWidth() + "x" + (int) bounds.getHeight());
        this.saveSettings();
    }
}
