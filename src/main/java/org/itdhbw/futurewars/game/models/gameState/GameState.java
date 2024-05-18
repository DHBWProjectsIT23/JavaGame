package org.itdhbw.futurewars.game.models.gameState;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

/**
 * The type Game state.
 */
public class GameState {
    private static final org.apache.logging.log4j.Logger LOGGER =
            LogManager.getLogger(GameState.class);
    private final ObjectProperty<TileModel> selectedTile =
            new SimpleObjectProperty<>();
    private final ObjectProperty<UnitModel> selectedUnit =
            new SimpleObjectProperty<>();
    private final ObjectProperty<TileModel> hoveredTile =
            new SimpleObjectProperty<>();
    private final ObjectProperty<ActiveMode> activeMode =
            new SimpleObjectProperty<>(ActiveMode.REGULAR);
    private final IntegerProperty mapHeight = new SimpleIntegerProperty();
    private final IntegerProperty mapWidth = new SimpleIntegerProperty();
    private final IntegerProperty mapHeightTiles = new SimpleIntegerProperty();
    private final IntegerProperty mapWidthTiles = new SimpleIntegerProperty();
    private final IntegerProperty tileSize = new SimpleIntegerProperty();
    private final IntegerProperty currentPlayer = new SimpleIntegerProperty(1);
    private Scene previousScene;
    private Stage primaryStage;

    /**
     * Instantiates a new Game state.
     */
    public GameState() {
    }

    private Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gets current player.
     *
     * @return the current player
     */
    public int getCurrentPlayer() {
        return currentPlayer.get();
    }

    /**
     * End turn.
     */
    public void endTurn() {
        Context.getUnitRepository().getActiveUnits()
               .forEach(unit -> unit.setHasMoved(false));
        currentPlayer.set(currentPlayer.get() == 1 ? 2 : 1);
    }

    /**
     * Gets previous scene.
     *
     * @return the previous scene
     */
    public Scene getPreviousScene() {
        if (previousScene == null) {
            throw new IllegalStateException("No previous scene set");
        }
        return previousScene;
    }

    /**
     * Sets previous scene.
     *
     * @param previousScene the previous scene
     */
    public void setPreviousScene(Scene previousScene) {
        LOGGER.info("Setting previous scene to {}", previousScene);
        this.previousScene = previousScene;
    }

    /**
     * Gets map width.
     *
     * @return the map width
     */
    public int getMapWidth() {
        return mapWidth.get();
    }

    /**
     * Sets map width.
     *
     * @param mapWidth the map width
     */
    public void setMapWidth(int mapWidth) {
        this.mapWidth.set(mapWidth);
    }

    /**
     * Gets map height.
     *
     * @return the map height
     */
    public int getMapHeight() {
        return mapHeight.get();
    }

    /**
     * Sets map height.
     *
     * @param mapHeight the map height
     */
    public void setMapHeight(int mapHeight) {
        this.mapHeight.set(mapHeight);
    }

    /**
     * Sets tile size.
     *
     * @param tileSize the tile size
     */
    public void setTileSize(int tileSize) {
        this.tileSize.set(tileSize);
    }

    /**
     * Gets map height tiles.
     *
     * @return the map height tiles
     */
    public int getMapHeightTiles() {
        return mapHeightTiles.get();
    }

    /**
     * Sets map height tiles.
     *
     * @param mapHeightTiles the map height tiles
     */
    public void setMapHeightTiles(int mapHeightTiles) {
        this.mapHeightTiles.set(mapHeightTiles);
    }

    /**
     * Map height tiles property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty mapHeightTilesProperty() {
        return mapHeightTiles;
    }

    /**
     * Gets map width tiles.
     *
     * @return the map width tiles
     */
    public int getMapWidthTiles() {
        return mapWidthTiles.get();
    }

    /**
     * Sets map width tiles.
     *
     * @param mapWidthTiles the map width tiles
     */
    public void setMapWidthTiles(int mapWidthTiles) {
        this.mapWidthTiles.set(mapWidthTiles);
    }

    /**
     * Map width tiles property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty mapWidthTilesProperty() {
        return mapWidthTiles;
    }

    /**
     * Map height property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty mapHeightProperty() {
        return mapHeight;
    }

    /**
     * Map width property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty mapWidthProperty() {
        return mapWidth;
    }

    /**
     * Tile size property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty tileSizeProperty() {
        return tileSize;
    }

    /**
     * Gets selected unit.
     *
     * @return the selected unit
     */
    public UnitModel getSelectedUnit() {
        if (selectedUnit.get() == null) {
            return null;
        }
        return selectedUnit.get();
    }

    /**
     * Select unit.
     *
     * @param unit the unit
     */
    public void selectUnit(UnitModel unit) {
        selectedUnit.set(unit);
    }

    /**
     * Selected unit property object property.
     *
     * @return the object property
     */
    public ObjectProperty<UnitModel> selectedUnitProperty() {
        return selectedUnit;
    }


    /**
     * Selected tile property object property.
     *
     * @return the object property
     */
    public ObjectProperty<TileModel> selectedTileProperty() {
        return this.selectedTile;
    }

    /**
     * Hovered tile property object property.
     *
     * @return the object property
     */
    public ObjectProperty<TileModel> hoveredTileProperty() {
        return this.hoveredTile;
    }

    /**
     * Active mode property object property.
     *
     * @return the object property
     */
    public ObjectProperty<ActiveMode> activeModeProperty() {
        return this.activeMode;
    }

    /**
     * Sets active mode.
     *
     * @param activeMode the active mode
     */
    public void setActiveMode(ActiveMode activeMode) {
        this.activeMode.set(activeMode);
    }

    /**
     * Select tile.
     *
     * @param tile the tile
     */
    public void selectTile(TileModel tile) {
        this.selectedTile.set(tile);
    }

    /**
     * Deselect tile.
     */
    public void deselectTile() {
        this.selectedTile.set(null);
    }

    /**
     * Hover tile.
     *
     * @param tileView the tile view
     */
    public void hoverTile(TileModel tileView) {
        this.hoveredTile.set(tileView);
    }

    /**
     * Unhover tile.
     */
    public void unhoverTile() {
        this.hoveredTile.set(null);
    }

    /**
     * Current player property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty currentPlayerProperty() {
        return currentPlayer;
    }
}
