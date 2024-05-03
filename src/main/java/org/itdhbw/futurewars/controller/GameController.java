package org.itdhbw.futurewars.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.itdhbw.futurewars.service.SelectedTileService;

public class GameController {
    private final SelectedTileService selectedTileService;
    @FXML
    private Label testLabel;

    public GameController() {
        this.selectedTileService = SelectedTileService.getInstance();
    }

    public void initialize() {
        testLabel.textProperty().bind(selectedTileService.selectedTileMessageProperty());
    }
}
