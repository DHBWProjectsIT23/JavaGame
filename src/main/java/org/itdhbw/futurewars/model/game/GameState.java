package org.itdhbw.futurewars.model.game;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.itdhbw.futurewars.model.tile.TileModel;

public class GameState {
    private final ObjectProperty<TileModel> selectedTile = new SimpleObjectProperty<>();
    private final ObjectProperty<TileModel> hoveredTile = new SimpleObjectProperty<>();
    private final ObjectProperty<ActiveMode> activeMode = new SimpleObjectProperty<>(ActiveMode.REGULAR);
    private int mapWidth;
    private int mapHeight;

    public GameState() {
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public ObjectProperty<TileModel> getSelectedTileProperty() {
        return this.selectedTile;
    }

    public ObjectProperty<TileModel> getHoveredTileProperty() {
        return this.hoveredTile;
    }

    public ObjectProperty<ActiveMode> activeModeProperty() {
        return this.activeMode;
    }

    public void setActiveMode(ActiveMode activeMode) {
        this.activeMode.set(activeMode);
    }

    public void selectTile(TileModel tile) {
        this.selectedTile.set(tile);
    }

    public void deselectTile() {
        this.selectedTile.set(null);
    }

    public void hoverTile(TileModel tileView) {
        this.hoveredTile.set(tileView);
    }

    public void unhoverTile() {
        this.hoveredTile.set(null);
    }

}
