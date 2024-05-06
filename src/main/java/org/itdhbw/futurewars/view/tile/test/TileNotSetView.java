package org.itdhbw.futurewars.view.tile.test;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.test.TileNotSetModel;
import org.itdhbw.futurewars.view.tile.TileView;

public class TileNotSetView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/TileNotSet.png");

    public TileNotSetView(TileNotSetModel testTile) {
        super(testTile);
    }

    protected void setTexture() {
        this.textureLayer.setImage(TileNotSetView.TEXTURE);
    }
}

