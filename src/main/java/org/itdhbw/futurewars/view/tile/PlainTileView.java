package org.itdhbw.futurewars.view.tile;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.PlainTileModel;

public class PlainTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/Tiles/PlainTile.png");

    public PlainTileView(PlainTileModel plainTileModel) {
        super(plainTileModel);
    }

    protected void setTexture() {
        this.textureLayer.setImage(PlainTileView.TEXTURE);
    }
}

