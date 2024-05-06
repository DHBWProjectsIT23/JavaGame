package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.ArtilleryUnitModel;

public class ArtilleryUnitView extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/Placeholders/ArtilleryUnit.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/Placeholders/ArtilleryUnit.png");

    public ArtilleryUnitView(ArtilleryUnitModel artilleryUnitModel) {
        super(artilleryUnitModel);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(ArtilleryUnitView.TEXTURE_1);
        } else {
            this.setImage(ArtilleryUnitView.TEXTURE_2);
        }
    }
}

