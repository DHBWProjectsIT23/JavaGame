package org.itdhbw.futurewars.model.game;

import org.itdhbw.futurewars.controller.tile.TileBuilder;
import org.itdhbw.futurewars.controller.tile.TileController;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.unit.UnitBuilder;
import org.itdhbw.futurewars.controller.unit.UnitController;
import org.itdhbw.futurewars.util.AStarPathfinder;

public class Context {
    private static TileRepository tileRepository;
    private static TileBuilder tileBuilder;
    private static TileController tileController;
    private static UnitBuilder unitBuilder;
    private static UnitController unitController;
    private static GameState gameState;
    private static AStarPathfinder pathfinder;

    public static void initialize() {
        tileRepository = new TileRepository();
        gameState = new GameState();
        pathfinder = new AStarPathfinder();
        unitController = new UnitController();
        unitBuilder = new UnitBuilder();
        tileController = new TileController();
        tileBuilder = new TileBuilder();
        tileController.initialize();
        unitController.initialize();
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

    public static TileController getTileController() {
        return tileController;
    }

    public static UnitBuilder getUnitBuilder() {
        return unitBuilder;
    }

    public static UnitController getUnitController() {
        return unitController;
    }

    public static GameState getGameState() {
        return gameState;
    }

}
