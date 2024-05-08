package org.itdhbw.futurewars.controller.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.unit.UnitFactoryCustom;
import org.itdhbw.futurewars.controller.unit.UnitRepository;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.model.unit.CustomUnitModel;
import org.itdhbw.futurewars.view.unit.CustomUnitView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class UnitLoader {
    private static final Logger LOGGER = LogManager.getLogger(UnitLoader.class);
    private static final String UNIT_VALIDATION = "FUTURE_WARS_UNIT_FORMAT";
    private final Map unitFactories;
    private final UnitRepository unitRepository;
    private final Optional<CustomUnitModel> unitModel = Optional.empty();
    private final Optional<CustomUnitView> unitView = Optional.empty();
    private String unitType;
    private int attackRange;
    private int movementRange;
    private int travelCostPlain;
    private int travelCostWood;
    private int travelCostMountain;
    private int travelCostSea;
    private String texture1;
    private String texture2;

    public UnitLoader() {
        LOGGER.info("UnitLoader created");
        unitFactories = new HashMap<>();
        unitRepository = Context.getUnitRepository();

        Path dir = Paths.get("resources/testUnit");
        List<String> files = new ArrayList<>();

        try {
            Files.walk(dir)
                    .filter(Files::isRegularFile)
                    .forEach(file -> files.add(file.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (String file : files) {

            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file));
                String[] validation = reader.readLine().split(",");
                LOGGER.info("Validation: {}", validation[0]);
                if (validation[0].equals(UNIT_VALIDATION)) {
                    loadUnit(reader);
                } else {
                    throw new IllegalArgumentException("Given file is not a unit file");
                }

            } catch (
                      IOException e) {
                e.printStackTrace();
            }

            createUnitFactory();
        }

        //createUnitModel();
        //createUnitView();
    }

    private void createUnitFactory() {
        LOGGER.info("Creating unit factory");
        UnitFactoryCustom unitFactoryCustom = new UnitFactoryCustom(unitType, attackRange, movementRange, travelCostPlain, travelCostWood, travelCostMountain, travelCostSea, texture1, texture2);
        unitFactories.put(unitType, unitFactoryCustom);
    }

    public Map<String, UnitFactoryCustom> getUnitFactories() {
        LOGGER.info("Returning unit factories: {}", unitFactories);
        return unitFactories;
    }

    public void loadUnit(BufferedReader reader) throws IOException {
        // on second line - skip to third
        reader.readLine();
        unitType = reader.readLine();

        // on sixth line - skip to seventh
        reader.readLine();
        String[] thirdLine = reader.readLine().split(",");
        attackRange = Integer.parseInt(thirdLine[0]);
        movementRange = Integer.parseInt(thirdLine[1]);

        // on fourth line - skip to fifth
        reader.readLine();

        String[] fifthLine = reader.readLine().split(",");
        travelCostPlain = Integer.parseInt(fifthLine[0]);
        travelCostWood = Integer.parseInt(fifthLine[1]);
        travelCostSea = Integer.parseInt(fifthLine[2]);
        travelCostMountain = Integer.parseInt(fifthLine[3]);

        // on sixth line - skip to seventh
        reader.readLine();
        texture1 = reader.readLine();
        texture2 = reader.readLine();

        unitRepository.addUnitType(unitType);
    }
}
