package org.itdhbw.futurewars.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;

public class TileView extends StackPane {
    private static final Image TEXTURE = new Image("file:resources/textures/UnpassableTile.png");
    private static final Logger LOGGER = LogManager.getLogger(TileView.class);
    private static final Image HOVER_IMAGE = new Image("file:resources/textures/64Hovered.png");
    protected static final ImageView HOVER_OVERLAY = new ImageView(HOVER_IMAGE);
    private static final Image HOVER_OCCUPIED_IMAGE = new Image("file:resources/textures/64HoveredOccupied.png");
    private static final Image SELECTED_IMAGE = new Image("file:resources/textures/64Selected.png");
    protected static final ImageView SELECTED_OVERLAY = new ImageView(SELECTED_IMAGE);
    public final int viewId = this.hashCode();
    protected final Pane possibleMoveOverlay = new Pane();
    protected final ImageView textureLayer;
    private final ImageView highlightedOverlay = new ImageView(new Image("file:resources/textures/64Highlighted.png"));
    private final TileModel tileModel;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final BooleanProperty hovered = new SimpleBooleanProperty(false);
    private final GameState gameState;

    public TileView(TileModel tileModel) {
        LOGGER.info("Creating tile view {} for tile {}", this.viewId, tileModel.modelId);
        this.gameState = Context.getGameState();
        this.tileModel = tileModel;
        this.textureLayer = new ImageView();
        this.textureLayer.fitWidthProperty().bind(gameState.tileSizeProperty());
        this.textureLayer.fitHeightProperty().bind(gameState.tileSizeProperty());
        this.setTexture();

        possibleMoveOverlay.setOpacity(0.2);
        possibleMoveOverlay.setMouseTransparent(true);
        possibleMoveOverlay.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        HOVER_OVERLAY.fitWidthProperty().bind(gameState.tileSizeProperty());
        HOVER_OVERLAY.fitHeightProperty().bind(gameState.tileSizeProperty());
        SELECTED_OVERLAY.fitWidthProperty().bind(gameState.tileSizeProperty());
        SELECTED_OVERLAY.fitHeightProperty().bind(gameState.tileSizeProperty());
        highlightedOverlay.fitWidthProperty().bind(gameState.tileSizeProperty());
        highlightedOverlay.fitHeightProperty().bind(gameState.tileSizeProperty());

        this.getChildren().add(this.textureLayer);
        this.setUserData(this);
        this.addBindings();
        this.addListeners();
    }

    private void addBindings() {
        LOGGER.info("Adding hovered binding to tile view {}...", this.viewId);
        this.hovered.bind(Bindings.createBooleanBinding(
                () -> gameState.hoveredTileProperty().get() == this.tileModel,
                gameState.hoveredTileProperty()
        ));

        LOGGER.info("Adding selected binding to tile view {}...", this.viewId);
        this.selected.bind(Bindings.createBooleanBinding(
                () -> gameState.selectedTileProperty().get() == this.tileModel,
                gameState.selectedTileProperty()
        ));
    }

    private void addListeners() {
        LOGGER.info("Adding hovered listener to tile view {}...", this.viewId);
        this.hovered.addListener((_, _, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                addHoverOverlay();
            } else {
                removeHoverOverlay();
            }
        });

        LOGGER.info("Adding selected listener to tile view {}...", this.viewId);
        selected.addListener((_, _, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                addSelectedOverlay();
            } else {
                removeSelectedOverlay();
            }
        });

        tileModel.partOfPathProperty().addListener((_, _, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                this.getChildren().add(highlightedOverlay);
            } else {
                this.getChildren().remove(highlightedOverlay);
            }
        });

        tileModel.partOfPossiblePathProperty().addListener((_, _, newValue) -> {
            this.setPossibleMove(Boolean.TRUE.equals(newValue));
        });
    }

    public TileModel getTileModel() {
        return tileModel;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public BooleanProperty hoveredProperty() {
        return hovered;
    }

    private void addHoverOverlay() {
        LOGGER.trace("Adding hover overlay to tile {}...", this.viewId);
        HOVER_OVERLAY.setImage(this.tileModel.isOccupied() ? HOVER_OCCUPIED_IMAGE : HOVER_IMAGE);
        this.getChildren().add(HOVER_OVERLAY);
    }

    private void removeHoverOverlay() {
        LOGGER.trace("Removing hover overlay from tile {}...", this.viewId);
        this.getChildren().remove(HOVER_OVERLAY);
    }

    private void addSelectedOverlay() {
        LOGGER.info("Adding selected overlay to tile {}...", this.viewId);
        this.getChildren().add(SELECTED_OVERLAY);
    }

    private void removeSelectedOverlay() {
        LOGGER.info("Removing selected overlay from tile {}...", this.viewId);
        this.getChildren().remove(SELECTED_OVERLAY);
    }

    public void removeFromStack(Node node) {
        LOGGER.info("Removing node {} from tile view...", node.hashCode());
        this.getChildren().remove(node);
    }

    public void addToStack(Node node) {
        LOGGER.info("Adding node {} to tile view...", node.hashCode());
        this.getChildren().add(node);
    }

    public void setPossibleMove(boolean transparent) {
        LOGGER.info("Setting possible move overlay for tile {}...", this.viewId);
        if (transparent) {
            this.getChildren().add(possibleMoveOverlay);
        } else {
            this.getChildren().remove(possibleMoveOverlay);
        }
    }

    protected void setTexture() {
        this.textureLayer.setImage(TileView.TEXTURE);
    }

    public void setTexture(Image texture) {
        this.textureLayer.setImage(texture);
    }
}
