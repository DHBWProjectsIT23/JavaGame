package org.itdhbw.futurewars.model.game;

import javafx.stage.Stage;
import org.itdhbw.futurewars.controller.OptionsController;
import org.itdhbw.futurewars.controller.loader.MapLoader;
import org.itdhbw.futurewars.controller.loader.UnitLoader;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.tile.TileEventController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.tile.factory.TileBuilder;
import org.itdhbw.futurewars.controller.ui.MapViewController;
import org.itdhbw.futurewars.controller.unit.UnitAttackController;
import org.itdhbw.futurewars.controller.unit.UnitCreationController;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.controller.unit.UnitRepository;
import org.itdhbw.futurewars.util.AStarPathfinder;

public class Context {
    private static TileRepository tileRepository;
    private static UnitRepository unitRepository;
    private static TileBuilder tileBuilder;
    private static TileEventController tileEventController;
    private static TileCreationController tileCreationController;
    private static UnitMovementController unitMovementController;
    private static GameState gameState;
    private static AStarPathfinder pathfinder;
    private static UnitCreationController unitCreationController;
    private static MapLoader mapLoader;
    private static MapViewController gameController = null;
    private static UnitLoader unitLoader;
    private static UnitAttackController unitAttackController;
    private static OptionsController optionsController;
    private static Stage primaryStage;

    private Context() {
        // private constructor to prevent instantiation
    }

    public static OptionsController getOptionsController() {
        return optionsController;
    }

    public static UnitLoader getUnitLoader() {
        return unitLoader;
    }

    public static MapViewController getMapController() {
        if (gameController == null) {
            throw new IllegalStateException("MapController not set");
        }
        return gameController;
    }

    public static void setMapController(MapViewController gameController) {
        Context.gameController = gameController;
    }

    public static void initialize() {
        optionsController = new OptionsController();
        tileRepository = new TileRepository();
        unitRepository = new UnitRepository();
        unitLoader = new UnitLoader();
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

    public static UnitCreationController getUnitCreationController() {
        return unitCreationController;
    }

    public static AStarPathfinder getPathfinder() {
        return pathfinder;
    }

    public static TileRepository getTileRepository() {
        return tileRepository;
    }

    public static TileBuilder getTileBuilder() {
        return tileBuilder;
    }

    public static TileEventController getTileEventController() {
        return tileEventController;
    }

    public static TileCreationController getTileCreationController() {
        return tileCreationController;
    }

    public static UnitMovementController getUnitMovementController() {
        return unitMovementController;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static MapLoader getMapLoader() {
        return mapLoader;
    }

    public static UnitRepository getUnitRepository() {
        return unitRepository;
    }

    public static UnitAttackController getUnitAttackController() {
        return unitAttackController;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Context.primaryStage = primaryStage;
    }
}
