package org.itdhbw.futurewars.view.tile;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.UnpassableTileModel;

public class UnpassableTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/UnpassableTile.png");

    public UnpassableTileView(UnpassableTileModel unpassableTileModel) {
        super(unpassableTileModel);
    }

    protected void setTexture() {
        this.textureLayer.setImage(UnpassableTileView.TEXTURE);
    }
}

