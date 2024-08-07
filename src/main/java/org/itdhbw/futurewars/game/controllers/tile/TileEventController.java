package org.itdhbw.futurewars.game.controllers.tile;

import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.exceptions.NoUnitSelectedException;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.ActionModeHandler;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.AttackModeHandler;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.MouseEventHandler;
import org.itdhbw.futurewars.game.controllers.tile.mouse_events.RegularModeHandler;
import org.itdhbw.futurewars.game.controllers.unit.UnitAttackController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.models.game_state.ActiveMode;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Pathfinder;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

//! BUG: Hovering over a tile that is occupied throws an NullPointerException in Moving Unit Mode
public class TileEventController {
    private static final Logger LOGGER = Logger.getLogger(TileEventController.class.getSimpleName());
    private static final List<TileModel> highlightedMoveTiles = new ArrayList<>();
    private static final List<TileModel> highlightedAttackTiles = new ArrayList<>();
    private static final List<TileModel> highlightedMergeTiles = new ArrayList<>();
    private final EnumMap<ActiveMode, MouseEventHandler> mouseEventHandlers;
    private GameState gameState;
    private Pathfinder pathfinder;


    public TileEventController() {
        this.mouseEventHandlers = new EnumMap<>(ActiveMode.class);
    }

    public boolean isPartOfAttackableTiles(TileView tileView) {
        return highlightedAttackTiles.contains(tileView.getTileModel());
    }

    public void initialize() {
        UnitMovementController unitMovementController;
        this.gameState = Context.getGameState();
        this.pathfinder = Context.getPathfinder();
        unitMovementController = Context.getUnitMovementController();
        this.mouseEventHandlers.put(ActiveMode.REGULAR_MODE, new RegularModeHandler(gameState));
        this.mouseEventHandlers.put(ActiveMode.ACTION_MODE, new ActionModeHandler(gameState, pathfinder));
        this.mouseEventHandlers.put(ActiveMode.ATTACK_MODE, new AttackModeHandler(gameState, unitMovementController,
                                                                                  new UnitAttackController()));

        this.gameState.activeModeProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Switching to mode " + newValue);
            switch (newValue) {
                case ActiveMode.ACTION_MODE:
                    this.highlightMoveTiles();
                    this.highlightPossibleMergeTiles();
                    break;
                case ActiveMode.ATTACK_MODE:
                    this.highlightPossibleAttackTiles();
                    break;
                default:
                    this.unHiglightTiles();
            }
        });
    }

    private void highlightMoveTiles() {
        TileModel startTile = gameState.getSelectedTile();

        Task<Set<TileModel>> task = new Task<>() {
            @Override
            protected Set<TileModel> call() {
                return pathfinder.getReachableTiles(startTile);
            }
        };

        task.setOnSucceeded(ignored -> {
            Set<TileModel> possibleTiles = task.getValue();
            for (TileModel tile : possibleTiles) {
                if (!tile.isOccupied()) {
                    tile.partOfPossiblePathProperty().set(true);
                    highlightedMoveTiles.add(tile);
                }
            }
        });

        new Thread(task).start();
    }

    private void highlightPossibleMergeTiles() {
        TileModel startTile = gameState.getSelectedTile();
        UnitModel mergingUnit;
        try {
            mergingUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not calculate mergeable units");
            return;
        }

        Task<Set<TileModel>> task = getMergeableTilesTask(startTile, mergingUnit);

        new Thread(task).start();
    }

    private void highlightPossibleAttackTiles() {
        TileModel startTile = gameState.getSelectedTile();
        //Throw properly
        UnitModel attackingUnit;
        try {
            attackingUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not calculate attackable units");
            return;
        }

        Task<Set<TileModel>> task = getAttackableTilesTask(startTile, attackingUnit);

        new Thread(task).start();
    }

    private void unHiglightTiles() {
        highlightedMoveTiles.forEach(tileModel -> tileModel.partOfPossiblePathProperty().set(false));

        highlightedAttackTiles.forEach(tileModel -> tileModel.possibleToAttackProperty().set(false));

        highlightedMergeTiles.forEach(tileModel -> tileModel.possibleToMergeProperty().set(false));

        highlightedMoveTiles.clear();
    }

    private Task<Set<TileModel>> getMergeableTilesTask(TileModel startTile, UnitModel mergingUnit) {
        Task<Set<TileModel>> task = new Task<>() {
            @Override
            protected Set<TileModel> call() {
                return pathfinder.getMergeableTiles(startTile, mergingUnit);
            }
        };
        task.setOnSucceeded(ignored -> {
            Set<TileModel> possibleTiles = task.getValue();
            possibleTiles.forEach(tile -> {
                if (tile != startTile) {
                    tile.possibleToMergeProperty().set(true);
                    highlightedMergeTiles.add(tile);
                }
            });
        });
        return task;
    }

    private Task<Set<TileModel>> getAttackableTilesTask(TileModel startTile, UnitModel attackingUnit) {
        Task<Set<TileModel>> task = new Task<>() {
            @Override
            protected Set<TileModel> call() {
                return pathfinder.getAttackableTiles(startTile, attackingUnit);
            }
        };
        task.setOnSucceeded(ignored -> {
            Set<TileModel> possibleTiles = task.getValue();
            possibleTiles.forEach(tile -> {
                if (tile != startTile) {
                    tile.possibleToAttackProperty().set(true);
                    highlightedAttackTiles.add(tile);
                }
            });
        });
        return task;
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


