package org.itdhbw.futurewars.model.game;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.itdhbw.futurewars.model.tile.Tile;

public class GameState {
    private static final GameState INSTANCE = new GameState();
    private final ObjectProperty<Tile> selectedTile = new SimpleObjectProperty<>();
    private final ObjectProperty<Tile> hoveredTile = new SimpleObjectProperty<>();
    private ActiveMode activeMode = ActiveMode.REGULAR;

    private GameState() {
    }

    public static GameState getInstance() {
        return INSTANCE;
    }

    public static ObjectProperty<Tile> getSelectedTileProperty() {
        return INSTANCE.selectedTile;
    }

    public static ObjectProperty<Tile> getHoveredTileProperty() {
        return INSTANCE.hoveredTile;
    }

    public static ActiveMode getActiveMode() {
        return getInstance().activeMode;
    }

    public void setActiveMode(ActiveMode activeMode) {
        this.activeMode = activeMode;
    }

    public void selectTile(Tile tile) {
        selectedTile.set(tile);
    }

    public void deselectTile() {
        this.selectedTile.set(null);
    }

    public void hoverTile(Tile tile) {
        hoveredTile.set(tile);
    }

    public void unhoverTile() {
        this.hoveredTile.set(null);
    }

}
