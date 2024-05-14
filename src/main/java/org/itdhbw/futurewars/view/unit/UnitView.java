package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.unit.UnitModel;

public class UnitView extends ImageView {
    private static final Logger LOGGER = LogManager.getLogger(UnitView.class);
    public final int viewId = this.hashCode();
    protected final UnitModel unitModel;

    public UnitView(UnitModel unitModel) {
        this.unitModel = unitModel;
        LOGGER.info("Creating unit view {} for unit {}", this.viewId, unitModel.modelId);

        this.fitHeightProperty().bind(Context.getGameState().tileSizeProperty());
        this.fitWidthProperty().bind(Context.getGameState().tileSizeProperty());
    }

    public void setTexture(Image texture1, Image texture2) {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(texture1);
        } else {
            this.setImage(texture2);
        }
    }
}
