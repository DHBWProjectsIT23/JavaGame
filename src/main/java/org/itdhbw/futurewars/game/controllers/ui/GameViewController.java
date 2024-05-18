package org.itdhbw.futurewars.game.controllers.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorPopup;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.game.models.gameState.ActiveMode;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

import java.io.IOException;


/**
 * The type Game view controller.
 */
public class GameViewController {

    private static final Logger LOGGER =
            LogManager.getLogger(GameViewController.class);
    private final GameState gameState;
    @FXML
    private Label selectedTileDebug;
    @FXML
    private Label movingDebug;
    @FXML
    private Label hoveredTileDebug;
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
    @FXML
    private AnchorPane optionsPane;
    @FXML
    private AnchorPane escapeMenu;
    @FXML
    private Label currentLabel;

    /**
     * Instantiates a new Game view controller.
     */
    public GameViewController() {
        this.gameState = Context.getGameState();
    }

    /**
     * Initialize.
     */
    public void initialize() {
        selectedTileDebug.textProperty().bind(createTileBinding(
                gameState.selectedTileProperty(), "No tile selected"));
        hoveredTileDebug.textProperty()
                        .bind(createTileBinding(gameState.hoveredTileProperty(),
                                                "No tile hovered"));
        movingDebug.textProperty()
                   .bind(createModeBinding(gameState.activeModeProperty()));
        currentLabel.textProperty().bind(Bindings.createStringBinding(
                () -> "Current player: " + gameState.getCurrentPlayer(),
                gameState.currentPlayerProperty()));
        gameState.selectedTileProperty().addListener((_, _, newValue) -> {
            if (newValue == null) {
                this.clearPropertyInformation();
            } else {
                this.setPropertyInformation();
            }
        });

        Platform.runLater(() -> {
            Scene scene = selectedTileDebug.getScene();
            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    escapeMenu.setVisible(!escapeMenu.isVisible());
                }
            });
        });
    }

    private void setPropertyInformation() {
        TileModel selectedTile = gameState.selectedTileProperty().get();
        this.selectedTileType.setText(
                selectedTile.getMovementType().toString());
        if (selectedTile.isOccupied()) {
            UnitModel occupyingUnit = selectedTile.getOccupyingUnit();
            this.selectedUnitType.setText(occupyingUnit.getUnitType());
            this.selectedUnitHP.setText(occupyingUnit.getCurrentHealth() + "/" +
                                        occupyingUnit.getMaxHealth());
            this.selectedUnitRange.setText(
                    String.valueOf(occupyingUnit.getAttackRange()));
            this.selectedUnitTeam.setText(
                    String.valueOf(occupyingUnit.getTeam()));
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
        return Bindings.createStringBinding(() -> {
            ActiveMode activeMode = activeModeProperty.get();
            return (activeMode != null) ? String.valueOf(activeMode) : "Error!";
        }, activeModeProperty);
    }

    private StringBinding createTileBinding(ObjectProperty<TileModel> tileProperty, String defaultMessage) {
        return Bindings.createStringBinding(() -> {
            TileModel tile = tileProperty.get();
            return (tile != null) ?
                   tile.getPosition() + " - Type: " + tile.getMovementType() :
                   defaultMessage;
        }, tileProperty);
    }

    @FXML
    private void openSettings(ActionEvent actionEvent) {
        Stage stage = Context.getPrimaryStage();
        Context.getGameState().setPreviousScene(stage.getScene());
        try {
            Parent optionsView = FXMLLoader.load(
                    FileHelper.getFxmlFile("options-view.fxml").toURL());
            Scene scene = new Scene(optionsView);
            Context.getGameState().setPreviousScene(stage.getScene());
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FailedToLoadFileException e) {
            LOGGER.error("Error while opening settings", e);
            ErrorPopup.showRecoverableErrorPopup("Failed to open settings", e);
        }
        escapeMenu.setVisible(false);
    }

    @FXML
    private void quitToMenu(ActionEvent actionEvent) {
        Stage stage = Context.getPrimaryStage();
        try {
            Parent menuView = FXMLLoader.load(
                    FileHelper.getFxmlFile("menu-view.fxml").toURL());
            Scene scene = new Scene(menuView);
            stage.setScene(scene);
            Context.getOptionsController().loadSettings();
        } catch (IOException | FailedToLoadFileException e) {
            LOGGER.error("Error while quitting to menu", e);
            ErrorPopup.showRecoverableErrorPopup("Failed to quit to menu", e);
        }
    }

    @FXML
    private void quitToDesktop(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    private void endTurn(ActionEvent actionEvent) {
        Context.getGameState().endTurn();
    }
}
