package org.itdhbw.futurewars.controller.tile.mouseEvents;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.view.tile.TileView;

public interface MouseEventHandler {
    void handleMouseEnter(MouseEvent event, TileView tileView);

    void handleMouseClick(MouseEvent event, TileView tileView);
}
