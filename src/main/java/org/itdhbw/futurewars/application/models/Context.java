package org.itdhbw.futurewars.application.models;

import javafx.stage.Stage;
import org.itdhbw.futurewars.application.controllers.other.OptionsController;
import org.itdhbw.futurewars.game.controllers.loaders.MapLoader;
import org.itdhbw.futurewars.game.controllers.loaders.TileLoader;
import org.itdhbw.futurewars.game.controllers.loaders.UnitLoader;
import org.itdhbw.futurewars.game.controllers.tile.TileCreationController;
import org.itdhbw.futurewars.game.controllers.tile.TileEventController;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileBuilder;
import org.itdhbw.futurewars.game.controllers.ui.MapViewController;
import org.itdhbw.futurewars.game.controllers.unit.UnitAttackController;
import org.itdhbw.futurewars.game.controllers.unit.UnitCreationController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.controllers.unit.UnitRepository;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitBuilder;
import org.itdhbw.futurewars.game.models.gameState.GameState;
import org.itdhbw.futurewars.game.utils.AStarPathfinder;

/**
 * The type Context.
 */
public class Context {
    private static TileRepository tileRepository;
    private static UnitRepository unitRepository;
    private static TileLoader tileLoader;
    private static TileBuilder tileBuilder;
    private static UnitLoader unitLoader;
    private static UnitBuilder unitBuilder;
    private static TileEventController tileEventController;
    private static TileCreationController tileCreationController;
    private static UnitMovementController unitMovementController;
    private static GameState gameState;
    private static AStarPathfinder pathfinder;
    private static UnitCreationController unitCreationController;
    private static MapLoader mapLoader;
    private static MapViewController gameController = null;
    private static UnitAttackController unitAttackController;
    private static OptionsController optionsController;
    private static Stage primaryStage;

    private Context() {
        // private constructor to prevent instantiation
    }

    /**
     * Gets tile loader.
     *
     * @return the tile loader
     */
    public static TileLoader getTileLoader() {
        return tileLoader;
    }

    /**
     * Gets unit builder.
     *
     * @return the unit builder
     */
    public static UnitBuilder getUnitBuilder() {
        return unitBuilder;
    }

    /**
     * Gets options controller.
     *
     * @return the options controller
     */
    public static OptionsController getOptionsController() {
        return optionsController;
    }

    /**
     * Gets unit loader.
     *
     * @return the unit loader
     */
    public static UnitLoader getUnitLoader() {
        return unitLoader;
    }

    /**
     * Gets map controller.
     *
     * @return the map controller
     */
    public static MapViewController getMapController() {
        if (gameController == null) {
            throw new IllegalStateException("MapController not set");
        }
        return gameController;
    }

    /**
     * Sets map controller.
     *
     * @param gameController the game controller
     */
    public static void setMapController(MapViewController gameController) {
        Context.gameController = gameController;
    }

    /**
     * Initialize.
     */
    public static void initialize() {
        optionsController = new OptionsController();
        tileRepository = new TileRepository();
        unitRepository = new UnitRepository();
        unitLoader = new UnitLoader();
        unitBuilder = new UnitBuilder();
        tileLoader = new TileLoader();
        gameState = new GameState();
        unitAttackController = new UnitAttackController();
        pathfinder = new AStarPathfinder();
        unitMovementController = new UnitMovementController();
        unitCreationController = new UnitCreationController();
        tileEventController = new TileEventController();
        tileBuilder = new TileBuilder();
        tileCreationController = new TileCreationController();
        mapLoader = new MapLoader();
        tileEventController.initialize();
        unitMovementController.initialize();
    }

    /**
     * Gets unit creation controller.
     *
     * @return the unit creation controller
     */
    public static UnitCreationController getUnitCreationController() {
        return unitCreationController;
    }

    /**
     * Gets pathfinder.
     *
     * @return the pathfinder
     */
    public static AStarPathfinder getPathfinder() {
        return pathfinder;
    }

    /**
     * Gets tile repository.
     *
     * @return the tile repository
     */
    public static TileRepository getTileRepository() {
        return tileRepository;
    }

    /**
     * Gets tile builder.
     *
     * @return the tile builder
     */
    public static TileBuilder getTileBuilder() {
        return tileBuilder;
    }

    /**
     * Gets tile event controller.
     *
     * @return the tile event controller
     */
    public static TileEventController getTileEventController() {
        return tileEventController;
    }

    /**
     * Gets tile creation controller.
     *
     * @return the tile creation controller
     */
    public static TileCreationController getTileCreationController() {
        return tileCreationController;
    }

    /**
     * Gets unit movement controller.
     *
     * @return the unit movement controller
     */
    public static UnitMovementController getUnitMovementController() {
        return unitMovementController;
    }

    /**
     * Gets game state.
     *
     * @return the game state
     */
    public static GameState getGameState() {
        return gameState;
    }

    /**
     * Gets map loader.
     *
     * @return the map loader
     */
    public static MapLoader getMapLoader() {
        return mapLoader;
    }

    /**
     * Gets unit repository.
     *
     * @return the unit repository
     */
    public static UnitRepository getUnitRepository() {
        return unitRepository;
    }

    /**
     * Gets unit attack controller.
     *
     * @return the unit attack controller
     */
    public static UnitAttackController getUnitAttackController() {
        return unitAttackController;
    }

    /**
     * Gets primary stage.
     *
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets primary stage.
     *
     * @param primaryStage the primary stage
     */
    public static void setPrimaryStage(Stage primaryStage) {
        Context.primaryStage = primaryStage;
    }
}
