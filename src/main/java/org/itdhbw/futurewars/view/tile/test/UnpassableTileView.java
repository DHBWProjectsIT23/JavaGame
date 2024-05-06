package org.itdhbw.futurewars.view.tile.test;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.test.UnpassableTileModel;
import org.itdhbw.futurewars.view.tile.TileView;

public class UnpassableTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/UnpassableTile.png");

    public UnpassableTileView(UnpassableTileModel unpassableTileModel) {
        super(unpassableTileModel);
    }

    protected void setTexture() {
        this.textureLayer.setImage(UnpassableTileView.TEXTURE);
    }
}

