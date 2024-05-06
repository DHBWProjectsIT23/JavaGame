package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.TankUnitModel;

public class TankUnitView extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/Placeholders/TankUnit.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/Placeholders/TankUnit.png");

    public TankUnitView(TankUnitModel tankUnitModel) {
        super(tankUnitModel);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(TankUnitView.TEXTURE_1);
        } else {
            this.setImage(TankUnitView.TEXTURE_2);
        }
    }
}

