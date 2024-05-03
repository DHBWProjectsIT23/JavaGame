package org.itdhbw.futurewars.model.tile;

import javafx.scene.image.Image;

public class TestTile extends Tile {
    private static final Image TEXTURE = new Image("file:resources/textures/64Sample.png");

    public TestTile(int x, int y, double size) {
        super(x, y, size, TileType.TEST_TILE);
    }

    @Override
    protected void setTexture() {
        view.setImage(TEXTURE);
    }
}
