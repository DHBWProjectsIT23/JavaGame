package org.itdhbw.futurewars.model.editor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.unit.factory.UnitFactory;
import org.itdhbw.futurewars.model.game.Context;

import java.util.ArrayList;
import java.util.List;

public class EditorTile extends StackPane {
    private static final Image BLANK_TILE = new Image("file:resources/textures/TileNotSet.png");
    private static final Logger LOGGER = LogManager.getLogger(EditorTile.class);
    private final ImageView tileOccupiedOverlay = new ImageView(new Image("file:resources/textures/64Highlighted.png"));
    private final ImageView unitTextureOverlay = new ImageView();
    private final Label tileTypeLabel;
    private final Label unitTypeLabel;
    private final Label unitTeamLabel;
    private final StringProperty unitType = new SimpleStringProperty();
    private final StringProperty tileType = new SimpleStringProperty();
    private final ImageView imageView = new ImageView();
    private final List<Image> textures = new ArrayList<>();
    private final IntegerProperty unitTeam = new SimpleIntegerProperty();
    private int textureVariant = 0;

    public EditorTile(String tileType) {
        super();
        imageView.fitWidthProperty().bind(this.prefWidthProperty());
        imageView.fitHeightProperty().bind(this.prefHeightProperty());

        tileOccupiedOverlay.fitWidthProperty().bind(this.prefWidthProperty());
        tileOccupiedOverlay.fitHeightProperty().bind(this.prefHeightProperty());

        unitTextureOverlay.fitWidthProperty().bind(this.prefWidthProperty());
        unitTextureOverlay.fitHeightProperty().bind(this.prefHeightProperty());

        this.getChildren().add(imageView);

        tileTypeLabel = new Label();
        unitTypeLabel = new Label();
        unitTeamLabel = new Label();

        tileTypeLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        unitTypeLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        unitTeamLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        tileTypeLabel.textProperty().bind(this.tileType);

        unitType.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                unitTypeLabel.setText("No Unit");
                unitTeamLabel.setText("");
                this.getChildren().remove(tileOccupiedOverlay);
                this.getChildren().remove(unitTextureOverlay);
            } else {
                unitTypeLabel.setText(newValue);
                unitTeamLabel.setText(this.unitTeam.get() == 1 ? "Team 1" : "Team 2");
                UnitFactory factory = Context.getUnitBuilder().getUnitFactories().get(newValue);
                if (factory == null) {
                    LOGGER.error("No factory found for unit type {}", newValue);
                    this.getChildren().add(tileOccupiedOverlay);
                    return;
                }
                Pair<String, String> unit_textures = factory.getUnitTextures();
                String texture = this.unitTeam.get() == 1 ? unit_textures.getKey() : unit_textures.getValue();
                unitTextureOverlay.setImage(new Image("file:" + texture));
                this.getChildren().add(unitTextureOverlay);
            }
        });
        unitType.set(null);

        unitTeam.addListener((observable, oldValue, newValue) -> {
            if (this.unitType.get() == null) {
                return;
            }
            unitTeamLabel.setText(newValue.equals(1) ? "Team 1" : "Team 2");
        });
        unitTeam.set(1);


        VBox labelBox = new VBox();
        labelBox.getChildren().addAll(tileTypeLabel, unitTypeLabel, unitTeamLabel);
        this.getChildren().add(labelBox);

        this.tileType.set(tileType);

        this.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS)));
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

    public StringProperty tileTypeProperty() {
        return tileType;
    }

    public void setLabelsVisible(boolean visible) {
        tileTypeLabel.setVisible(visible);
        unitTypeLabel.setVisible(visible);
        unitTeamLabel.setVisible(visible);
    }

    public void setOpacityOnSelection(boolean isSelected) {
        if (isSelected) {
            this.setOpacity(0.5); // reduce opacity when selected
        } else {
            this.setOpacity(1.0); // restore opacity when not selected
        }
    }

    public void setBackgroudImage(Image image) {
        this.imageView.setImage(image);
    }

    public void setTextures(List<Image> textures) {
        this.textures.clear();
        this.textures.addAll(textures);
        this.setBackgroudImage(textures.getFirst());
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
            setBackgroudImage(texture);
        } else {
            LOGGER.error("Texture not found for variant: {}", textureVariant);
            setBackgroudImage(BLANK_TILE);
        }
    }
}

