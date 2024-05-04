package org.itdhbw.futurewars.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.Tile;

public class TileOverlayView {
    private static final Logger LOGGER = LogManager.getLogger(TileOverlayView.class);
    private static final Image HOVER = new Image("file:resources/textures/64Hovered.png");
    protected static final ImageView HOVER_OVERLAY = new ImageView(HOVER);
    private static final Image HOVER_OCCUPIED = new Image("file:resources/textures/64HoveredOccupied.png");
    private static final Image SELECTED = new Image("file:resources/textures/64Selected.png");
    protected static final ImageView SELECTED_OVERLAY = new ImageView(SELECTED);
    private TileOverlayView() {
        //private constructor to hide the implicit public one
    }

    public static void addHoverOverlay(Tile tile) {
        LOGGER.trace("Adding hover overlay to tile {}...", tile.getId());
        HOVER_OVERLAY.setImage(tile.isOccupied() ? HOVER_OCCUPIED : HOVER);
        tile.addToStackPane(HOVER_OVERLAY);
    }

    public static void removeHoverOverlay(Tile tile) {
        LOGGER.trace("Removing hover overlay from tile {}...", tile.getId());
        tile.removeFromStackPane(HOVER_OVERLAY);
    }

    public static void addSelectedOverlay(Tile tile) {
        LOGGER.info("Adding selected overlay to tile {}...", tile.getId());
        tile.addToStackPane(SELECTED_OVERLAY);
    }

    public static void removeSelectedOverlay(Tile tile) {
        LOGGER.info("Removing selected overlay from tile {}...", tile.getId());
        tile.removeFromStackPane(SELECTED_OVERLAY);
    }
}
