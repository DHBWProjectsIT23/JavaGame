package org.itdhbw.futurewars.game.views;

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
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.ErrorPopup;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;

/**
 * The type Tile view.
 */
public class TileView extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(TileView.class);
    /**
     * The View id.
     */
    public final int viewId = this.hashCode();
    /**
     * The Possible move overlay.
     */
    protected final Pane possibleMoveOverlay = new Pane();
    /**
     * The Texture layer.
     */
    protected final ImageView textureLayer;
    private final TileModel tileModel;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final BooleanProperty hovered = new SimpleBooleanProperty(false);
    private final GameState gameState;
    /**
     * The Selected overlay.
     */
    protected ImageView selectedOverlay;
    /**
     * The Hover overlay.
     */
    protected ImageView hoverOverlay;
    private ImageView highlightedOverlay;
    private Image texture;
    private Image hoverImage;
    private Image hoverOccupiedImage;

    /**
     * Instantiates a new Tile view.
     *
     * @param tileModel the tile model
     */
    public TileView(TileModel tileModel) {
        LOGGER.info("Creating tile view {} for tile {}", this.viewId,
                tileModel.modelId);
        loadTextures();

        this.gameState = Context.getGameState();
        this.tileModel = tileModel;
        this.textureLayer = new ImageView();
        this.textureLayer.fitWidthProperty().bind(gameState.tileSizeProperty());
        this.textureLayer.fitHeightProperty()
                .bind(gameState.tileSizeProperty());
        this.setTexture();

        possibleMoveOverlay.setOpacity(0.2);
        possibleMoveOverlay.setMouseTransparent(true);
        possibleMoveOverlay.setBackground(new Background(
                new BackgroundFill(Color.RED, CornerRadii.EMPTY,
                        Insets.EMPTY)));

        hoverOverlay.fitWidthProperty().bind(gameState.tileSizeProperty());
        hoverOverlay.fitHeightProperty().bind(gameState.tileSizeProperty());
        selectedOverlay.fitWidthProperty().bind(gameState.tileSizeProperty());
        selectedOverlay.fitHeightProperty().bind(gameState.tileSizeProperty());
        highlightedOverlay.fitWidthProperty()
                .bind(gameState.tileSizeProperty());
        highlightedOverlay.fitHeightProperty()
                .bind(gameState.tileSizeProperty());

        this.getChildren().add(this.textureLayer);
        this.setUserData(this);
        this.addBindings();
        this.addListeners();
    }

    private void loadTextures() {
        try {
            selectedOverlay = new ImageView(
                    FileHelper.getInternalTexture("other/64Selected.png"));
            texture = FileHelper.getInternalTexture("tiles/misc/TileNotSet.png");
            hoverImage = FileHelper.getInternalTexture("other/64Hovered.png");
            hoverOccupiedImage = FileHelper.getInternalTexture(
                    "other/64HoveredOccupied.png");
            hoverOverlay = new ImageView(hoverImage);
            highlightedOverlay = new ImageView(
                    FileHelper.getInternalTexture("other/64Highlighted.png"));
        } catch (FailedToLoadTextureException e) {
            ErrorHandler.addException(e, "Failed to load textures");
        }
    }

    private void addBindings() {
        LOGGER.info("Adding hovered binding to tile view {}...", this.viewId);
        this.hovered.bind(Bindings.createBooleanBinding(
                () -> gameState.hoveredTileProperty().get() == this.tileModel,
                gameState.hoveredTileProperty()));

        LOGGER.info("Adding selected binding to tile view {}...", this.viewId);
        this.selected.bind(Bindings.createBooleanBinding(
                () -> gameState.selectedTileProperty().get() == this.tileModel,
                gameState.selectedTileProperty()));
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

    /**
     * Gets tile model.
     *
     * @return the tile model
     */
    public TileModel getTileModel() {
        return tileModel;
    }

    /**
     * Selected property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * Hovered property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty hoveredProperty() {
        return hovered;
    }

    private void addHoverOverlay() {
        LOGGER.trace("Adding hover overlay to tile {}...", this.viewId);
        hoverOverlay.setImage(
                this.tileModel.isOccupied() ? hoverOccupiedImage : hoverImage);
        this.getChildren().add(hoverOverlay);
    }

    private void removeHoverOverlay() {
        LOGGER.trace("Removing hover overlay from tile {}...", this.viewId);
        this.getChildren().remove(hoverOverlay);
    }

    private void addSelectedOverlay() {
        LOGGER.info("Adding selected overlay to tile {}...", this.viewId);
        this.getChildren().add(selectedOverlay);
    }

    private void removeSelectedOverlay() {
        LOGGER.info("Removing selected overlay from tile {}...", this.viewId);
        this.getChildren().remove(selectedOverlay);
    }

    /**
     * Remove from stack.
     *
     * @param node the node
     */
    public void removeFromStack(Node node) {
        LOGGER.info("Removing node {} from tile view...", node.hashCode());
        this.getChildren().remove(node);
    }

    /**
     * Add to stack.
     *
     * @param node the node
     */
    public void addToStack(Node node) {
        LOGGER.info("Adding node {} to tile view...", node.hashCode());
        this.getChildren().add(node);
    }

    /**
     * Sets possible move.
     *
     * @param transparent the transparent
     */
    public void setPossibleMove(boolean transparent) {
        LOGGER.info("Setting possible move overlay for tile {}...",
                this.viewId);
        if (transparent) {
            this.getChildren().add(possibleMoveOverlay);
        } else {
            this.getChildren().remove(possibleMoveOverlay);
        }
    }

    /**
     * Sets texture.
     */
    protected void setTexture() {
        this.textureLayer.setImage(this.texture);
    }

    /**
     * Sets texture.
     *
     * @param texture the texture
     */
    public void setTexture(Image texture) {
        this.textureLayer.setImage(texture);
    }
}
