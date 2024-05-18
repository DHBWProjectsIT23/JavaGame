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

    public static TileLoader getTileLoader() {
        return tileLoader;
    }

    public static UnitBuilder getUnitBuilder() {
        return unitBuilder;
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
