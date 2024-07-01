package org.itdhbw.futurewars.game.controllers.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.tile.TileCreationController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.models.game_state.ActiveMode;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.TileView;

public class MapViewController {
    private static final Logger LOGGER = LogManager.getLogger(MapViewController.class);
    private final TileCreationController tileCreationController;
    private final UnitMovementController unitMovementController;
    private final GameState gameState;
    private final DoubleProperty mouseX = new SimpleDoubleProperty();
    private final DoubleProperty mouseY = new SimpleDoubleProperty();

    @FXML
    private GridPane gameGrid;
    @FXML
    private AnchorPane overlayPane;
    @FXML
    private Button overlayMoveButton;
    @FXML
    private Button overlayCloseButton;
    @FXML
    private VBox overlayBox;
    @FXML
    private Button overlayAttackButton;
    @FXML
    private AnchorPane statusViewOverlay;
    @FXML
    private VBox statusViewVBox;
    @FXML
    private ImageView currentTileTexture;
    @FXML
    private Label currentTileType;
    @FXML
    private Label currentTileDef;
    @FXML
    private Label currentTileAtk;
    @FXML
    private ImageView currentUnitTexture;
    @FXML
    private Label currentUnitType;
    @FXML
    private Label currentUnitHealth;
    @FXML
    private Button overlayInfoButton;
    @FXML
    private HBox currentUnitHBox;
    @FXML
    private Pane infoViewSeperator;

    public MapViewController() {
        this.tileCreationController = Context.getTileCreationController();
        this.unitMovementController = Context.getUnitMovementController();
        this.gameState = Context.getGameState();
    }


    public void initialize() {
        hideOverlay();
        hideTileStatusView();
        hideUnitStatusView();

        this.addTilesToGrid();
        LOGGER.info("Creating Units...");


        this.gameState.activeModeProperty().addListener((_, _, newValue) -> {
            if (newValue == ActiveMode.OVERLAY) {
                this.showOverlay();
            } else {
                this.hideOverlay();
            }
        });
        this.gameGrid.setOnMouseMoved(event -> {
            mouseX.set(event.getX() + 20);
            mouseY.set(event.getY() + 10);
        });

        this.gameState.selectedTileProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                this.showTileStatusView(newValue);
            } else {
                this.hideTileStatusView();
            }
        });

        this.gameState.selectedUnitProperty().addListener((_, _, newValue) -> {
            if (newValue.isPresent()) {
                // Throw properly
                this.showUnitStatusView(newValue.orElseThrow());
            } else {
                this.hideUnitStatusView();
            }
        });

    }

    private void hideTileStatusView() {
        this.statusViewOverlay.setVisible(false);
    }

    private void showTileStatusView(TileModel selectedTile) {
        this.statusViewOverlay.setVisible(true);
        this.currentTileType.setText(selectedTile.getTileType());
        this.currentTileDef.setText("Def: " + selectedTile.getTerrainCover() / 10 + "/5");
        Image tileTexture = Context.getTileRepository().getTileView(selectedTile.getPosition()).getTexture();
        this.currentTileTexture.setImage(tileTexture);
    }

    private void hideUnitStatusView() {
        this.statusViewVBox.getChildren().removeAll(this.currentUnitHBox, this.infoViewSeperator);
    }

    private void showUnitStatusView(UnitModel selectedUnitModel) {
        LOGGER.info("Showing unit status view for unit {}", selectedUnitModel.modelId);
        this.currentUnitTexture.setImage(Context.getUnitRepository().getUnitView(selectedUnitModel).getTexture());
        this.currentUnitType.setText(selectedUnitModel.getUnitType());
        this.currentUnitHealth.setText(
                selectedUnitModel.getCurrentHealth() + "/" + selectedUnitModel.getMaxHealth() + " ♥");
        this.statusViewVBox.getChildren().addAll(this.currentUnitHBox, this.infoViewSeperator);
    }

    private void hideOverlay() {
        this.overlayPane.setVisible(false);
        this.overlayPane.setDisable(true);
    }

    private void showOverlay() {
        LOGGER.info("Showing overlay...");
        LOGGER.info("Mouse X: {} - Mouse Y: {}", mouseX.get(), mouseY.get());
        this.overlayBox.getChildren().remove(overlayMoveButton);
        this.overlayBox.getChildren().remove(overlayAttackButton);
        this.overlayBox.getChildren().remove(overlayInfoButton);
        this.overlayBox.getChildren().remove(overlayCloseButton);
        if (gameState.getSelectedUnit().orElse(null).canAttack() &&
            gameState.getSelectedUnit().orElse(null).canMove()) {
            this.overlayBox.getChildren().add(overlayMoveButton);
            this.overlayBox.getChildren().add(overlayAttackButton);
            this.overlayBox.getChildren().add(overlayInfoButton);
            this.overlayBox.getChildren().add(overlayCloseButton);
        } else if (gameState.getSelectedUnit().orElse(null).canMove()) {
            this.overlayBox.getChildren().add(overlayMoveButton);
            this.overlayBox.getChildren().add(overlayInfoButton);
            this.overlayBox.getChildren().add(overlayCloseButton);
        } else {
            this.overlayBox.getChildren().add(overlayInfoButton);
            this.overlayBox.getChildren().add(overlayCloseButton);
        }
        this.overlayBox.setLayoutX(mouseX.get());
        this.overlayBox.setLayoutY(mouseY.get());
        this.overlayPane.setVisible(true);
        this.overlayPane.setDisable(false);
    }

    private void addTileToGrid(Pair<TileModel, TileView> tile) {
        LOGGER.info("Pair: {} - Model: {} - View: {}", tile, tile.getKey(), tile.getValue());
        LOGGER.info("Tile position: {}", tile.getKey().getPosition());
        Position position = tile.getKey().getPosition();
        gameGrid.add(tile.getValue(), position.getX(), position.getY());
    }

    private void addTilesToGrid() {
        LOGGER.info("Loading map...");

        Pair<TileModel, TileView>[][] allTiles = Context.getTileRepository().getAllTiles();
        for (int x = 0; x < (gameState.getMapWidthTiles()); x++) {
            for (int y = 0; y < (gameState.getMapHeightTiles()); y++) {
                LOGGER.error("x: {} of {}, y: {} of {}", x, gameState.getMapWidthTiles(), y,
                             gameState.getMapHeightTiles());
                Pair<TileModel, TileView> tilePair = allTiles[x][y];
                if (tilePair == null) {
                    LOGGER.warn("tilePair was null");
                    tilePair = tileCreationController.createTile("NOT_SET_TILE", x, y);
                }
                this.addTileToGrid(tilePair);
            }
        }


    }

    @FXML
    private void enterMoveMode(ActionEvent actionEvent) {
        unitMovementController.moveUnit(gameState.selectedUnitProperty().get().orElse(null),
                                        gameState.selectedTileProperty().get());
        this.gameState.setActiveMode(ActiveMode.REGULAR);
    }

    @FXML
    private void closeOverlay(ActionEvent actionEvent) {
        this.gameState.setActiveMode(ActiveMode.REGULAR);
    }

    @FXML
    private void enterAttackMode(ActionEvent actionEvent) {
        LOGGER.info("Entering attack mode...");
        this.gameState.setActiveMode(ActiveMode.ATTACKING_UNIT);
    }
}