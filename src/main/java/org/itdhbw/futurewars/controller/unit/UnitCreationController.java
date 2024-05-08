package org.itdhbw.futurewars.controller.unit;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.tile.TileRepository;
import org.itdhbw.futurewars.controller.unit.factory.UnitBuilder;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.tile.TileModel;
import org.itdhbw.futurewars.model.unit.UnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.util.Position;
import org.itdhbw.futurewars.view.unit.UnitView;

public class UnitCreationController {
    private static final Logger LOGGER = LogManager.getLogger(UnitCreationController.class);
    private final UnitBuilder unitBuilder;
    private final TileRepository tileRepository;
    private final UnitBuilderCustom unitBuilderCustom;

    public UnitCreationController() {
        this.unitBuilder = Context.getUnitBuilder();
        this.tileRepository = Context.getTileRepository();
        this.unitBuilderCustom = new UnitBuilderCustom();
    }

    private UnitModel constructUnit(UnitType unitType, int team) {
        LOGGER.info("Creating unit of type {} for team {}", unitType, team);
        Pair<UnitModel, UnitView> unitPair = unitBuilder.createUnit(unitType, team);
        return unitPair.getKey();
    }

    public void createUnit(UnitType unitType, TileModel initialTile, int team) {
        LOGGER.info("Spawning unit at tile {}", initialTile.modelId);
        constructUnit(unitType, team).spawn(initialTile);
    }

    public void createUnit(UnitType unitType, Position position, int team) {
        LOGGER.info("Spawning unit at position {}", position);
        constructUnit(unitType, team).spawn(tileRepository.getTileModel(position));
    }

    public void createUnit(UnitType unitType, int x, int y, int team) {
        LOGGER.info("Spawning unit at position ({}, {})", x, y);
        constructUnit(unitType, team).spawn(tileRepository.getTileModel(new Position(x, y)));
    }

    public void createCustomUnit(String unitType, int x, int y) {
        LOGGER.info("Spawning custom unit at position ({}, {})", x, y);
        Pair<UnitModel, UnitView> unitPair = unitBuilderCustom.createUnit(unitType);
        unitPair.getKey().spawn(tileRepository.getTileModel(new Position(x, y)));
    }

}
