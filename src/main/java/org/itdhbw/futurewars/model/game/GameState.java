package org.itdhbw.futurewars.model.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;

public class GameState {
    private final ObjectProperty<TileModel> selectedTile = new SimpleObjectProperty<>();
    private final ObjectProperty<UnitModel> selectedUnit = new SimpleObjectProperty<>();
    private final ObjectProperty<TileModel> hoveredTile = new SimpleObjectProperty<>();
    private final ObjectProperty<ActiveMode> activeMode = new SimpleObjectProperty<>(ActiveMode.REGULAR);
    private final IntegerProperty mapHeight = new SimpleIntegerProperty();
    private final IntegerProperty mapWidth = new SimpleIntegerProperty();
    private final IntegerProperty mapHeightTiles = new SimpleIntegerProperty();
    private final IntegerProperty mapWidthTiles = new SimpleIntegerProperty();
    private final IntegerProperty tileSize = new SimpleIntegerProperty();
    private Scene previousScene;

    public Scene getPreviousScene() {
        return previousScene;
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public GameState() {
    }

    public int getMapWidth() {
        return mapWidth.get();
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth.set(mapWidth);
    }

    public int getMapHeight() {
        return mapHeight.get();
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight.set(mapHeight);
    }

    public void setTileSize(int tileSize) {
        this.tileSize.set(tileSize);
    }

    public int getMapHeightTiles() {
        return mapHeightTiles.get();
    }

    public void setMapHeightTiles(int mapHeightTiles) {
        this.mapHeightTiles.set(mapHeightTiles);
    }

    public IntegerProperty mapHeightTilesProperty() {
        return mapHeightTiles;
    }

    public int getMapWidthTiles() {
        return mapWidthTiles.get();
    }

    public void setMapWidthTiles(int mapWidthTiles) {
        this.mapWidthTiles.set(mapWidthTiles);
    }

    public IntegerProperty mapWidthTilesProperty() {
        return mapWidthTiles;
    }

    public IntegerProperty mapHeightProperty() {
        return mapHeight;
    }

    public IntegerProperty mapWidthProperty() {
        return mapWidth;
    }

    public IntegerProperty tileSizeProperty() {
        return tileSize;
    }

    public UnitModel getSelectedUnit() {
        if (selectedUnit.get() == null) {
            return null;
        }
        return selectedUnit.get();
    }

    public void selectUnit(UnitModel unit) {
        selectedUnit.set(unit);
    }

    public ObjectProperty<UnitModel> selectedUnitProperty() {
        return selectedUnit;
    }


    public ObjectProperty<TileModel> selectedTileProperty() {
        return this.selectedTile;
    }

    public ObjectProperty<TileModel> hoveredTileProperty() {
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
