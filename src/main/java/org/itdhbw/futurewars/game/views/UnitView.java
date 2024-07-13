package org.itdhbw.futurewars.game.views;

import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

public class UnitView extends StackPane {
    protected final UnitModel unitModel;
    private final ImageView textureLayer = new ImageView();

    public UnitView(UnitModel unitModel) {
        this.unitModel = unitModel;

        this.textureLayer.fitHeightProperty().bind(Context.getGameState().tileSizeProperty());
        this.textureLayer.fitWidthProperty().bind(Context.getGameState().tileSizeProperty());
        this.textureLayer.getStyleClass().add("image-border");

        Text hpText = new Text();
        hpText.textProperty().bind(unitModel.currentHealthProperty().asString());
        hpText.setFill(Color.WHITE);
        hpText.getStyleClass().addAll("black-stroke-border", "pixel-font", "hp-text");


        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-0.5);

        this.unitModel.hasMadeAnActionProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                this.setOpacity(0.7);
                this.textureLayer.setEffect(colorAdjust);
            } else {
                this.setOpacity(1);
                this.textureLayer.setEffect(null);
            }
        });

        this.getChildren().add(this.textureLayer);
        this.getChildren().add(hpText);
        StackPane.setAlignment(hpText, Pos.BOTTOM_RIGHT);
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
