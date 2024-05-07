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
    @FXML
    private Label selectedUnitTeam;
    @FXML
    private Label selectedUnitRange;
    @FXML
    private Label selectedUnitHP;
    @FXML
    private Label selectedUnitType;
    @FXML
    private Label selectedTileType;

    public GameController() {
        this.gameState = Context.getGameState();
    }

    public void initialize() {
        selectedTileDebug.textProperty().bind(createTileBinding(gameState.getSelectedTileProperty(), "No tile selected"));
        hoveredTileDebug.textProperty().bind(createTileBinding(gameState.getHoveredTileProperty(), "No tile hovered"));
        movingDebug.textProperty().bind(createModeBinding(gameState.activeModeProperty()));
        gameState.getSelectedTileProperty().addListener((_, _, newValue) -> {
            if (newValue == null) {
                this.clearPropertyInformation();
            } else {
                this.setPropertyInformation();
            }
        });
    }

    private void setPropertyInformation() {
        TileModel selectedTile = gameState.getSelectedTileProperty().get();
        this.selectedTileType.setText(selectedTile.getTileType().toString());
        if (selectedTile.isOccupied()) {
            this.selectedUnitType.setText(selectedTile.getOccupyingUnit().getUnitType().toString());
            this.selectedUnitHP.setText("TBD"); //selectedTile.getOccupyingUnit().getHp());
            this.selectedUnitRange.setText("TBD");  //selectedTile.getOccupyingUnit().getRange());
            this.selectedUnitTeam.setText(String.valueOf(selectedTile.getOccupyingUnit().getTeam()));
        }
    }

    private void clearPropertyInformation() {
        this.selectedTileType.setText("No tile selected");
        this.selectedUnitType.setText("No unit selected");
        this.selectedUnitHP.setText("");
        this.selectedUnitRange.setText("");
        this.selectedUnitTeam.setText("");
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
        Context.getMapController().loadMap("resources/maps/testMaps/testMap1.fwm");
    }

    @FXML
    private void loadMap2(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/testMap2.fwm");
    }

    @FXML
    private void loadMap3(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/testMap3.fwm");
    }

    @FXML
    private void loadNewMap1(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/newMap1.fwm");
    }

    @FXML
    private void loadNewMap2(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/newMap2.fwm");
    }

    @FXML
    private void loadNewMap3(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/newMap3.fwm");
    }

    public void loadLittleIsland(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/maps/littleIsland.fwm");
    }
    public void loadEonSprings(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/maps/eonSprings.fwm");
    }

    public void loadPistonDam(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/maps/pistonDam.fwm");
    }

    public void loadTestMapX(ActionEvent actionEvent) {
        Context.getMapController().loadMap("resources/maps/testMaps/testMapX.fwm");
    }
}
