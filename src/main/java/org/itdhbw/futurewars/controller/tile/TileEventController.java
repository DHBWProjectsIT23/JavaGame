package org.itdhbw.futurewars.controller.tile;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.util.AStarPathfinder;
import org.itdhbw.futurewars.view.tile.TileView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//! BUG: Hovering over a tile that is occupied throws an NullPointerException in Moving Unit Mode
public class TileEventController {
    private static final Logger LOGGER = LogManager.getLogger(TileEventController.class);
    private static final List<TileModel> highlightedTiles = new ArrayList<>();
    private GameState gameState;
    private UnitMovementController unitMovementController;
    private AStarPathfinder pathfinder;

    public TileEventController() {
    }

    public void initialize() {
        this.unitMovementController = Context.getUnitMovementController();
        this.gameState = Context.getGameState();
        this.pathfinder = Context.getPathfinder();
    }

    public void handleMouseEntered(MouseEvent event) {
        handleEvent(event, this::handleRegularEnter, this::handleMovingUnitEnter);
    }

    public void handleMouseClick(MouseEvent event) {
        handleEvent(event, this::handleRegularClick, this::handleMovingUnitClick);
    }

    private void handleEvent(MouseEvent event, Consumer<TileView> regularHandler, Consumer<TileView> movingUnitHandler) {
        TileView tileView = (TileView) ((StackPane) event.getSource()).getUserData();
        switch (gameState.activeModeProperty().get()) {
            case REGULAR:
                regularHandler.accept(tileView);
                break;
            case MOVING_UNIT:
                movingUnitHandler.accept(tileView);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gameState.activeModeProperty());
        }
    }


    private void handleRegularEnter(TileView tileView) {
        gameState.hoverTile(tileView.getTileModel());
    }

    private void handleRegularClick(TileView tileView) {
        gameState.selectTile(tileView.getTileModel());

        if (tileView.getTileModel().isOccupied()) {
            gameState.setActiveMode(ActiveMode.MOVING_UNIT);
        }
    }

    private void handleMovingUnitEnter(TileView tileView) {
        List<TileModel> newPath = pathfinder.findPath(gameState.getSelectedTileProperty().get(), tileView.getTileModel());

        for (TileModel tile : new ArrayList<>(highlightedTiles)) {
            if (!newPath.contains(tile)) {
                tile.setHighlighted(false);
                highlightedTiles.remove(tile);
            }
        }

        for (TileModel tile : newPath) {
            if (!highlightedTiles.contains(tile)) {
                tile.setHighlighted(true);
                highlightedTiles.add(tile);
            }
        }
    }


    private void handleMovingUnitClick(TileView tileView) {
        UnitModel unitModel = gameState.getSelectedTileProperty().get().getOccupyingUnit();
        unitMovementController.moveUnit(unitModel, tileView.getTileModel());
        gameState.setActiveMode(ActiveMode.REGULAR);
        gameState.deselectTile();
        for (TileModel tile : highlightedTiles) {
            tile.setHighlighted(false);
        }
    }
}


