package org.itdhbw.futurewars.map_editor.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitFactory;

import java.util.ArrayList;
import java.util.List;

public class EditorTile extends StackPane {
    private static final Image BLANK_TILE = new Image("file:resources/textures/FallbackTile.png");
    private static final Logger LOGGER = LogManager.getLogger(EditorTile.class);
    private final ImageView tileOccupiedOverlay = new ImageView(new Image("file:resources/textures/64Highlighted.png"));
    private final ImageView unitTextureOverlay = new ImageView();
    private final StringProperty unitType = new SimpleStringProperty();
    private final StringProperty tileType = new SimpleStringProperty();
    private final ImageView imageView = new ImageView();
    private final List<Image> textures = new ArrayList<>();
    private final IntegerProperty unitTeam = new SimpleIntegerProperty();
    private int textureVariant = 0;

    public EditorTile(String tileType) {
        super();
        this.minHeight(32);
        this.minWidth(32);


        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.setHeight(newValue.doubleValue());
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.setWidth(newValue.doubleValue());
        });


        imageView.fitWidthProperty().bind(this.prefWidthProperty());
        imageView.fitHeightProperty().bind(this.prefHeightProperty());

        tileOccupiedOverlay.fitWidthProperty().bind(this.prefWidthProperty());
        tileOccupiedOverlay.fitHeightProperty().bind(this.prefHeightProperty());

        unitTextureOverlay.fitWidthProperty().bind(this.prefWidthProperty());
        unitTextureOverlay.fitHeightProperty().bind(this.prefHeightProperty());

        this.getChildren().add(imageView);

        unitType.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                this.getChildren().remove(tileOccupiedOverlay);
                this.getChildren().remove(unitTextureOverlay);
            } else {
                UnitFactory factory = Context.getUnitBuilder().getUnitFactories().get(newValue);
                if (factory == null) {
                    LOGGER.error("No factory found for unit type {}", newValue);
                    this.getChildren().add(tileOccupiedOverlay);
                    return;
                }
                Pair<Image, Image> unitTextures = factory.getUnitTextures();
                Image texture = this.unitTeam.get() == 1 ? unitTextures.getKey() : unitTextures.getValue();
                unitTextureOverlay.setImage(texture);
                this.getChildren().add(unitTextureOverlay);
            }
        });
        unitType.set(null);
        unitTeam.set(1);
        this.tileType.set(tileType);

        this.setBorder(new Border(
                new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
    }

    public String getUnitType() {
        return unitType.get();
    }

    public void setUnitType(String unitType) {
        this.unitType.set(unitType);
    }

    public StringProperty unitTypeProperty() {
        return unitType;
    }

    public String getTileType() {
        return tileType.get();
    }

    public void setTileType(String tileType) {
        this.tileType.set(tileType);
    }

    public void setOpacityOnSelection(boolean isSelected) {
        if (isSelected) {
            this.setOpacity(0.5); // reduce opacity when selected
        } else {
            this.setOpacity(1.0); // restore opacity when not selected
        }
    }

    public void setBackgroundImage(Image image) {
        this.imageView.setImage(image);
    }

    public void setTextures(List<Image> textures) {
        this.textures.clear();
        this.textures.addAll(textures);
        this.setBackgroundImage(textures.getFirst());
    }

    public int getUnitTeam() {
        return unitTeam.get();
    }

    public void setUnitTeam(int team) {
        this.unitTeam.set(team);
    }

    public int getTextureVariant() {
        return textureVariant;
    }

    public void setTextureVariant(int textureVariant) {
        this.textureVariant = textureVariant;
        Image texture = textures.get(textureVariant);
        if (texture != null && !texture.isError()) {
            setBackgroundImage(texture);
        } else {
            LOGGER.error("Texture not found for variant: {}", textureVariant);
            setBackgroundImage(BLANK_TILE);
        }
    }
}

