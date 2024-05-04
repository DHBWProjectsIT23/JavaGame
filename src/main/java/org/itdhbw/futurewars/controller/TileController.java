package org.itdhbw.futurewars.controller;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.Tile;

public class TileController {
    private TileController() {
        // private constructor to hide the implicit public one
    }

    public static void handleMouseEntered(MouseEvent event) {
        Tile tile = (Tile) ((StackPane) event.getSource()).getUserData();
        switch (GameState.getActiveMode()) {
            case REGULAR:
                handleRegularEnter(tile);
                break;
            case MOVING_UNIT:
                handleMovingUnitEnter(tile);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + GameState.getActiveMode());
        }
        // rest of the code
    }

    public static void handleMouseClick(MouseEvent event) {
        Tile tile = (Tile) ((StackPane) event.getSource()).getUserData();
        switch (GameState.getActiveMode()) {
            case REGULAR:
                handleRegularClick(tile);
                break;
            case MOVING_UNIT:
                handleMovingUnitClick(tile);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + GameState.getActiveMode());
        }
        // rest of the code
    }

    private static void handleRegularEnter(Tile tile) {
        GameState.getInstance().hoverTile(tile);
    }

    private static void handleRegularClick(Tile tile) {
        GameState.getInstance().selectTile(tile);

        if (tile.isOccupied()) {
            GameState.getInstance().setActiveMode(ActiveMode.MOVING_UNIT);
        }
    }

    private static void handleMovingUnitEnter(Tile tile) {
        GameState.getInstance().hoverTile(tile);
    }

    private static void handleMovingUnitClick(Tile tile) {
        GameState.getSelectedTileProperty().get().getOccupyingUnit().get().moveTo(tile);
        GameState.getInstance().setActiveMode(ActiveMode.REGULAR);
        GameState.getInstance().deselectTile();
    }
}


