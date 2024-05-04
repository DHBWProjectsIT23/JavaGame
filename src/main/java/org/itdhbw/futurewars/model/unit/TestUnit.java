package org.itdhbw.futurewars.model.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.tile.Tile;

public class TestUnit extends Unit {

    private static final Image TEXTURE_TEAM1 = new Image("file:resources/textures/UnitSample.png");
    private static final Image TEXTURE_TEAM2 = new Image("file:resources/textures/UnitSample2.png");

    public TestUnit(Tile tile, double tileSize, UnitType unitType, final int team) {
        super(tile, tileSize, unitType, team);
    }

    @Override
    protected void setTexture() {
        if (this.team == 1) {
            textureLayer.setImage(TEXTURE_TEAM1);
        } else {
            textureLayer.setImage(TEXTURE_TEAM2);
        }
    }
}
