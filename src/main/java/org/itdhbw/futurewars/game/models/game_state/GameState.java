package org.itdhbw.futurewars.game.models.game_state;

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

import java.util.Optional;

public class GameState {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(GameState.class);
    private final ObjectProperty<TileModel> selectedTile = new SimpleObjectProperty<>();
    private final ObjectProperty<Optional<UnitModel>> selectedUnit = new SimpleObjectProperty<>(Optional.empty());
    private final ObjectProperty<TileModel> hoveredTile = new SimpleObjectProperty<>();
    private final ObjectProperty<ActiveMode> activeMode = new SimpleObjectProperty<>(ActiveMode.REGULAR);
    private final IntegerProperty mapHeight = new SimpleIntegerProperty();
    private final IntegerProperty mapWidth = new SimpleIntegerProperty();
    private final IntegerProperty mapHeightTiles = new SimpleIntegerProperty();
    private final IntegerProperty mapWidthTiles = new SimpleIntegerProperty();
    private final IntegerProperty tileSize = new SimpleIntegerProperty();
    private final IntegerProperty currentPlayer = new SimpleIntegerProperty(1);


    private final IntegerProperty currentDay = new SimpleIntegerProperty(1);
    private Scene previousScene;
    private Stage primaryStage;

    public GameState() {
        currentPlayer.addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Current player changed to {}", newValue);
            if (oldValue.equals(2) && newValue.equals(1)) {
                currentDay.set(currentDay.get() + 1);
            }
        });
    }

    public int getCurrentDay() {
        return currentDay.get();
    }

    public IntegerProperty currentDayProperty() {
        return currentDay;
    }

    private Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public int getCurrentPlayer() {
        return currentPlayer.get();
    }

    public void endTurn() {
        Context.getUnitRepository().getActiveUnits().forEach(unit -> unit.setHasMoved(false));
        currentPlayer.set(currentPlayer.get() == 1 ? 2 : 1);
    }

    //!TODO: Custom exception
    public Scene getPreviousScene() throws IllegalStateException {
        if (previousScene == null) {
            throw new IllegalStateException("No previous scene set");
        }
        return previousScene;
    }

    public void setPreviousScene(Scene previousScene) {
        LOGGER.info("Setting previous scene to {}", previousScene);
        this.previousScene = previousScene;
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

    public Optional<UnitModel> getSelectedUnit() {
        //!TODO: Dont return null
        return selectedUnit.get();
    }

    public void selectUnit(Optional<UnitModel> unit) {
        selectedUnit.set(unit);
    }

    public ObjectProperty<Optional<UnitModel>> selectedUnitProperty() {
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

    public IntegerProperty currentPlayerProperty() {
        return currentPlayer;
    }
}
