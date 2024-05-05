package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.TestUnitModel;

public class TestUnitView extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/UnitSample.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/UnitSample2.png");

    public TestUnitView(TestUnitModel testUnit) {
        super(testUnit);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(TestUnitView.TEXTURE_1);
        } else {
            this.setImage(TestUnitView.TEXTURE_2);
        }
    }
}

