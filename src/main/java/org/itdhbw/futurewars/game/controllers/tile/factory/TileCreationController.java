// TileCreationController.java
package org.itdhbw.futurewars.game.controllers.tile.factory;

import javafx.util.Pair;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.views.TileView;

//!TODO: log
public class TileCreationController {
    private final TileBuilder tileBuilder;

    public TileCreationController() {
        this.tileBuilder = Context.getTileBuilder();
    }

    public Pair<TileModel, TileView> createUnsetTile(int x, int y) {
        return createUnsetTile("TILE_NOT_SET", x, y, 0);
    }

    public Pair<TileModel, TileView> createUnsetTile(String tileType, int x, int y, int textureVariant) {
        return tileBuilder.createTile(tileType, x, y, textureVariant);
    }
}
