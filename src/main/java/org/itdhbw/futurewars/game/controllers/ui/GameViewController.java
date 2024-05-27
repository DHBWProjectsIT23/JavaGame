package org.itdhbw.futurewars.game.controllers.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.controllers.other.SceneController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;
import org.itdhbw.futurewars.game.models.gameState.ActiveMode;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;

import java.net.MalformedURLException;
import java.net.URL;

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
    private StackPane escapeMenu;
    @FXML
    private Label currentLabel;
    @FXML
    private AnchorPane backgroundPane;
    @FXML
    private AnchorPane gamePane;

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

        try {
            URL backgroundImage = FileHelper.getFile(
                    "$INTERNAL_DIR/assets/advanceWarsSplash.jpg").toURL();
            backgroundPane.setStyle(
                    "-fx-background-image: url('" + backgroundImage + "')");
        } catch (FailedToLoadFileException | MalformedURLException e) {
            ErrorHandler.addException(e, "Failed to load background image");
        }

        gameState.currentPlayerProperty().addListener((_, _, newValue) -> {
            if (newValue.equals(1)) {
                LOGGER.info("Player 1's turn");
                gamePane.getStyleClass().remove("red-border");
                gamePane.getStyleClass().add("blue-border");
            } else {
                LOGGER.info("Player 2's turn");
                gamePane.getStyleClass().remove("blue-border");
                gamePane.getStyleClass().add("red-border");
            }
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
        try {
            SceneController.loadScene("options-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to load settings view");
            ErrorHandler.showErrorPopup();
        }
    }

    @FXML
    private void quitToMenu(ActionEvent actionEvent) {
        try {
            SceneController.loadScene("menu-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addException(e, "Failed to load menu view");
            ErrorHandler.showErrorPopup();
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
