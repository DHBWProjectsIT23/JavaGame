package org.itdhbw.futurewars.game.controllers.loaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.application.utils.FileHelper;
import org.itdhbw.futurewars.exceptions.CustomException;
import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;
import org.itdhbw.futurewars.game.controllers.unit.UnitRepository;
import org.itdhbw.futurewars.game.controllers.unit.factory.UnitFactory;
import org.itdhbw.futurewars.game.models.unit.TargetType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitLoader implements LoaderFactory {
    private static final Logger LOGGER = LogManager.getLogger(UnitLoader.class);
    private final Map<String, UnitFactory> unitFactories;
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
    private int baseDamage;
    private int armor;
    private int piercing;
    private int lowAirPiercing;
    private TargetType targetType;
    private final List<TargetType> canAttackType;

    public UnitLoader() {
        unitRepository = Context.getUnitRepository();
        unitFactories = new HashMap<>();
        canAttackType = new ArrayList<>();
    }

    public Map<String, File> getSystemFiles() throws
                                              FailedToRetrieveFilesException {
        return FileHelper.retrieveFiles(FileHelper::getInternalUnitPath);
    }

    public Map<String, File> getUserFiles() throws
                                            FailedToRetrieveFilesException {
        return FileHelper.retrieveFiles(FileHelper::getUserUnitPath);
    }

    private void createUnitFactory() {
        LOGGER.info("Creating unit factory");
        UnitFactory unitFactoryCustom =
                new UnitFactory(unitType, attackRange, movementRange,
                                travelCostPlain, travelCostWood,
                                travelCostMountain, travelCostSea, texture1,
                                texture2, baseDamage, armor, piercing, lowAirPiercing, targetType, canAttackType);
        Context.getUnitBuilder().addUnitFactory(unitType, unitFactoryCustom);
    }

    public Map<String, UnitFactory> getUnitFactories() {
        LOGGER.info("Returning unit factories: {}", unitFactories);
        return unitFactories;
    }

    public void loadFile(BufferedReader reader, File file) throws
                                                           FailedToLoadFileException {
        try {
            // on second line - skip to third
            reader.readLine();
            unitType = reader.readLine();

            // on fourth line - skip to fifth
            reader.readLine();

            String[] fifthLine = reader.readLine().split(",");
            attackRange = Integer.parseInt(fifthLine[0]);
            movementRange = Integer.parseInt(fifthLine[1]);

            // on sixth line - skip to seventh
            reader.readLine();

            String[] seventhLine = reader.readLine().split(",");
            travelCostPlain = Integer.parseInt(seventhLine[0]);
            travelCostWood = Integer.parseInt(seventhLine[1]);
            travelCostSea = Integer.parseInt(seventhLine[2]);
            travelCostMountain = Integer.parseInt(seventhLine[3]);

            // on eighth line - skip to ninth
            reader.readLine();
            try {
                texture1 = FileHelper.getFile(reader.readLine());
                texture2 = FileHelper.getFile(reader.readLine());
            } catch (CustomException e) {
                ErrorHandler.addException(e, "failed to load unit textures");
            }

            // on eleventh line - skip to twelfth
            reader.readLine();

            String[] twelfthLine = reader.readLine().split(",");
            baseDamage = Integer.parseInt(twelfthLine[0]);
            armor = Integer.parseInt(twelfthLine[1]);
            piercing = Integer.parseInt(twelfthLine[2]);
            lowAirPiercing = Integer.parseInt(twelfthLine[3]);

            // on thirteenth line - skip to fourteenth
            reader.readLine();
            try {
                targetType = TargetType.valueOf(reader.readLine());
            } catch (IllegalArgumentException e) {
                ErrorHandler.addException(e, "failed to load target type");
            }

            // on fifteenth line - skip to sixteenth
            reader.readLine();
            String[] sixteenthLine = reader.readLine().split(",");
            for (String type : sixteenthLine) {
                canAttackType.add(TargetType.valueOf(type));
            }

            unitRepository.addUnitType(unitType);
        } catch (IOException e) {
            throw new FailedToLoadFileException("Failed to load unit file");
        }

        createUnitFactory();
    }
}
