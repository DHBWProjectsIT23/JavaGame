package org.itdhbw.futurewars.controller.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.controller.unit.UnitAttackController;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.view.TileView;

public class AttackingUnitModeHandler implements MouseEventHandler {
    private final GameState gameState;
    private final UnitMovementController unitMovementController;
    private final UnitAttackController unitAttackController;

    public AttackingUnitModeHandler(GameState gameState, UnitMovementController unitMovementController, UnitAttackController unitAttackController) {
        this.gameState = gameState;
        this.unitMovementController = unitMovementController;
        this.unitAttackController = unitAttackController;
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
        unitAttackController.attack(tileView.getTileModel().getOccupyingUnit());
        gameState.setActiveMode(ActiveMode.REGULAR);
    }
}

