package org.itdhbw.futurewars.controller.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.view.tile.TileView;

public class RegularModeHandler implements MouseEventHandler {

    private final GameState gameState;

    public RegularModeHandler(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void handleMouseEnter(MouseEvent event, TileView tileView) {
        gameState.hoverTile(tileView.getTileModel());
    }

    @Override
    public void handleMouseClick(MouseEvent event, TileView tileView) {
        gameState.selectTile(tileView.getTileModel());
        if (tileView.getTileModel().isOccupied()) {
            gameState.selectUnit(tileView.getTileModel().getOccupyingUnit());
            gameState.setActiveMode(ActiveMode.MOVING_UNIT);
        }
    }
}

