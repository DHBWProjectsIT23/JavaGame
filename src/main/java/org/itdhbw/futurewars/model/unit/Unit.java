package org.itdhbw.futurewars.model.unit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.tile.Tile;

public abstract class Unit {
    private static final Logger LOGGER = LogManager.getLogger(Unit.class);
    protected final ImageView textureLayer;
    protected final int team;
    private final UnitType unitType;
    private final ObjectProperty<Tile> currentTile = new SimpleObjectProperty<>();

    protected Unit(final Tile initialTile, final double tileSize, UnitType unitType, final int team) {
        LOGGER.info("Creating unit at {}...", initialTile.getId());

        this.team = team;
        this.unitType = unitType;

        this.textureLayer = new ImageView();
        this.textureLayer.setFitWidth(tileSize);
        this.textureLayer.setFitHeight(tileSize);
        setTexture();


        currentTile.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.removeOccupyingUnit();
            }
            if (newValue != null) {
                newValue.setOccupyingUnit(this);
            }
        });
        this.currentTile.set(initialTile);

    }

    public void moveTo(Tile newTile) {
        if (newTile.isOccupied()) {
            LOGGER.error("Tile {} is already occupied!", newTile.getId());
            return;
        }
        this.currentTile.set(newTile);
    }

    public ImageView getTextureLayer() {
        return textureLayer;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    protected abstract void setTexture();

}
