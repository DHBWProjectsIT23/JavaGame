package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.game.controllers.unit.UnitAttackController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.models.game_state.ActiveMode;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.views.TileView;

public class AttackModeHandler implements MouseEventHandler {
    private final GameState gameState;
    private final UnitMovementController unitMovementController;
    private final UnitAttackController unitAttackController;

    public AttackModeHandler(GameState gameState, UnitMovementController unitMovementController, UnitAttackController unitAttackController) {
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
        // Throw properly
        unitMovementController.moveUnit(gameState.selectedUnitProperty().get().orElseThrow(),
                                        gameState.selectedTileProperty().get());
        // tile view die mitgegeben wird enthält tile mit unit die angegriffen wrid
        unitAttackController.attack(tileView.getTileModel().getOccupyingUnit());
        gameState.setActiveMode(ActiveMode.REGULAR_MODE);
    }
}

