package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.BomberUnitModel;

public class BomberUnitView extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/Placeholders/BomberUnit.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/Placeholders/BomberUnit.png");

    public BomberUnitView(BomberUnitModel bomberUnitModel) {
        super(bomberUnitModel);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(BomberUnitView.TEXTURE_1);
        } else {
            this.setImage(BomberUnitView.TEXTURE_2);
        }
    }
}

