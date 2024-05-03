package org.itdhbw.futurewars.models.tiles;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.itdhbw.futurewars.utils.Position;

import java.util.logging.Logger;

public abstract class Tile {
    private static final Logger LOGGER = Logger.getLogger(Tile.class.getName());
    private static final int TILE_SIZE = 64;
    private static final Image TEXTURE = new Image("file:resources/textures/64Sample.png");
    private static final Image HOVER = new Image("file:resources/textures/64Selected.png");
    protected final ImageView view;
    protected final ImageView hoverOverlay;
    private final Position position;
    protected StackPane stackPane = new StackPane();

    protected Tile(final int x, final int y) {
        LOGGER.info("Creating tile at " + y + "-" + x + "...");
        this.position = new Position(x, y, true);

        this.hoverOverlay = new ImageView(HOVER);
        this.hoverOverlay.setFitHeight(TILE_SIZE);
        this.hoverOverlay.setFitWidth(TILE_SIZE);

        this.view = new ImageView();
        this.view.setId(getId());
        this.view.setFitWidth(TILE_SIZE);
        this.view.setFitHeight(TILE_SIZE);
        setTexture();

        this.stackPane = new StackPane();
        this.stackPane.getChildren().add(this.view);
        this.stackPane.setOnMouseClicked(event -> LOGGER.info("Tile clicked: " + getId()));
        this.stackPane.setOnMouseEntered(handleMouseEnter());
        this.stackPane.setOnMouseExited(handleMouseExit());
    }

    protected void setTexture() {
        view.setImage(TEXTURE);
    }


    private EventHandler<MouseEvent> handleMouseExit() {
        return event -> {
            LOGGER.info("Tile exited: " + getId());
            this.stackPane.getChildren().remove(this.hoverOverlay);
        };
    }

    private EventHandler<MouseEvent> handleMouseEnter() {
        return event -> {
            LOGGER.info("Tile entered: " + getId());
            this.stackPane.getChildren().add(this.hoverOverlay);
        };
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public ImageView getView() {
        LOGGER.info("Returning tile view...");
        return view;
    }

    public String getId() {
        return "tile" + position.getY() + "-" + position.getX();
    }

    public Position getPosition() {
        return position;
    }
}
