package org.itdhbw.futurewars.controller.tile;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.mouseEvents.MouseEventHandler;
import org.itdhbw.futurewars.controller.tile.mouseEvents.MovingUnitModeHandler;
import org.itdhbw.futurewars.controller.tile.mouseEvents.RegularModeHandler;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.util.AStarPathfinder;
import org.itdhbw.futurewars.view.tile.TileView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

//! BUG: Hovering over a tile that is occupied throws an NullPointerException in Moving Unit Mode
public class TileEventController {
    private static final Logger LOGGER = LogManager.getLogger(TileEventController.class);
    private static final List<TileModel> highlightedTiles = new ArrayList<>();
    private final EnumMap<ActiveMode, MouseEventHandler> mouseEventHandlers;
    private GameState gameState;
    private UnitMovementController unitMovementController;
    private AStarPathfinder pathfinder;

    public TileEventController() {
        this.mouseEventHandlers = new EnumMap<>(ActiveMode.class);
    }

    public void initialize() {
        this.unitMovementController = Context.getUnitMovementController();
        this.gameState = Context.getGameState();
        this.pathfinder = Context.getPathfinder();
        this.mouseEventHandlers.put(ActiveMode.REGULAR, new RegularModeHandler(gameState));
        this.mouseEventHandlers.put(ActiveMode.MOVING_UNIT, new MovingUnitModeHandler(gameState, unitMovementController, pathfinder));
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


