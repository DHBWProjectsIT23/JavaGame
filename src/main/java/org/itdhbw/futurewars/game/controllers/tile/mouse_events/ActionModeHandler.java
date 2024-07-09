package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.concurrent.Task;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.exceptions.NoUnitSelectedException;
import org.itdhbw.futurewars.game.models.game_state.ActiveMode;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Pathfinder;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionModeHandler implements MouseEventHandler {
    private static final List<TileModel> highlightedTiles = new ArrayList<>();
    private static final Logger LOGGER = LogManager.getLogger(ActionModeHandler.class);
    private final GameState gameState;
    private final Pathfinder pathfinder;

    public ActionModeHandler(GameState gameState, Pathfinder pathfinder) {
        this.gameState = gameState;
        this.pathfinder = pathfinder;
    }

    @Override
    public void handleMouseEnter(MouseEvent event, TileView tileView) {
        Task<List<TileModel>> getMovableTiles = new Task<>() {
            @Override
            protected List<TileModel> call() {
                return pathfinder.findPath(gameState.getSelectedTile(), tileView.getTileModel());
            }
        };

        getMovableTiles.setOnSucceeded(taskEvent -> {
            List<TileModel> newPath = getMovableTiles.getValue();

            for (TileModel tile : new ArrayList<>(highlightedTiles)) {
                if (!newPath.contains(tile)) {
                    tile.setPartOfPath(false);
                    highlightedTiles.remove(tile);
                }
            }

            for (TileModel tile : newPath) {
                if (!highlightedTiles.contains(tile)) {
                    tile.setPartOfPath(true);
                    highlightedTiles.add(tile);
                }
            }

            UnitModel selectedUnit;
            try {
                selectedUnit = gameState.getSelectedUnit();
            } catch (NoUnitSelectedException e) {
                ErrorHandler.addVerboseException(e, "No unit selected, could not calculate path");
                return;
            }

            if (highlightedTiles.size() > 1) {
                selectedUnit.setCanMove(tileView.getTileModel() == highlightedTiles.getLast());
            } else {
                selectedUnit.setCanMove(false);
            }
        });

        Task<Set<TileModel>> getAttackableTiles = getAttackableTilesTask(tileView);

        new Thread(getMovableTiles).start();
        new Thread(getAttackableTiles).start();

    }

    private Task<Set<TileModel>> getAttackableTilesTask(TileView tileView) {
        Task<Set<TileModel>> getAttackableTiles = new Task<>() {
            @Override
            protected Set<TileModel> call() {

                UnitModel selectedUnit;
                try {
                    selectedUnit = gameState.getSelectedUnit();
                } catch (NoUnitSelectedException e) {
                    ErrorHandler.addVerboseException(e, "No unit selected, could not calculate attackable tiles");
                    return new HashSet<>();
                }
                return pathfinder.getAttackableTiles(tileView.getTileModel(), selectedUnit);
            }
        };

        getAttackableTiles.setOnSucceeded(_ -> {
            Set<TileModel> attackableTiles = getAttackableTiles.getValue();
            // Throw properly
            UnitModel selectedUnit;
            try {
                selectedUnit = gameState.getSelectedUnit();
            } catch (NoUnitSelectedException e) {
                ErrorHandler.addVerboseException(e, "No unit selected, could not calculate attackable tiles");
                return;
            }
            selectedUnit.setCanAttack(!attackableTiles.isEmpty());
        });
        return getAttackableTiles;
    }

    @Override
    public void handleMouseClick(MouseEvent event, TileView tileView) {
        UnitModel unit;
        try {
            unit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not calculate possible actions");
            return;
        }

        if (tileView.getTileModel().isOccupied() && tileView.getTileModel() != gameState.getSelectedTile() &&
            !tileView.getTileModel().getOccupyingUnit().canMergeWith(unit)) {
            return;
        }
        unit.setCanMerge(
                tileView.getTileModel().isOccupied() && tileView.getTileModel().getOccupyingUnit().canMergeWith(unit));

        // Behavior needs to differ if the tile of the unit is clicked
        if (unit.getPosition() == tileView.getTileModel().getPosition()) {
            unit.setCanAttack(!pathfinder.getAttackableTiles(tileView.getTileModel(), unit).isEmpty());
            unit.setCanMove(false);
        }

        gameState.setActiveMode(ActiveMode.OVERLAY_MODE);
        gameState.selectTile(tileView.getTileModel());

        for (TileModel tile : highlightedTiles) {
            tile.setPartOfPath(false);
        }
    }

}

