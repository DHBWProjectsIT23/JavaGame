package org.itdhbw.futurewars.game.controllers.ui;

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
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.game.controllers.loaders.MapLoader;
import org.itdhbw.futurewars.game.controllers.tile.TileCreationController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.models.gameState.ActiveMode;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.utils.Position;
import org.itdhbw.futurewars.game.views.TileView;

/**
 * The type Map view controller.
 */
public class MapViewController {
    private static final Logger LOGGER =
            LogManager.getLogger(MapViewController.class);
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

    /**
     * Instantiates a new Map view controller.
     */
    public MapViewController() {
        this.tileCreationController = Context.getTileCreationController();
        this.unitMovementController = Context.getUnitMovementController();
        this.gameState = Context.getGameState();
        this.mapLoader = Context.getMapLoader();
    }


    /**
     * Initialize.
     */
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
        LOGGER.info("Pair: {} - Model: {} - View: {}", tile, tile.getKey(),
                    tile.getValue());
        LOGGER.info("Tile position: {}",
                    tile.getKey().getPosition().toString());
        Position position = tile.getKey().getPosition();
        gameGrid.add(tile.getValue(), position.getX(), position.getY());
    }

    /**
     * Load map.
     *
     * @param filename the filename
     */
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

        Pair<TileModel, TileView>[][] allTiles =
                Context.getTileRepository().getAllTiles();
        for (int x = 0; x < (gameState.getMapWidthTiles()); x++) {
            for (int y = 0; y < (gameState.getMapHeightTiles()); y++) {
                LOGGER.error("x: {} of {}, y: {} of {}", x,
                             gameState.getMapWidthTiles(), y,
                             gameState.getMapHeightTiles());
                Pair<TileModel, TileView> tilePair = allTiles[x][y];
                if (tilePair == null) {
                    LOGGER.warn("tilePair was null");
                    tilePair =
                            tileCreationController.createTile("NOT_SET_TILE", x,
                                                              y);
                }
                this.addTileToGrid(tilePair);
            }
        }


    }

    @FXML
    private void enterMoveMode(ActionEvent actionEvent) {
        unitMovementController.moveUnit(gameState.selectedUnitProperty().get(),
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