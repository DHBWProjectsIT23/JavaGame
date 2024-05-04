package org.itdhbw.futurewars.model.tile;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.itdhbw.futurewars.controller.TileController;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.unit.Unit;
import org.itdhbw.futurewars.util.Constants;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.TileOverlayView;

public abstract class Tile {
    private final Position position;
    protected final ImageView textureLayer;
    private final TileType tileType;
    private final ObjectProperty<Unit> occupyingUnit = new SimpleObjectProperty<>(null);
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final BooleanProperty hovered = new SimpleBooleanProperty(false);
    private boolean isOccupied = false;
    protected StackPane stackPane = new StackPane();

    protected Tile(final int x, final int y, TileType tileType) {
        this.tileType = tileType;
        this.position = new Position(x, y, true);

        this.textureLayer = new ImageView();
        this.textureLayer.setId(getId());
        this.textureLayer.setFitHeight(Constants.TILE_SIZE);
        this.textureLayer.setFitWidth(Constants.TILE_SIZE);
        setTexture();

        this.stackPane = new StackPane();
        this.stackPane.getChildren().add(this.textureLayer);
        this.stackPane.setOnMouseClicked(TileController::handleMouseClick);
        this.stackPane.setOnMouseEntered(TileController::handleMouseEntered);
        this.stackPane.setUserData(this);

        this.hovered.bind(Bindings.createBooleanBinding(
                () -> GameState.getHoveredTileProperty().get() == this,
                GameState.getHoveredTileProperty()
        ));

        this.hovered.addListener((_, _, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                TileOverlayView.addHoverOverlay(this);
            } else {
                TileOverlayView.removeHoverOverlay(this);
            }
        });

        this.selected.bind(Bindings.createBooleanBinding(
                () -> GameState.getSelectedTileProperty().get() == this,
                GameState.getSelectedTileProperty()
        ));

        selected.addListener((_, _, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                TileOverlayView.addSelectedOverlay(this);
            } else {
                TileOverlayView.removeSelectedOverlay(this);
            }
        });
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void removeOccupyingUnit() {
        this.isOccupied = false;
        Unit unit = this.occupyingUnit.get();
        if (unit != null) {
            this.stackPane.getChildren().remove(unit.getTextureLayer());
        }
        this.occupyingUnit.set(null);
    }

    public void addToStackPane(ImageView imageView) {
        stackPane.getChildren().add(imageView);
    }

    public void removeFromStackPane(ImageView imageView) {
        stackPane.getChildren().remove(imageView);
    }

    public ObjectProperty<Unit> getOccupyingUnit() {
        return occupyingUnit;
    }

    public String getId() {
        return "tile" + position.getY() + "-" + position.getX();
    }

    public void setOccupyingUnit(Unit unit) {
        this.occupyingUnit.set(unit);
        this.isOccupied = true;
        this.addToStackPane(unit.getTextureLayer());
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public BooleanProperty hoveredProperty() {
        return hovered;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    protected abstract void setTexture();
}
