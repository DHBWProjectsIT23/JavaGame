package org.itdhbw.futurewars.application.models;

import javafx.stage.Stage;
import org.itdhbw.futurewars.application.controllers.other.OptionsController;
import org.itdhbw.futurewars.game.controllers.MapRepository;
import org.itdhbw.futurewars.game.controllers.loaders.FileLoader;
import org.itdhbw.futurewars.game.controllers.tile.TileEventController;
import org.itdhbw.futurewars.game.controllers.tile.TileRepository;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileBuilder;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileCreationController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.controllers.unit.UnitRepository;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitBuilder;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.utils.Pathfinder;

public class Context {
    private static TileRepository tileRepository;
    private static UnitRepository unitRepository;
    private static MapRepository mapRepository;
    private static TileBuilder tileBuilder;
    private static UnitBuilder unitBuilder;
    private static TileEventController tileEventController;
    private static TileCreationController tileCreationController;
    private static UnitMovementController unitMovementController;
    private static GameState gameState;
    private static Pathfinder pathfinder;
    private static OptionsController optionsController;
    private static FileLoader fileLoader;
    private static Stage primaryStage;

    private Context() {
        // private constructor to prevent instantiation
    }

    public static FileLoader getFileLoader() {
        return fileLoader;
    }

    public static UnitBuilder getUnitBuilder() {
        return unitBuilder;
    }

    public static OptionsController getOptionsController() {
        return optionsController;
    }

    public static void initialize() {
        optionsController = new OptionsController();
        mapRepository = new MapRepository();
        tileRepository = new TileRepository();
        unitRepository = new UnitRepository();
        unitBuilder = new UnitBuilder();
        gameState = new GameState();
        pathfinder = new Pathfinder();
        unitMovementController = new UnitMovementController();
        tileEventController = new TileEventController();
        tileBuilder = new TileBuilder();
        tileCreationController = new TileCreationController();
        fileLoader = new FileLoader();
        tileEventController.initialize();
        unitMovementController.initialize();
    }

    public static MapRepository getMapRepository() {
        return mapRepository;
    }

    public static Pathfinder getPathfinder() {
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

    public static UnitRepository getUnitRepository() {
        return unitRepository;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Context.primaryStage = primaryStage;
    }
}
