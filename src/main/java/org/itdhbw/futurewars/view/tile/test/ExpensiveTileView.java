package org.itdhbw.futurewars.view.tile.test;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.test.ExpensiveTileModel;
import org.itdhbw.futurewars.view.tile.TileView;

public class ExpensiveTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/ExpensiveTile.png");

    public ExpensiveTileView(ExpensiveTileModel testTile) {
        super(testTile);
    }

    protected void setTexture() {
        this.textureLayer.setImage(ExpensiveTileView.TEXTURE);
    }
}
