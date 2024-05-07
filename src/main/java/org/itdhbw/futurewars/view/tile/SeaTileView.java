package org.itdhbw.futurewars.view.tile;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.SeaTileModel;

public class SeaTileView extends TileView {
    private static final Image TEXTURE = new Image("file:resources/textures/Tiles/SeaTile.png");

    public SeaTileView(SeaTileModel seaTileModel) {
        super(seaTileModel);
    }

    protected void setTexture() {
        this.textureLayer.setImage(SeaTileView.TEXTURE);
    }
}

