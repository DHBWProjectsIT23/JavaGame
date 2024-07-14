package org.itdhbw.futurewars.game.controllers.ui;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.exceptions.NoUnitSelectedException;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileCreationController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.models.game_state.ActiveMode;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.logging.Logger;

public class MapViewController {
    private static final Logger LOGGER = Logger.getLogger(MapViewController.class.getSimpleName());
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
    private ImageView currentUnitTexture;
    @FXML
    private Label currentUnitType;
    @FXML
    private Label currentUnitHealth;
    @FXML
    private HBox currentUnitHBox;
    @FXML
    private Pane infoViewSeperator;
    @FXML
    private Button overlayMergeButton;
    @FXML
    private StackPane infoOverlay;
    @FXML
    private Text infoOverlayText;

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

        this.gameState.activeModeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == ActiveMode.OVERLAY_MODE) {
                this.showOverlay();
            } else {
                this.hideOverlay();
            }
        });
        this.gameGrid.setOnMouseMoved(event -> {
            mouseX.set(event.getX() + 20);
            mouseY.set(event.getY() + 10);
        });

        this.gameState.hoveredTileProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.hideTileStatusView();
                this.showTileStatusView(newValue);
                if (newValue.isOccupied()) {
                    this.hideUnitStatusView();
                    this.showUnitStatusView(newValue.getOccupyingUnit());
                } else {
                    this.hideUnitStatusView();
                }
            } else {
                this.hideTileStatusView();
            }
        });

        this.gameState.currentDayProperty()
                      .addListener((observable, oldValue, newValue) -> showDayChangeInfo(newValue));

        this.infoOverlayText.setOnMouseClicked(ignored -> this.infoOverlay.setVisible(false));

    }

    private void hideOverlay() {
        this.overlayPane.setVisible(false);
        this.overlayPane.setDisable(true);
    }

    private void hideTileStatusView() {
        this.statusViewOverlay.setVisible(false);
    }

    private void hideUnitStatusView() {
        this.statusViewVBox.getChildren().removeAll(this.currentUnitHBox, this.infoViewSeperator);
    }

    private void addTilesToGrid() {
        LOGGER.info("Adding tiles to grid...");

        Pair<TileModel, TileView>[][] allTiles = Context.getTileRepository().getAllTiles();
        for (int x = 0; x < (gameState.getMapWidthTiles()); x++) {
            for (int y = 0; y < (gameState.getMapHeightTiles()); y++) {
                Pair<TileModel, TileView> tilePair = allTiles[x][y];
                if (tilePair == null) {
                    LOGGER.warning("tilePair was null");
                    tilePair = tileCreationController.createUnsetTile(x, y);
                }
                this.addTileToGrid(tilePair);
            }
        }


    }

    private void showOverlay() {
        addOverlayButtons();

        int x = (int) mouseX.get();
        int y = (int) mouseY.get();

        int width = (int) this.overlayBox.getWidth();
        int height = (int) this.overlayBox.getHeight();

        if (x + width > gameGrid.getWidth()) {
            // Subtract 10 to make sure the overlay does not move the grid
            x = (int) (gameGrid.getWidth() - width - 10);
        }

        if (y + height > gameGrid.getHeight()) {
            // Subtract 10 to make sure the overlay does not move the grid
            y = (int) (gameGrid.getHeight() - height - 10);
        }

        this.overlayBox.setLayoutX(x);
        this.overlayBox.setLayoutY(y);

        this.overlayPane.setVisible(true);
        this.overlayPane.setDisable(false);
    }

    private void showTileStatusView(TileModel selectedTile) {
        setStatusViewSide(selectedTile);
        this.statusViewOverlay.setVisible(true);
        this.currentTileType.setText(selectedTile.getTileType().replace("_", " "));
        this.currentTileDef.setText("Def: " + selectedTile.getTerrainCover() / 10 + "/5");
        Image tileTexture = Context.getTileRepository().getTileView(selectedTile.getPosition()).getTexture();
        this.currentTileTexture.setImage(tileTexture);
    }

    private void showUnitStatusView(UnitModel selectedUnitModel) {
        this.currentUnitTexture.setImage(Context.getUnitRepository().getUnitView(selectedUnitModel).getTexture());
        this.currentUnitType.setText(selectedUnitModel.getUnitType().replace("_", " "));
        this.currentUnitHealth.setText(
                selectedUnitModel.getCurrentHealth() + "/" + selectedUnitModel.getMaxHealth() + " ♥");
        this.statusViewVBox.getChildren().addAll(this.currentUnitHBox, this.infoViewSeperator);
    }

    private void showDayChangeInfo(Number day) {
        this.infoOverlayText.setText("Day " + day);
        this.infoOverlay.setVisible(true);
        Thread.startVirtualThread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ErrorHandler.addVerboseException(e, "Failed properly show Day Info");
                Thread.currentThread().interrupt();
            }
            Platform.runLater(() -> this.infoOverlay.setVisible(false));
        });
    }

    private void addTileToGrid(Pair<TileModel, TileView> tile) {
        Position position = tile.getKey().getPosition();
        gameGrid.add(tile.getValue(), position.getX(), position.getY());
    }

    private void addOverlayButtons() {
        ObservableList<Node> overlayChildren = this.overlayBox.getChildren();
        UnitModel selectedUnit;
        try {
            selectedUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not add overlay buttons");
            return;
        }

        overlayChildren.remove(overlayMoveButton);
        overlayChildren.remove(overlayMergeButton);
        overlayChildren.remove(overlayAttackButton);
        overlayChildren.remove(overlayCloseButton);
        if (selectedUnit.canMerge()) {
            overlayChildren.add(overlayMergeButton);
            overlayChildren.add(overlayCloseButton);
        } else if (selectedUnit.canAttack() && selectedUnit.canMove()) {
            overlayChildren.add(overlayMoveButton);
            overlayChildren.add(overlayAttackButton);
            overlayChildren.add(overlayCloseButton);
        } else if (selectedUnit.canMove()) {
            overlayChildren.add(overlayMoveButton);
            overlayChildren.add(overlayCloseButton);
        } else if (selectedUnit.canAttack()) {
            overlayChildren.add(overlayAttackButton);
            overlayChildren.add(overlayCloseButton);
        } else {
            overlayChildren.add(overlayCloseButton);
        }
    }

    private void setStatusViewSide(TileModel selectedTile) {
        int tileX = selectedTile.getPosition().getX();
        int mapWidth = gameState.getMapWidthTiles();
        int leftSwapTrigger = (int) (mapWidth / 2.5);
        int rightSwapTrigger = (int) (mapWidth - (mapWidth / 2.5));
        double anchorPosition = 16;

        if (tileX < leftSwapTrigger) {
            AnchorPane.setRightAnchor(this.statusViewVBox, anchorPosition);
            AnchorPane.setLeftAnchor(this.statusViewVBox, null);
        } else if (tileX > rightSwapTrigger) {
            AnchorPane.setLeftAnchor(this.statusViewVBox, anchorPosition);
            AnchorPane.setRightAnchor(this.statusViewVBox, null);
        }
    }

    @FXML
    private void moveUnit(ActionEvent ignored) {
        UnitModel selectedUnit;
        try {
            selectedUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not move unit");
            return;
        }
        unitMovementController.moveUnit(selectedUnit, gameState.getSelectedTile());
        this.gameState.setActiveMode(ActiveMode.REGULAR_MODE);
    }

    @FXML
    private void closeOverlay(ActionEvent ignored) {
        this.gameState.setActiveMode(ActiveMode.REGULAR_MODE);
    }

    @FXML
    private void enterAttackMode(ActionEvent ignored) {
        LOGGER.info("Entering attack mode...");
        this.gameState.setActiveMode(ActiveMode.ATTACK_MODE);
    }

    @FXML
    private void mergeUnits(ActionEvent ignored) {
        UnitModel selectedUnit;
        try {
            selectedUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not merge units");
            return;
        }
        unitMovementController.mergeUnit(selectedUnit, gameState.getSelectedTile());
        this.gameState.setActiveMode(ActiveMode.REGULAR_MODE);
    }
}