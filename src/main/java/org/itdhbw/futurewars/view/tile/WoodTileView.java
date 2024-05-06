package org.itdhbw.futurewars.view.tile;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.WoodTileModel;

public class WoodTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/tiles/WoodTile.png");

    public WoodTileView(WoodTileModel woodTileModel) {
        super(woodTileModel);
    }

    protected void setTexture() {
        this.textureLayer.setImage(WoodTileView.TEXTURE);
    }
}

