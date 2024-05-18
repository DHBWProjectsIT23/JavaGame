package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.game.views.TileView;

/**
 * The interface Mouse event handler.
 */
public interface MouseEventHandler {
    /**
     * Handle mouse enter.
     *
     * @param event    the event
     * @param tileView the tile view
     */
    void handleMouseEnter(MouseEvent event, TileView tileView);

    /**
     * Handle mouse click.
     *
     * @param event    the event
     * @param tileView the tile view
     */
    void handleMouseClick(MouseEvent event, TileView tileView);

    /**
     * Highlight possible tiles.
     */
    default void highlightPossibleTiles() {

    }
}
