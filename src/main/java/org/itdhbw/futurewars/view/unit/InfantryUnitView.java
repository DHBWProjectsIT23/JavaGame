package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.InfantryUnitModel;

public class InfantryUnitView extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/Units/Robot1.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/Units/Robot1.png");

    public InfantryUnitView(InfantryUnitModel infantryUnitModel) {
        super(infantryUnitModel);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(InfantryUnitView.TEXTURE_1);
        } else {
            this.setImage(InfantryUnitView.TEXTURE_2);
        }
    }
}

