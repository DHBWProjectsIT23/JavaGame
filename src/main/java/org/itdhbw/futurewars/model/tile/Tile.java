package org.itdhbw.futurewars.model.tile;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.itdhbw.futurewars.service.SelectedTileService;
import org.itdhbw.futurewars.util.Position;

import java.util.logging.Logger;

public abstract class Tile {
    private static final Logger LOGGER = Logger.getLogger(Tile.class.getName());
    private static final Image TEXTURE = new Image("file:resources/textures/64Sample.png");
    private static final Image HOVER = new Image("file:resources/textures/64Selected.png");
    protected final ImageView view;
    protected final ImageView hoverOverlay;
    private final Position position;
    protected StackPane stackPane = new StackPane();
    private final TileType tyleType;
    private Boolean isHovered = false;

    protected Tile(final int x, final int y, final double tileSize, TileType tileType) {
        LOGGER.info("Creating tile at " + y + "-" + x + "...");

        this.tyleType = tileType;
        // Setting position
        this.position = new Position(x, y, true);

        // Used if tile is hovered over
        this.hoverOverlay = new ImageView(HOVER);
        this.hoverOverlay.setFitHeight(tileSize);
        this.hoverOverlay.setFitWidth(tileSize);

        // texture of the tile itself
        this.view = new ImageView();
        this.view.setId(getId());
        this.view.setFitWidth(tileSize);
        this.view.setFitHeight(tileSize);
        setTexture();

        // stack pane displaying tile and hovered state
        this.stackPane = new StackPane();
        this.stackPane.getChildren().add(this.view);
        this.stackPane.setOnMouseClicked(handleMouseClick());
        this.stackPane.setOnMouseEntered(handleMouseEnter());
        this.stackPane.setOnMouseExited(handleMouseExit());
    }

    @Override
    public String toString() {
        return "Tile{" +
                       "type=" + tyleType +
                       ", position=" + position +
                       ", isHovered=" + isHovered +
                       ", view=" + view +
                       ", hoverOverlay=" + hoverOverlay +
                       ", stackPane=" + stackPane +
                       '}';
    }

    protected void setTexture() {
        view.setImage(TEXTURE);
    }

    private EventHandler<MouseEvent> handleMouseClick() {
        return _ -> {
            LOGGER.info("Tile clicked: " + this);
            SelectedTileService.getInstance().setSelectedTileMessage("Tile clicked: " + getId());
        };
    }

    private EventHandler<MouseEvent> handleMouseExit() {
        return _ -> {
            LOGGER.info("Tile exited: " + getId());
            this.isHovered = true;
            this.stackPane.getChildren().remove(this.hoverOverlay);
        };
    }

    private EventHandler<MouseEvent> handleMouseEnter() {
        return _ -> {
            LOGGER.info("Tile entered: " + getId());
            this.isHovered = true;
            this.stackPane.getChildren().add(this.hoverOverlay);
        };
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public String getId() {
        return "tile" + position.getY() + "-" + position.getX();
    }
}
