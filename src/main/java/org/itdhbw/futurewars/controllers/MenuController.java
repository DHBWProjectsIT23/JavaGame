package org.itdhbw.futurewars.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button startButton;

    @FXML
    private void startGame(ActionEvent actionEvent) {
        try {
            Parent mapView = FXMLLoader.load(getClass().getResource("/org/itdhbw/futurewars/map-view.fxml"));
            Scene scene = new Scene(mapView);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
