package org.itdhbw.futurewars.controller.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.view.TileView;

public interface MouseEventHandler {
    void handleMouseEnter(MouseEvent event, TileView tileView);

    void handleMouseClick(MouseEvent event, TileView tileView);

    default void highlightPossibleTiles() {

    }
}
