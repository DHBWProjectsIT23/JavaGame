package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.MechanizedUnitModel;

public class MechanizedUnitVIew extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/Placeholders/MechanizedUnit.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/Placeholders/MechanizedUnit.png");

    public MechanizedUnitVIew(MechanizedUnitModel mechanizedUnitModel) {
        super(mechanizedUnitModel);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(MechanizedUnitVIew.TEXTURE_1);
        } else {
            this.setImage(MechanizedUnitVIew.TEXTURE_2);
        }
    }
}

