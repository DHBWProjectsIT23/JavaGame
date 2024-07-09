package org.itdhbw.futurewars.game.views;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;

public class TileView extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(TileView.class);
    public final int viewId = this.hashCode();
    protected final Pane possibleMoveOverlay = new Pane();
    protected final Pane possibleMergeOverlay = new Pane();
    protected final ImageView textureLayer;
    private final TileModel tileModel;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final BooleanProperty hovered = new SimpleBooleanProperty(false);
    private final GameState gameState;
    protected ImageView selectedOverlay;
    protected ImageView hoverOverlay;
    private ImageView highlightedOverlay;
    private Image texture;
    private Image hoverImage;
    private Image hoverOccupiedImage;

    public TileView(TileModel tileModel) {
        LOGGER.info("Creating tile view {} for tile {}", this.viewId, tileModel.modelId);
        loadTextures();

        this.gameState = Context.getGameState();
        this.tileModel = tileModel;
        this.textureLayer = new ImageView();
        this.textureLayer.fitWidthProperty().bind(gameState.tileSizeProperty());
        this.textureLayer.fitHeightProperty().bind(gameState.tileSizeProperty());
        this.setTexture();

        possibleMoveOverlay.setMouseTransparent(true);
        possibleMoveOverlay.getStyleClass().add("possible-move-overlay");

        possibleMergeOverlay.setMouseTransparent(true);
        possibleMergeOverlay.getStyleClass().add("possible-merge-overlay");

        hoverOverlay.fitWidthProperty().bind(gameState.tileSizeProperty());
        hoverOverlay.fitHeightProperty().bind(gameState.tileSizeProperty());
        selectedOverlay.fitWidthProperty().bind(gameState.tileSizeProperty());
        selectedOverlay.fitHeightProperty().bind(gameState.tileSizeProperty());
        highlightedOverlay.fitWidthProperty().bind(gameState.tileSizeProperty());
        highlightedOverlay.fitHeightProperty().bind(gameState.tileSizeProperty());

        this.getChildren().add(this.textureLayer);
        this.setUserData(this);
        this.addBindings();
        this.addListeners();
    }

    private void loadTextures() {
        try {
            selectedOverlay = new ImageView(FileHelper.getInternalTexture("other/64Selected.png"));
            texture = FileHelper.getInternalTexture("fallback/FallbackTile.png");
            hoverImage = FileHelper.getInternalTexture("other/64Hovered.png");
            hoverOccupiedImage = FileHelper.getInternalTexture("other/64HoveredOccupied.png");
            hoverOverlay = new ImageView(hoverImage);
            highlightedOverlay = new ImageView(FileHelper.getInternalTexture("other/64Highlighted.png"));
            LOGGER.info("Loaded textures");
        } catch (FailedToLoadTextureException e) {
            ErrorHandler.addException(e, "Failed to load textures");
        }
    }

    private void addBindings() {
        LOGGER.info("Adding hovered binding to tile view {}...", this.viewId);
        this.hovered.bind(Bindings.createBooleanBinding(() -> gameState.hoveredTileProperty().get() == this.tileModel,
                                                        gameState.hoveredTileProperty()));

        LOGGER.info("Adding selected binding to tile view {}...", this.viewId);
        this.selected.bind(Bindings.createBooleanBinding(() -> gameState.selectedTileProperty().get() == this.tileModel,
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

        tileModel.possibleToMergeProperty().addListener((_, _, newValue) -> {
            LOGGER.info("Possible merge for {}: {}", this.viewId, newValue);
            this.setPossibleMerge(Boolean.TRUE.equals(newValue));
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
        hoverOverlay.setImage(this.tileModel.isOccupied() ? hoverOccupiedImage : hoverImage);
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

    public void removeFromStack(Node node) {
        LOGGER.info("Removing node {} from tile view...", node.hashCode());
        this.getChildren().remove(node);
    }

    public void addToStack(Node node) {
        LOGGER.info("Adding node {} to tile view...", node.hashCode());
        this.getChildren().add(node);
    }

    public void setPossibleMove(boolean transparent) {
        if (transparent) {
            this.getChildren().add(possibleMoveOverlay);
        } else {
            this.getChildren().remove(possibleMoveOverlay);
        }
    }

    public void setPossibleMerge(boolean transparent) {
        if (transparent) {
            this.getChildren().add(possibleMergeOverlay);
        } else {
            this.getChildren().remove(possibleMergeOverlay);
        }
    }

    protected void setTexture() {
        this.textureLayer.setImage(this.texture);
    }

    public Image getTexture() {
        return this.texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
        this.textureLayer.setImage(texture);
    }
}
