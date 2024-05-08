package org.itdhbw.futurewars.controller.loader;

import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.controller.unit.UnitFactoryCustom;
import org.itdhbw.futurewars.model.unit.CustomUnitModel;
import org.itdhbw.futurewars.model.unit.UnitType;
import org.itdhbw.futurewars.view.unit.CustomUnitView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Optional;

public class UnitLoader {
    private static final Logger LOGGER = LogManager.getLogger(UnitLoader.class);
    private static final String UNIT_VALIDATION = "FUTURE_WARS_UNIT_FORMAT";
    private final EnumMap<UnitType, UnitFactoryCustom> unitFactories;
    private String unitName;
    private int attackRange;
    private int movementRange;
    private int travelCostPlain;
    private int travelCostWood;
    private int travelCostMountain;
    private int travelCostSea;
    private UnitType unitType;
    private String texture1;
    private String texture2;
    private Optional<CustomUnitModel> unitModel = Optional.empty();
    private Optional<CustomUnitView> unitView = Optional.empty();

    public UnitLoader() {
        LOGGER.info("UnitLoader created");
        unitFactories = new EnumMap<>(UnitType.class);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("resources/testUnit/unit.csv"));
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

        //createUnitModel();
        //createUnitView();
    }

    private void createUnitFactory() {
        LOGGER.info("Creating unit factory");
        UnitFactoryCustom unitFactoryCustom = new UnitFactoryCustom(unitName, attackRange, movementRange, travelCostPlain, travelCostWood, travelCostMountain, travelCostSea, texture1, texture2, unitType);
        unitFactories.put(unitType, unitFactoryCustom);
    }

    public EnumMap<UnitType, UnitFactoryCustom> getUnitFactories() {
        LOGGER.info("Returning unit factories: {}", unitFactories);
        return unitFactories;
    }

    public void loadUnit(BufferedReader reader) throws IOException {
        // on second line - skip to third
        reader.readLine();
        unitName = reader.readLine();

        // on fourth line - skip to fifth
        reader.readLine();
        unitType = UnitType.valueOf(reader.readLine());

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
    }

    private void createUnitModel() {
        LOGGER.info("Creating unit model");
        unitModel = Optional.of(new CustomUnitModel(unitType, 1));
        unitModel.get().setAttackRange(attackRange);
        unitModel.get().setMovementRange(movementRange);
        unitModel.get().setPlainTileTravelCost(travelCostPlain);
        unitModel.get().setWoodTileTravelCost(travelCostWood);
        unitModel.get().setMountainTileTravelCost(travelCostMountain);
        unitModel.get().setSeaTileTravelCost(travelCostSea);
        unitModel.get().debugLog();
    }

    private void createUnitView() {
        LOGGER.info("Creating unit view");
        unitView = Optional.of(new CustomUnitView(unitModel.orElseThrow()));
        Image texture1Image = new Image("file:" + this.texture1);
        Image texture2Image = new Image("file:" + this.texture2);
        unitView.get().setTexture(texture1Image, texture2Image);
    }

    public Pair<CustomUnitModel, CustomUnitView> getUnit() {
        return new Pair<>(unitModel.orElseThrow(), unitView.orElseThrow());
    }
}
