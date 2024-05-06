package org.itdhbw.futurewars.view.tile.test;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.test.TestTileModel;
import org.itdhbw.futurewars.view.tile.TileView;

public class TestTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/64Sample.png");

    public TestTileView(TestTileModel testTile) {
        super(testTile);
    }

    protected void setTexture() {
        this.textureLayer.setImage(TestTileView.TEXTURE);
    }
}

