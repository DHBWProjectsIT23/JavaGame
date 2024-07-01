package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.game.models.game_state.ActiveMode;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.Optional;

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
            if (tileView.getTileModel().getOccupyingUnit().getTeam() !=
                gameState.getCurrentPlayer() ||
                tileView.getTileModel().getOccupyingUnit().hasMoved()) {
                return;
            }
            gameState.selectUnit(Optional.of(tileView.getTileModel().getOccupyingUnit()));
            gameState.setActiveMode(ActiveMode.MOVING_UNIT);
        } else {
            gameState.selectUnit(Optional.empty());
        }
    }
}

