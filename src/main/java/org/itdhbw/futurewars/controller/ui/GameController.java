package org.itdhbw.futurewars.controller.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;


public class GameController {
    @FXML
    private Label selectedTileDebug;
    @FXML
    private Label movingDebug;
    @FXML
    private Label hoveredTileDebug;

    public GameController() {
        // Called by FXMLLoader
    }

    public void initialize() {
        selectedTileDebug.textProperty().bind(createTileBinding(GameState.getInstance().getSelectedTileProperty(), "No tile selected"));
        hoveredTileDebug.textProperty().bind(createTileBinding(GameState.getInstance().getHoveredTileProperty(), "No tile hovered"));
        movingDebug.textProperty().bind(createModeBinding(GameState.getInstance().activeModeProperty()));
    }

    private StringBinding createModeBinding(ObjectProperty<ActiveMode> activeModeProperty) {
        return Bindings.createStringBinding(
                () -> {
                    ActiveMode activeMode = activeModeProperty.get();
                    return (activeMode != null) ? String.valueOf(activeMode) : "Error!";
                },
                activeModeProperty
        );
    }

    private StringBinding createTileBinding(ObjectProperty<TileModel> tileProperty, String defaultMessage) {
        return Bindings.createStringBinding(
                () -> {
                    TileModel tile = tileProperty.get();
                    return (tile != null) ? String.valueOf(tile.modelId) : defaultMessage;
                },
                tileProperty
        );
    }
}
