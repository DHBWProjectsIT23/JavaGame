package org.itdhbw.futurewars.game.views;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

public class UnitView extends StackPane {
    private static final Logger LOGGER = LogManager.getLogger(UnitView.class);
    private final ImageView textureLayer = new ImageView();
    private final Text hpText = new Text();
    public final int viewId = this.hashCode();
    protected final UnitModel unitModel;

    public UnitView(UnitModel unitModel) {
        this.unitModel = unitModel;
        LOGGER.info("Creating unit view {} for unit {}", this.viewId, unitModel.modelId);

        this.textureLayer.fitHeightProperty().bind(Context.getGameState().tileSizeProperty());
        this.textureLayer.fitWidthProperty().bind(Context.getGameState().tileSizeProperty());

        this.hpText.textProperty()
                   .bind(Bindings.createStringBinding(() -> unitModel.currentHealthProperty().get() + "♥",
                                                      unitModel.currentHealthProperty().asString()));
        if (unitModel.getTeam() == 1) {
            this.hpText.setFill(Color.BLUE);
            this.hpText.getStyleClass().add("white-shadow");
        } else {
            this.hpText.setFill(Color.RED);
            this.hpText.getStyleClass().add("black-shadow");
        }

        this.getChildren().add(this.textureLayer);
        this.getChildren().add(this.hpText);
        StackPane.setAlignment(this.hpText, Pos.TOP_CENTER);
    }


    public void setTexture(Image texture1, Image texture2) {
        if (this.unitModel.getTeam() == 1) {
            this.textureLayer.setImage(texture1);
        } else {
            this.textureLayer.setImage(texture2);
        }
    }

    public Image getTexture() {
        return this.textureLayer.getImage();
    }
}
