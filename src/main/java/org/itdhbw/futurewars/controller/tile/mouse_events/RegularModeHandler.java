package org.itdhbw.futurewars.controller.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.view.TileView;

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
            if (tileView.getTileModel().getOccupyingUnit().getTeam() != gameState.getCurrentPlayer() || tileView.getTileModel().getOccupyingUnit().hasMoved()) {
                return;
            }
            gameState.selectUnit(tileView.getTileModel().getOccupyingUnit());
            gameState.setActiveMode(ActiveMode.MOVING_UNIT);
        }
    }
}
