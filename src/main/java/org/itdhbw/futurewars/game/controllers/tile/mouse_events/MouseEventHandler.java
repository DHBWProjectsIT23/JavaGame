package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.game.views.TileView;

public interface MouseEventHandler {
    void handleMouseEnter(MouseEvent event, TileView tileView);

    void handleMouseClick(MouseEvent event, TileView tileView);
}
