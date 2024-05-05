package org.itdhbw.futurewars.view.unit;

import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.util.Constants;

public abstract class UnitView extends ImageView {
    private static final Logger LOGGER = LogManager.getLogger(UnitView.class);
    public final int viewId = this.hashCode();
    protected final UnitModel unitModel;

    protected UnitView(UnitModel unitModel) {
        this.unitModel = unitModel;
        LOGGER.info("Creating unit view {} for unit {}", this.viewId, unitModel.modelId);

        this.setFitWidth(Constants.TILE_SIZE);
        this.setFitHeight(Constants.TILE_SIZE);
        this.setTexture();
    }

    protected abstract void setTexture();
}