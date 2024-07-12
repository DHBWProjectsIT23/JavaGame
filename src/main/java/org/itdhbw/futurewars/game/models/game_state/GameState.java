package org.itdhbw.futurewars.game.models.game_state;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.exceptions.NoUnitSelectedException;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

import java.util.Optional;

public class GameState {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(GameState.class);
    private final ObjectProperty<TileModel> selectedTile = new SimpleObjectProperty<>();
    private final ObjectProperty<Optional<UnitModel>> selectedUnit = new SimpleObjectProperty<>(Optional.empty());
    private final ObjectProperty<TileModel> hoveredTile = new SimpleObjectProperty<>();
    private final ObjectProperty<ActiveMode> activeMode = new SimpleObjectProperty<>(ActiveMode.REGULAR_MODE);
    private final IntegerProperty mapHeight = new SimpleIntegerProperty();
    private final IntegerProperty mapWidth = new SimpleIntegerProperty();
    private final IntegerProperty mapHeightTiles = new SimpleIntegerProperty();
    private final IntegerProperty mapWidthTiles = new SimpleIntegerProperty();
    private final IntegerProperty tileSize = new SimpleIntegerProperty();
    private final IntegerProperty currentPlayer = new SimpleIntegerProperty(1);
    private final IntegerProperty currentDay = new SimpleIntegerProperty(1);
    private Parent previousRoot;

    public GameState() {
        currentPlayer.addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Current player changed to {}", newValue);
            if (oldValue.equals(2) && newValue.equals(1)) {
                currentDay.set(currentDay.get() + 1);
            }
        });
    }

    public IntegerProperty currentDayProperty() {
        return currentDay;
    }

    public int getCurrentPlayer() {
        return currentPlayer.get();
    }

    public void endTurn() {
        Context.getUnitRepository().getActiveUnits().forEach(unit -> unit.setHasMadeAnAction(false));
        currentPlayer.set(currentPlayer.get() == 1 ? 2 : 1);
    }

    public Parent getPreviousRoot() throws IllegalStateException {
        if (previousRoot == null) {
            throw new IllegalStateException("No previous scene set");
        }
        return previousRoot;
    }

    public void setPreviousRoot(Parent previousRoot) {
        LOGGER.info("Setting previous scene to {}", previousRoot);
        this.previousRoot = previousRoot;
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

    public void resetGame() {
        this.selectedTile.set(null);
        this.selectedUnit.set(Optional.empty());
        this.hoveredTile.set(null);
        this.activeMode.set(ActiveMode.REGULAR_MODE);
        this.currentPlayer.set(1);
        this.currentDay.set(1);

        Context.getUnitRepository().reset();
    }

    public UnitModel getSelectedUnit() throws NoUnitSelectedException {
        if (selectedUnit.get().isEmpty()) {
            throw new NoUnitSelectedException();
        }
        return selectedUnit.get().get();
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

    public void selectUnit(UnitModel unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        selectedUnit.set(Optional.of(unit));
    }

    public void deselectUnit() {
        selectedUnit.set(Optional.empty());
    }

    public ObjectProperty<TileModel> selectedTileProperty() {
        return this.selectedTile;
    }

    public ObjectProperty<TileModel> hoveredTileProperty() {
        return this.hoveredTile;
    }

    public TileModel getHoveredTile() {
        return hoveredTile.get();
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

    public TileModel getSelectedTile() {
        return selectedTile.get();
    }
}
