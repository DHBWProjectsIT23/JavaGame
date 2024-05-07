package org.itdhbw.futurewars.controller.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.view.tile.TileView;

public class AttackingUnitModeHandler implements MouseEventHandler {
    private final GameState gameState;

    public AttackingUnitModeHandler(GameState gameState) {
        this.gameState = gameState;
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
        gameState.setActiveMode(ActiveMode.REGULAR);
    }

}

