package org.itdhbw.futurewars.game.views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

/**
 * The type Unit view.
 */
public class UnitView extends ImageView {
    private static final Logger LOGGER = LogManager.getLogger(UnitView.class);
    /**
     * The View id.
     */
    public final int viewId = this.hashCode();
    /**
     * The Unit model.
     */
    protected final UnitModel unitModel;

    /**
     * Instantiates a new Unit view.
     *
     * @param unitModel the unit model
     */
    public UnitView(UnitModel unitModel) {
        this.unitModel = unitModel;
        LOGGER.info("Creating unit view {} for unit {}", this.viewId,
                    unitModel.modelId);

        this.fitHeightProperty()
            .bind(Context.getGameState().tileSizeProperty());
        this.fitWidthProperty().bind(Context.getGameState().tileSizeProperty());
    }

    /**
     * Sets texture.
     *
     * @param texture1 the texture 1
     * @param texture2 the texture 2
     */
    public void setTexture(Image texture1, Image texture2) {
        if (this.unitModel.getTeam() == 1) {
            this.setImage(texture1);
        } else {
            this.setImage(texture2);
        }
    }
}
