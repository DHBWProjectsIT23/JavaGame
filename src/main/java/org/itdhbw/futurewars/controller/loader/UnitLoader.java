package org.itdhbw.futurewars.controller.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.unit.UnitRepository;
import org.itdhbw.futurewars.controller.unit.factory.UnitFactory;
import org.itdhbw.futurewars.model.game.Context;
import org.itdhbw.futurewars.util.ErrorPopup;
import org.itdhbw.futurewars.util.FileHelper;
import org.itdhbw.futurewars.util.exceptions.CanNotLoadException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class UnitLoader {
    private static final Logger LOGGER = LogManager.getLogger(UnitLoader.class);
    private static final String UNIT_VALIDATION = "FUTURE_WARS_UNIT_FORMAT";
    private final Map<String, UnitFactory> unitFactories;
    private final Map<String, File> unitFiles;
    private final UnitRepository unitRepository;
    private String unitType;
    private int attackRange;
    private int movementRange;
    private int travelCostPlain;
    private int travelCostWood;
    private int travelCostMountain;
    private int travelCostSea;
    private URI texture1;
    private URI texture2;

    public UnitLoader() {
        unitRepository = Context.getUnitRepository();
        unitFactories = new HashMap<>();
        unitFiles = new HashMap<>();
    }

    public int numberOfUnitFiles() {
        return unitFiles.size();
    }

    public void retrieveSystemUnits() throws CanNotLoadException {
        LOGGER.info("Retrieving system units");
        unitFiles.putAll(FileHelper.retrieveFiles(FileHelper::getInternalUnitPath));
        LOGGER.info("Retrieved system units - total of {} units", unitFiles.size());
    }

    public void retrieveUserUnits() throws TextureNotLoaded {
        LOGGER.info("Retrieving user units");
        unitFiles.putAll(FileHelper.retrieveFiles(FileHelper::getUserUnitPath));
        LOGGER.info("Retrieved user units - total of {} units", unitFiles.size());
    }

    public void loadUnitsFromFiles() {
        for (File file : unitFiles.values()) {

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

            } catch (IOException e) {
                LOGGER.error("Error loading unit from file: {}", file);
            }

            createUnitFactory();
        }
    }

    private void createUnitFactory() {
        LOGGER.info("Creating unit factory");
        UnitFactory unitFactoryCustom = new UnitFactory(unitType, attackRange, movementRange, travelCostPlain, travelCostWood, travelCostMountain, travelCostSea, texture1, texture2);
        unitFactories.put(unitType, unitFactoryCustom);
    }

    public Map<String, UnitFactory> getUnitFactories() {
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
        try {
            texture1 = FileHelper.getFile(reader.readLine());
            texture2 = FileHelper.getFile(reader.readLine());
        } catch (TextureNotLoaded e) {
            LOGGER.error("Error loading texture: {}", e.getMessage());
            ErrorPopup.showRecoverableErrorPopup("Error loading texture for unit " + unitType, e);
        }

        unitRepository.addUnitType(unitType);
    }
}
