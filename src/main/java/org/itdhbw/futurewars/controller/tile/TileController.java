package org.itdhbw.futurewars.controller.tile;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.view.tile.TileView;

import java.util.HashMap;
import java.util.function.Consumer;

public class TileController {
    private static final Logger LOGGER = LogManager.getLogger(TileController.class);
    private static final TileBuilder TILE_FACTORY = new TileBuilder();
    private static final HashMap<TileModel, TileView> TILE_MODEL_VIEW_TABLE = new HashMap<>();

    private TileController() {
        throw new IllegalStateException("Utility class");
    }

    public static TileView getTileView(TileModel tileModel) {
        return TILE_MODEL_VIEW_TABLE.get(tileModel);
    }

    public static TileView createTile(TileType tileType, int x, int y) {
        LOGGER.info("Creating tile of type {} at position ({}, {})", tileType, x, y);
        Pair<TileModel, TileView> tilePair = TILE_FACTORY.createTile(tileType, x, y);
        TILE_MODEL_VIEW_TABLE.put(tilePair.getKey(), tilePair.getValue());
        return tilePair.getValue();
    }

    public static void handleMouseEntered(MouseEvent event) {
        handleEvent(event, TileController::handleRegularEnter, TileController::handleMovingUnitEnter);
    }

    public static void handleMouseClick(MouseEvent event) {
        handleEvent(event, TileController::handleRegularClick, TileController::handleMovingUnitClick);
    }

    private static void handleEvent(MouseEvent event, Consumer<TileView> regularHandler, Consumer<TileView> movingUnitHandler) {
        TileView tileView = (TileView) ((StackPane) event.getSource()).getUserData();
        switch (GameState.getInstance().activeModeProperty().get()) {
            case REGULAR:
                regularHandler.accept(tileView);
                break;
            case MOVING_UNIT:
                movingUnitHandler.accept(tileView);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + GameState.getInstance().activeModeProperty());
        }
    }


    private static void handleRegularEnter(TileView tileView) {
        GameState.getInstance().hoverTile(tileView.getTileModel());
    }

    private static void handleRegularClick(TileView tileView) {
        GameState.getInstance().selectTile(tileView.getTileModel());

        if (tileView.getTileModel().isOccupied()) {
            GameState.getInstance().setActiveMode(ActiveMode.MOVING_UNIT);
        }
    }

    private static void handleMovingUnitEnter(TileView tileView) {
        GameState.getInstance().hoverTile(tileView.getTileModel());
    }

    private static void handleMovingUnitClick(TileView tileView) {
        GameState.getInstance().getSelectedTileProperty().get().getOccupyingUnit().moveTo(tileView.getTileModel());
        GameState.getInstance().setActiveMode(ActiveMode.REGULAR);
        GameState.getInstance().deselectTile();
    }
}


