package org.itdhbw.futurewars.controller.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.view.tile.TileView;

public class AttackingUnitModeHandler implements MouseEventHandler {
    private final GameState gameState;
    private final UnitMovementController unitMovementController;

    public AttackingUnitModeHandler(GameState gameState, UnitMovementController unitMovementController) {
        this.gameState = gameState;
        this.unitMovementController = unitMovementController;
    }

    @Override
    public void handleMouseEnter(MouseEvent event, TileView tileView) {
        if (tileView.getTileModel().isOccupied()) {
            gameState.hoverTile(tileView.getTileModel());
        } else {
            gameState.unhoverTile();
        }
    }

    @Override
    public void handleMouseClick(MouseEvent event, TileView tileView) {
        // TODO!
        unitMovementController.moveUnit(gameState.selectedUnitProperty().get(), gameState.selectedTileProperty().get());
        // tile view die mitgegeben wird enthält tile mit unit die angegriffen wrid
        gameState.setActiveMode(ActiveMode.REGULAR);
    }

}

