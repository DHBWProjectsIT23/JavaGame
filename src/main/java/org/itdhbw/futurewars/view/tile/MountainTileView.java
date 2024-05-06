package org.itdhbw.futurewars.view.tile;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.MountainTileModel;

public class MountainTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/tiles/MountainTile.png");

    public MountainTileView(MountainTileModel mountainTileModel) {
        super(mountainTileModel);
    }

    protected void setTexture() {
        this.textureLayer.setImage(MountainTileView.TEXTURE);
    }
}

