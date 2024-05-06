package org.itdhbw.futurewars.controller.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;


public class GameController {
    private final GameState gameState;
    @FXML
    private Label selectedTileDebug;
    @FXML
    private Label movingDebug;
    @FXML
    private Label hoveredTileDebug;
    @FXML
    private Button map1Button;
    @FXML
    private Button map2Button;
    @FXML
    private Button map3Button;
    @FXML
    private Button newMap1Button;
    @FXML
    private Button newMap2Button;
    @FXML
    private Button newMap3Button;

    public GameController() {
        this.gameState = Context.getGameState();
    }

    public void initialize() {
        selectedTileDebug.textProperty().bind(createTileBinding(gameState.getSelectedTileProperty(), "No tile selected"));
        hoveredTileDebug.textProperty().bind(createTileBinding(gameState.getHoveredTileProperty(), "No tile hovered"));
        movingDebug.textProperty().bind(createModeBinding(gameState.activeModeProperty()));
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
                    return (tile != null) ? tile.getPosition() + " - Type: " + tile.getTileType() : defaultMessage;
                },
                tileProperty
        );
    }

    @FXML
    private void loadMap1(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMap1.csv");
    }

    @FXML
    private void loadMap2(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/testMap2.csv");
    }

    @FXML
    private void loadMap3(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/testMap3.csv");
    }

    @FXML
    private void loadNewMap1(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/newMap1.csv");
    }

    @FXML
    private void loadNewMap2(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/newMap2.csv");
    }

    @FXML
    private void loadNewMap3(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/newMap3.csv");
    }
}
