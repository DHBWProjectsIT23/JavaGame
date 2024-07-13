package org.itdhbw.futurewars.game.controllers.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.itdhbw.futurewars.application.controllers.other.SceneController;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ConfirmPopup;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.FailedToLoadSceneException;
import org.itdhbw.futurewars.exceptions.FailedToLoadTextureException;
import org.itdhbw.futurewars.game.controllers.unit.UnitRepository;
import org.itdhbw.futurewars.game.models.game_state.GameState;

public class GameViewController {
    private final GameState gameState;
    private final UnitRepository unitRepository;
    @FXML
    private StackPane escapeMenu;
    @FXML
    private AnchorPane backgroundPane;
    @FXML
    private AnchorPane gamePane;
    @FXML
    private Text currentTurnLabel;
    @FXML
    private Text currentPlayerLabel;
    @FXML
    private StackPane parentPane;
    @FXML
    private Text gameOverText;
    @FXML
    private VBox gameOverBox;
    @FXML
    private Button endTurnButton;
    @FXML
    private VBox labelBox;
    @FXML
    private StackPane backgroundImagePane;

    public GameViewController() {
        this.gameState = Context.getGameState();
        this.unitRepository = Context.getUnitRepository();
    }

    public void initialize() {
        Platform.runLater(() -> {
            Scene scene = Context.getPrimaryStage().getScene();
            scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    escapeMenu.setVisible(!escapeMenu.isVisible());
                }
            });
        });

        try {
            Image backgroundImage = FileHelper.getMiscTexture(FileHelper.MiscTextures.SPLASH_ART);
            backgroundImagePane.setStyle("-fx-background-image: url('" + backgroundImage.getUrl() + "')");
            backgroundImagePane.setOpacity(0.7);
        } catch (FailedToLoadTextureException e) {
            ErrorHandler.addException(e, "Failed to load background image");
        }

        gameState.currentPlayerProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(1)) {
                setTurnPlayer1();
            } else {
                setTurnPlayer2();
            }
        });

        gameState.currentDayProperty()
                 .addListener((observable, oldValue, newValue) -> currentTurnLabel.setText("Day " + newValue));

        unitRepository.team1UnitCountProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() <= 0) {
                showGameOverScreen(2);
            }
        });

        unitRepository.team2UnitCountProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() <= 0) {
                showGameOverScreen(1);
            }
        });

        double width = labelBox.getPrefWidth();
        double widthWithMargins = width + (width / 100 * 40);
        labelBox.setMinWidth(widthWithMargins);

        setTurnPlayer1();

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

    private void setTurnPlayer1() {
        gamePane.getStyleClass().remove("red-border");
        gamePane.getStyleClass().add("blue-border");

        endTurnButton.getStyleClass().remove("red-button");

        currentPlayerLabel.setText("Player 1");

    }

    private void setTurnPlayer2() {
        gamePane.getStyleClass().remove("blue-border");
        gamePane.getStyleClass().add("red-border");

        endTurnButton.getStyleClass().add("red-button");

        currentPlayerLabel.setText("Player 2");
    }

    private void showGameOverScreen(int player) {
        if (player == 1) {
            gameOverText.setText("Player 1 wins!");
        } else {
            gameOverText.setText("Player 2 wins!");
        }
        gameOverBox.setVisible(true);
    }

    @FXML
    private void quitToMenu(ActionEvent actionEvent) {
        ConfirmPopup.showWithRunnable(parentPane, "Are you sure you want to quit to the menu?",
                                      "The current game state will be lost", this::quitToMenuRunnable);
    }

    private void quitToMenuRunnable() {
        gameState.resetGame();
        try {
            SceneController.loadScene("menu-view.fxml");
        } catch (FailedToLoadSceneException e) {
            ErrorHandler.addVerboseException(e, "Failed to load menu view");
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

    @FXML
    private void returnToMenu(ActionEvent actionEvent) {
        quitToMenuRunnable();
    }
}
