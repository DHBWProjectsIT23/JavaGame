package org.itdhbw.futurewars.model.game;

import org.itdhbw.futurewars.controller.loader.MapLoader;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.tile.TileEventController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.tile.factory.TileBuilder;
import org.itdhbw.futurewars.controller.ui.MapController;
import org.itdhbw.futurewars.controller.unit.UnitCreationController;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.controller.unit.UnitRepository;
import org.itdhbw.futurewars.controller.unit.factory.UnitBuilder;
import org.itdhbw.futurewars.util.AStarPathfinder;

public class Context {
    private static TileRepository tileRepository;
    private static UnitRepository unitRepository;
    private static TileBuilder tileBuilder;
    private static TileEventController tileEventController;
    private static UnitBuilder unitBuilder;
    private static TileCreationController tileCreationController;
    private static UnitMovementController unitMovementController;
    private static GameState gameState;
    private static AStarPathfinder pathfinder;
    private static UnitCreationController unitCreationController;
    private static MapLoader mapLoader;
    private static MapController gameController = null;

    private Context() {
        // private constructor to prevent instantiation
    }

    public static MapController getMapController() {
        if (gameController == null) {
            throw new IllegalStateException("MapController not set");
        }
        return gameController;
    }

    public static void setMapController(MapController gameController) {
        if (Context.gameController != null) {
            throw new IllegalStateException("MapController already set");
        }
        Context.gameController = gameController;
    }

    public static void initialize() {
        tileRepository = new TileRepository();
        unitRepository = new UnitRepository();
        gameState = new GameState();
        pathfinder = new AStarPathfinder();
        unitBuilder = new UnitBuilder();
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

    public static UnitBuilder getUnitBuilder() {
        return unitBuilder;
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

}
