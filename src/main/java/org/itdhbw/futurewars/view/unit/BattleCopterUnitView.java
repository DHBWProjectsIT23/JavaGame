package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import org.itdhbw.futurewars.model.unit.BattleCopterUnitModel;

public class BattleCopterUnitView extends UnitView {
    private static final Image TEXTURE_1 = new Image("file:resources/textures/Placeholders/BattleCopterUnit.png");
    private static final Image TEXTURE_2 = new Image("file:resources/textures/Placeholders/BattleCopterUnit.png");

    public BattleCopterUnitView(BattleCopterUnitModel battleCopterUnitModel) {
        super(battleCopterUnitModel);
    }

    @Override
    protected void setTexture() {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(BattleCopterUnitView.TEXTURE_1);
        } else {
            this.setImage(BattleCopterUnitView.TEXTURE_2);
        }
    }
}

