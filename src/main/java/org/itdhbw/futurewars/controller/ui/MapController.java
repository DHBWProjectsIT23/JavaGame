package org.itdhbw.futurewars.controller.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.loader.MapLoader;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.model.game.ActiveMode;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.game.GameState;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.tile.TileType;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.tile.TileView;

public class MapController {
    private static final Logger LOGGER = LogManager.getLogger(MapController.class);
    private final TileCreationController tileCreationController;
    private final UnitMovementController unitMovementController;
    private final GameState gameState;
    private final MapLoader mapLoader;
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
    private Button overlayAttackButton1;

    public MapController() {
        this.tileCreationController = Context.getTileCreationController();
        this.unitMovementController = Context.getUnitMovementController();
        this.gameState = Context.getGameState();
        this.mapLoader = Context.getMapLoader();
    }


    public void initialize() {

        Context.setMapController(this);
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
            mouseX.set(event.getSceneX());
            mouseY.set(event.getSceneY());
        });
    }

    private void hideOverlay() {
        this.overlayPane.setVisible(false);
        this.overlayPane.setDisable(true);
    }

    private void showOverlay() {
        LOGGER.info("Showing overlay...");
        LOGGER.info("Mouse X: {} - Mouse Y: {}", mouseX.get(), mouseY.get());
        this.overlayBox.setLayoutX(mouseX.get());
        this.overlayBox.setLayoutY(mouseY.get());
        this.overlayPane.setVisible(true);
        this.overlayPane.setDisable(false);
    }

    private void addTileToGrid(Pair<TileModel, TileView> tile) {
        LOGGER.info("Pair: {} - Model: {} - View: {}", tile, tile.getKey(), tile.getValue());
        LOGGER.info("Tile position: {}", tile.getKey().getPosition().toString());
        Position position = tile.getKey().getPosition();
        gameGrid.add(tile.getValue(), position.getX(), position.getY());
    }

    public void loadMap(String filename) {
        this.gameGrid.getChildren().clear();
        try {
            mapLoader.loadMap(filename);
            this.addTilesToGrid();
        } catch (Exception e) {
            LOGGER.error("Error loading map: {}", e.getMessage());
        }

    }

    private void addTilesToGrid() {
        LOGGER.info("Loading map...");

        Pair<TileModel, TileView>[][] allTiles = Context.getTileRepository().getAllTiles();
        for (int x = 0; x < (gameState.getMapWidth()); x++) {
            for (int y = 0; y < (gameState.getMapHeight()); y++) {
                LOGGER.error("x: {} of {}, y: {} of {}", x, gameState.getMapWidth(), y, gameState.getMapHeight());
                Pair<TileModel, TileView> tilePair = allTiles[x][y];
                if (tilePair == null) {
                    LOGGER.warn("tilePair was null");
                    tilePair = tileCreationController.createTile(TileType.TILE_NOT_SET, x, y);
                }
                this.addTileToGrid(tilePair);
            }
        }


    }

    @FXML
    private void enterMoveMode(ActionEvent actionEvent) {
        unitMovementController.moveUnit(gameState.selectedUnitProperty().get(), gameState.selectedTileProperty().get());
        this.gameState.setActiveMode(ActiveMode.REGULAR);
        //this.gameState.setActiveMode(ActiveMode.MOVING_UNIT);
    }

    @FXML
    private void closeOverlay(ActionEvent actionEvent) {
        this.gameState.setActiveMode(ActiveMode.REGULAR);
        //LOGGER.info("Spawning custom unit");
        //for (String unitType : Context.getUnitRepository().getUnitTypes()) {
        //    LOGGER.info("Unit type: {}", unitType);
        //}
        //Context.getUnitCreationController().createCustomUnit("CUSTOM_UNIT_1", 0, 0);
    }

    @FXML
    private void enterAttackMode(ActionEvent actionEvent) {
        LOGGER.info("Entering attack mode...");
        this.gameState.setActiveMode(ActiveMode.ATTACKING_UNIT);
    }
}