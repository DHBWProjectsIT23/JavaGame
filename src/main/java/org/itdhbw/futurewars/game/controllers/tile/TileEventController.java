package org.itdhbw.futurewars.game.controllers.tile;

import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.AttackingUnitModeHandler;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.MouseEventHandler;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.MovingUnitModeHandler;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.RegularModeHandler;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.models.gameState.ActiveMode;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.AStarPathfinder;
import org.itdhbw.futurewars.game.views.TileView;

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
        this.mouseEventHandlers.put(ActiveMode.MOVING_UNIT, new MovingUnitModeHandler(gameState, pathfinder));
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

        Task<Set<TileModel>> task = new Task<>() {
            @Override
            protected Set<TileModel> call() {
                return pathfinder.getReachableTiles(startTile);
            }
        };

        task.setOnSucceeded(event -> {
            Set<TileModel> possibleTiles = task.getValue();
            for (TileModel tile : possibleTiles) {
                if (!tile.isOccupied()) {
                    tile.partOfPossiblePathProperty().set(true);
                    highlightedPossibleTiles.add(tile);
                }
            }
        });

        new Thread(task).start();
    }

    private void unhiglightPossibleTiles() {
        highlightedPossibleTiles.forEach(tileModel -> tileModel.partOfPossiblePathProperty().set(false));

        highlightedAttackTiles.forEach(tileModel -> tileModel.partOfPossiblePathProperty().set(false));

        highlightedPossibleTiles.clear();
    }

    private void highlightPossibleAttackTiles() {
        TileModel startTile = gameState.selectedTileProperty().get();
        UnitModel attackingUnit = gameState.selectedUnitProperty().get();

        Task<Set<TileModel>> task = new Task<>() {
            @Override
            protected Set<TileModel> call() {
                return pathfinder.getAttackableTiles(startTile, attackingUnit);
            }
        };
        task.setOnSucceeded(event -> {
            Set<TileModel> possibleTiles = task.getValue();
            possibleTiles.forEach(tile -> {
                if (tile != startTile) {
                    tile.partOfPossiblePathProperty().set(true);
                    highlightedAttackTiles.add(tile);
                }
            });
        });

        new Thread(task).start();
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


