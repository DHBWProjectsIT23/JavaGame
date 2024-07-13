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
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EditorTile extends StackPane {
    private static final Image BLANK_TILE = new Image("file:resources/textures/FallbackTile.png");
    private static final Logger LOGGER = Logger.getLogger(EditorTile.class.getSimpleName());
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


        this.widthProperty().addListener((observable, oldValue, newValue) -> this.setHeight(newValue.doubleValue()));

        this.heightProperty().addListener((observable, oldValue, newValue) -> this.setWidth(newValue.doubleValue()));


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
                    LOGGER.severe("No factory found for unit type " + newValue);
                    this.getChildren().add(tileOccupiedOverlay);
                    return;
                }
                Pair<Image, Image> unitTextures = factory.getUnitTextures();
                Image texture = this.unitTeam.get() == 1 ? unitTextures.getKey() : unitTextures.getValue();
                unitTextureOverlay.setImage(texture);
                this.getChildren().add(unitTextureOverlay);
            }
        });
        unitTeam.addListener((observable, oldValue, newValue) -> {
            if (unitType.get() == null) {
                return;
            }
            UnitFactory factory = Context.getUnitBuilder().getUnitFactories().get(unitType.get());
            Pair<Image, Image> unitTextures = factory.getUnitTextures();
            Image texture = newValue.equals(1) ? unitTextures.getKey() : unitTextures.getValue();
            unitTextureOverlay.setImage(texture);
            this.getChildren().remove(unitTextureOverlay);
            this.getChildren().add(unitTextureOverlay);
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
        if (unitType.equals("No Unit")) {
            this.unitType.set(null);
            return;
        }
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

    public void setTextures(List<Image> textures) {
        this.textures.clear();
        this.textures.addAll(textures);
        this.setBackgroundImage(textures.getFirst());
    }

    public void setTextureVariant(int textureVariant) {
        this.textureVariant = textureVariant;
        Image texture = textures.get(textureVariant);
        if (texture != null && !texture.isError()) {
            setBackgroundImage(texture);
        } else {
            ErrorHandler.addException(new FailedToLoadTextureException(String.valueOf(textureVariant), ""),
                                      "Failed to load texture variant " + textureVariant + " for tile " + this);
            setBackgroundImage(BLANK_TILE);
        }
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

    public void setBackgroundImage(Image image) {
        this.imageView.setImage(image);
    }

    @Override
    public int hashCode() {
        int result = tileOccupiedOverlay.hashCode();
        result = 31 * result + unitTextureOverlay.hashCode();
        result = 31 * result + unitType.hashCode();
        result = 31 * result + tileType.hashCode();
        result = 31 * result + imageView.hashCode();
        result = 31 * result + textures.hashCode();
        result = 31 * result + unitTeam.hashCode();
        result = 31 * result + textureVariant;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EditorTile that = (EditorTile) o;
        return textureVariant == that.textureVariant && tileOccupiedOverlay.equals(that.tileOccupiedOverlay) &&
               unitTextureOverlay.equals(that.unitTextureOverlay) && unitType.equals(that.unitType) &&
               tileType.equals(that.tileType) && imageView.equals(that.imageView) && textures.equals(that.textures) &&
               unitTeam.equals(that.unitTeam);
    }

    @Override
    public String toString() {
        return "EditorTile{" + "tileOccupiedOverlay=" + tileOccupiedOverlay + ", unitTextureOverlay=" +
               unitTextureOverlay + ", unitType=" + unitType + ", tileType=" + tileType + ", imageView=" + imageView +
               ", textures=" + textures + ", unitTeam=" + unitTeam + ", textureVariant=" + textureVariant + '}';
    }
}

