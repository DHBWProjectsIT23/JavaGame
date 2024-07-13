package org.itdhbw.futurewars.game.controllers.tile.mouse_events;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.exceptions.NoUnitSelectedException;
import org.itdhbw.futurewars.game.controllers.unit.UnitAttackController;
import org.itdhbw.futurewars.game.controllers.unit.UnitMovementController;
import org.itdhbw.futurewars.game.models.game_state.ActiveMode;
import org.itdhbw.futurewars.game.models.game_state.GameState;
import org.itdhbw.futurewars.game.models.tile.TileModel;
import org.itdhbw.futurewars.game.models.unit.UnitModel;
import org.itdhbw.futurewars.game.views.TileView;

import java.util.logging.Logger;

public class AttackModeHandler implements MouseEventHandler {
    private static final Logger LOGGER = Logger.getLogger(AttackModeHandler.class.getName());
    private final GameState gameState;
    private final UnitMovementController unitMovementController;
    private final UnitAttackController unitAttackController;

    public AttackModeHandler(GameState gameState, UnitMovementController unitMovementController, UnitAttackController unitAttackController) {
        this.gameState = gameState;
        this.unitMovementController = unitMovementController;
        this.unitAttackController = unitAttackController;
    }

    @Override
    public void handleMouseEnter(MouseEvent event, TileView tileView) {
        UnitModel selectedUnit;
        try {
            selectedUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not attack");
            return;
        }

        hideOldDamagePrediction();
        hideDamageTextOnSelectedUnit(selectedUnit);
        gameState.unhoverTile();

        if (!tileView.getTileModel().isOccupied()) {
            return;
        }
        UnitModel hoveredUnit = tileView.getTileModel().getOccupyingUnit();


        if (hoveredUnit.getTeam() == gameState.getCurrentPlayer()) {
            LOGGER.info("Unit Team: " + selectedUnit.getTeam() + " Current Player: " + gameState.getCurrentPlayer());
            return;
        }

        if (gameState.getSelectedTile().getPosition().calculateDistance(tileView.getTileModel().getPosition()) >
            selectedUnit.getAttackRange()) {
            return;
        }


        if (selectedUnit.canAttackUnit(tileView.getTileModel().getOccupyingUnit())) {
            gameState.hoverTile(tileView.getTileModel());
            int predictedDamageEnemy = UnitAttackController.calculateDamagePoints(selectedUnit, hoveredUnit);
            tileView.showDamageText(predictedDamageEnemy);
            int predictedDamageToSelf;
            if (hoveredUnit.getVulnerableTypes().contains(selectedUnit.getTargetType())) {
                predictedDamageToSelf = UnitAttackController.calculatePreviewDamage(hoveredUnit, selectedUnit);
            } else {
                predictedDamageToSelf = 0;
            }
            Context.getTileRepository().getTileView(selectedUnit.getPosition()).showDamageText(predictedDamageToSelf);
        }
    }

    @Override
    public void handleMouseClick(MouseEvent event, TileView tileView) {
        if (event.getButton() == MouseButton.SECONDARY ||
            !Context.getTileEventController().isPartOfAttackableTiles(tileView)) {
            gameState.deselectTile();
            gameState.setActiveMode(ActiveMode.REGULAR_MODE);
            return;
        }
        UnitModel selectedUnit;
        try {
            selectedUnit = gameState.getSelectedUnit();
        } catch (NoUnitSelectedException e) {
            ErrorHandler.addVerboseException(e, "No unit selected, could not attack");
            return;
        }
        hideOldDamagePrediction();
        hideDamageTextOnSelectedUnit(selectedUnit);

        unitMovementController.moveUnit(selectedUnit, gameState.getSelectedTile());

        unitAttackController.attack(tileView.getTileModel().getOccupyingUnit());
        gameState.setActiveMode(ActiveMode.REGULAR_MODE);
    }

    private void hideOldDamagePrediction() {
        TileModel hoveredTile = gameState.getHoveredTile();
        if (hoveredTile == null) {
            return;
        }
        TileView hoveredTileView = Context.getTileRepository().getTileView(hoveredTile);
        hoveredTileView.hideDamageText();
    }

    private static void hideDamageTextOnSelectedUnit(UnitModel selectedUnit) {
        Context.getTileRepository().getTileView(selectedUnit.getPosition()).hideDamageText();
    }
}

