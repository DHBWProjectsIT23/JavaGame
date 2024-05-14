package org.itdhbw.futurewars.controller.tile;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.mouse_events.AttackingUnitModeHandler;
import org.itdhbw.futurewars.controller.tile.mouse_events.MouseEventHandler;
import org.itdhbw.futurewars.controller.tile.mouse_events.MovingUnitModeHandler;
import org.itdhbw.futurewars.controller.tile.mouse_events.RegularModeHandler;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.util.AStarPathfinder;
import org.itdhbw.futurewars.view.TileView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

//! BUG: Hovering over a tile that is occupied throws an NullPointerException in Moving Unit Mode
public class TileEventController {
    private static final Logger LOGGER = LogManager.getLogger(TileEventController.class);
    private static final List<TileModel> highlightedPossibleTiles = new ArrayList<>();
    private static final List<TileModel> highlightedAttackTiles = new ArrayList<>();
    private final EnumMap<ActiveMode, MouseEventHandler> mouseEventHandlers;
    private GameState gameState;
    private AStarPathfinder pathfinder;
    private UnitMovementController unitMovementController;

    public TileEventController() {
        this.mouseEventHandlers = new EnumMap<>(ActiveMode.class);
    }

    public void initialize() {
        this.gameState = Context.getGameState();
        this.pathfinder = Context.getPathfinder();
        this.unitMovementController = Context.getUnitMovementController();
        this.mouseEventHandlers.put(ActiveMode.REGULAR, new RegularModeHandler(gameState));
        this.mouseEventHandlers.put(ActiveMode.MOVING_UNIT, new MovingUnitModeHandler(gameState, unitMovementController, pathfinder));
        this.mouseEventHandlers.put(ActiveMode.ATTACKING_UNIT, new AttackingUnitModeHandler(gameState, unitMovementController, Context.getUnitAttackController()));
        this.gameState.activeModeProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Switching to mode {}", newValue);
            if (newValue == ActiveMode.MOVING_UNIT) {
                this.highlightPossibleTiles();
            } else if (newValue == ActiveMode.ATTACKING_UNIT) {
                this.highlightPossibleAttackTiles();
            } else {
                this.unhiglightPossibleTiles();
            }
        });
    }

    private void highlightPossibleTiles() {
        TileModel startTile = gameState.selectedTileProperty().get();
        Set<TileModel> possibleTiles = pathfinder.getReachableTiles(startTile);
        for (TileModel tile : possibleTiles) {
            if (!tile.isOccupied()) {
                tile.partOfPossiblePathProperty().set(true);
                highlightedPossibleTiles.add(tile);
            }
        }
    }

    private void unhiglightPossibleTiles() {
        for (TileModel tile : highlightedPossibleTiles) {
            tile.partOfPossiblePathProperty().set(false);
        }

        for (TileModel tile : highlightedAttackTiles) {
            tile.partOfPossiblePathProperty().set(false);
        }

        highlightedPossibleTiles.clear();
    }

    private void highlightPossibleAttackTiles() {
        LOGGER.info("Highlighting attackable tiles");
        TileModel startTile = gameState.selectedTileProperty().get();
        UnitModel attackingUnit = gameState.selectedUnitProperty().get();
        LOGGER.info("Attacking Unit position: {}", attackingUnit.getPosition());
        LOGGER.info("Start Tile position: {}", startTile.getPosition());
        Set<TileModel> possibleTiles = pathfinder.getAttackableTiles(startTile, attackingUnit);
        for (TileModel tile : possibleTiles) {
            if (tile != startTile) {
                tile.partOfPossiblePathProperty().set(true);
                highlightedAttackTiles.add(tile);
            }
        }
    }

    public void handleMouseEnter(MouseEvent event) {
        TileView tileView = (TileView) ((StackPane) event.getSource()).getUserData();
        MouseEventHandler handler = mouseEventHandlers.get(gameState.activeModeProperty().get());
        if (handler != null) {
            handler.handleMouseEnter(event, tileView);
        }
    }

    public void handleMouseClick(MouseEvent event) {
        TileView tileView = (TileView) ((StackPane) event.getSource()).getUserData();
        MouseEventHandler handler = mouseEventHandlers.get(gameState.activeModeProperty().get());
        if (handler != null) {
            handler.handleMouseClick(event, tileView);
        }
    }
}


