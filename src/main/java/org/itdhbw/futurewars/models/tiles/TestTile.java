package org.itdhbw.futurewars.models.tiles;

import javafx.scene.image.Image;

public class TestTile extends Tile {
    private static final Image TEXTURE = new Image("file:resources/textures/64Sample.png");

    public TestTile(int x, int y) {
        super(x, y);
    }

    @Override
    protected void setTexture() {
        view.setImage(TEXTURE);
    }
}
