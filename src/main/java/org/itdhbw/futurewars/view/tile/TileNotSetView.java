package org.itdhbw.futurewars.view.tile;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.TileNotSetModel;

public class TileNotSetView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/TileNotSet.png");

    public TileNotSetView(TileNotSetModel testTile) {
        super(testTile);
    }

    protected void setTexture() {
        this.textureLayer.setImage(TileNotSetView.TEXTURE);
    }
}

