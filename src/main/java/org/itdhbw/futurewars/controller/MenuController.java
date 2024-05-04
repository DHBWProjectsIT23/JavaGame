package org.itdhbw.futurewars.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuController {

    private MenuController() {
        // private constructor to hide the implicit public one
    }
    @FXML
    private Button startButton;

    @FXML
    private void startGame(ActionEvent actionEvent) {
        try {
            Parent gameView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/itdhbw/futurewars/game-view.fxml")));
            Scene scene = new Scene(gameView);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
