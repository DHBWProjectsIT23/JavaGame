package org.itdhbw.futurewars.model.editor;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.model.unit.UnitType;

public class EditorTile extends VBox {
    private final Label label1;
    private final Label label2;
    private final ObjectProperty<UnitType> unitType = new SimpleObjectProperty<>();
    private final ObjectProperty<TileType> tileType = new SimpleObjectProperty<>();

    public EditorTile() {
        super();

        label1 = new Label();
        label2 = new Label();

        label1.textProperty().bind(tileType.asString());

        unitType.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                label2.setText("");
                this.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            } else {
                label2.setText(newValue.name());
                this.setBorder(new Border(new BorderStroke(Color.HOTPINK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2.0))));
            }
        });


        getChildren().addAll(label1, label2);


        tileType.addListener((observable, oldValue, newValue) -> {
            // Change the background color based on the new tileType
            switch (newValue) {
                case PLAIN_TILE:
                    this.setStyle("-fx-background-color: green;");
                    break;
                case WOOD_TILE:
                    this.setStyle("-fx-background-color: brown;");
                    break;
                case SEA_TILE:
                    this.setStyle("-fx-background-color: blue;");
                    break;
                case MOUNTAIN_TILE:
                    this.setStyle("-fx-background-color: gray;");
                    break;
            }
        });
        this.tileType.set(TileType.PLAIN_TILE);
    }

    public UnitType getUnitType() {
        return unitType.get();
    }

    public void setUnitType(UnitType unitType) {
        this.unitType.set(unitType);
    }

    public ObjectProperty<UnitType> unitTypeProperty() {
        return unitType;
    }

    public TileType getTileType() {
        return tileType.get();
    }

    public void setTileType(TileType tileType) {
        this.tileType.set(tileType);
    }

    public ObjectProperty<TileType> tileTypeProperty() {
        return tileType;
    }

    public void setLabelsVisible(boolean visible) {
        label1.setVisible(visible);
        label2.setVisible(visible);
    }

    public void setOpacityOnSelection(boolean isSelected) {
        if (isSelected) {
            this.setOpacity(0.5); // reduce opacity when selected
        } else {
            this.setOpacity(1.0); // restore opacity when not selected
        }
    }
}

