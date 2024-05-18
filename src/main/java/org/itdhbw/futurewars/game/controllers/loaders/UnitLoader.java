package org.itdhbw.futurewars.game.controllers.loaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorPopup;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.CustomException;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.game.controllers.unit.UnitRepository;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Unit loader.
 */
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

    /**
     * Instantiates a new Unit loader.
     */
    public UnitLoader() {
        unitRepository = Context.getUnitRepository();
        unitFactories = new HashMap<>();
        unitFiles = new HashMap<>();
    }

    /**
     * Number of unit files int.
     *
     * @return the int
     */
    public int numberOfUnitFiles() {
        return unitFiles.size();
    }

    /**
     * Retrieve system units.
     *
     * @throws FailedToRetrieveFilesException the failed to retrieve files exception
     * @throws FailedToLoadFileException      the failed to load file exception
     */
    public void retrieveSystemUnits() throws FailedToRetrieveFilesException,
                                             FailedToLoadFileException {
        LOGGER.info("Retrieving system units");
        unitFiles.putAll(
                FileHelper.retrieveFiles(FileHelper::getInternalUnitPath));
        LOGGER.info("Retrieved system units - total of {} units",
                    unitFiles.size());
    }

    /**
     * Retrieve user units.
     *
     * @throws FailedToLoadFileException      the failed to load file exception
     * @throws FailedToRetrieveFilesException the failed to retrieve files exception
     */
    public void retrieveUserUnits() throws FailedToLoadFileException,
                                           FailedToRetrieveFilesException {
        LOGGER.info("Retrieving user units");
        unitFiles.putAll(FileHelper.retrieveFiles(FileHelper::getUserUnitPath));
        LOGGER.info("Retrieved user units - total of {} units",
                    unitFiles.size());
    }

    /**
     * Load units from files.
     */
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
                    throw new IllegalArgumentException(
                            "Given file is not a unit file");
                }

            } catch (IOException e) {
                LOGGER.error("Error loading unit from file: {}", file);
            }

            createUnitFactory();
        }
    }

    private void createUnitFactory() {
        LOGGER.info("Creating unit factory");
        UnitFactory unitFactoryCustom =
                new UnitFactory(unitType, attackRange, movementRange,
                                travelCostPlain, travelCostWood,
                                travelCostMountain, travelCostSea, texture1,
                                texture2);
        unitFactories.put(unitType, unitFactoryCustom);
    }

    /**
     * Gets unit factories.
     *
     * @return the unit factories
     */
    public Map<String, UnitFactory> getUnitFactories() {
        LOGGER.info("Returning unit factories: {}", unitFactories);
        return unitFactories;
    }

    /**
     * Load unit.
     *
     * @param reader the reader
     * @throws IOException the io exception
     */
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
        } catch (CustomException e) {
            LOGGER.error("Error loading texture: {}", e.getMessage());
            ErrorPopup.showRecoverableErrorPopup(
                    "Error loading texture for unit " + unitType, e);
        }

        unitRepository.addUnitType(unitType);
    }
}
