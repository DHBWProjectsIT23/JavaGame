package org.itdhbw.futurewars.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.Tile;


public class GameController {
    @FXML
    private Label selectedTileDebug;
    @FXML
    private Label movingDebug;
    @FXML
    private Label hoveredTileDebug;

    private GameController() {
        // private constructor to hide the implicit public one
    }

    public void initialize() {
        selectedTileDebug.textProperty().bind(createTileBinding(GameState.getSelectedTileProperty(), "No tile selected"));
        hoveredTileDebug.textProperty().bind(createTileBinding(GameState.getHoveredTileProperty(), "No tile hovered"));
    }

    private StringBinding createTileBinding(ObjectProperty<Tile> tileProperty, String defaultMessage) {
        return Bindings.createStringBinding(
                () -> {
                    Tile tile = tileProperty.get();
                    return (tile != null) ? tile.getId() : defaultMessage;
                },
                tileProperty
        );
    }
}
