package org.itdhbw.futurewars.model.game;

import org.itdhbw.futurewars.controller.tile.TileBuilder;
import org.itdhbw.futurewars.controller.tile.TileCreationController;
import org.itdhbw.futurewars.controller.tile.TileEventController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.unit.UnitBuilder;
import org.itdhbw.futurewars.controller.unit.UnitCreationController;
import org.itdhbw.futurewars.controller.unit.UnitMovementController;
import org.itdhbw.futurewars.util.AStarPathfinder;

public class Context {
    private Context() {
        // private constructor to prevent instantiation
    }

    private static TileRepository tileRepository;
    private static TileBuilder tileBuilder;
    private static TileEventController tileEventController;
    private static UnitBuilder unitBuilder;
    private static TileCreationController tileCreationController;
    private static UnitMovementController unitMovementController;
    private static GameState gameState;
    private static AStarPathfinder pathfinder;
    private static UnitCreationController unitCreationController;

    public static void initialize() {
        tileRepository = new TileRepository();
        gameState = new GameState();
        pathfinder = new AStarPathfinder();
        unitBuilder = new UnitBuilder();
        unitMovementController = new UnitMovementController();
        unitCreationController = new UnitCreationController();
        tileEventController = new TileEventController();
        tileBuilder = new TileBuilder();
        tileCreationController = new TileCreationController();
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

}
