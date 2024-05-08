package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.CustomUnitModel;

public class CustomUnitView extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/Units/Robot1.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/Units/Robot1.png");

    public CustomUnitView(CustomUnitModel customUnitModel) {
        super(customUnitModel);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(CustomUnitView.TEXTURE_1);
        } else {
            this.setImage(CustomUnitView.TEXTURE_2);
        }
    }

    public void setTexture(Image texture1, Image texture2) {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(texture1);
        } else {
            this.setImage(texture2);
        }
    }

}

