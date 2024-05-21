package org.itdhbw.futurewars.application.controllers.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ErrorHandler;

import java.util.Map;

public class ErrorViewController {
    @FXML
    private Label errorCountLabel;
    @FXML
    private GridPane errorList;
    @FXML
    private ScrollPane scrollPane;

    public void initialize() {
        errorCountLabel.textProperty()
                       .bind(ErrorHandler.errorCountProperty().asString());

        GridPane.setHgrow(errorList, Priority.ALWAYS);
        GridPane.setVgrow(errorList, Priority.ALWAYS);
        GridPane.setHgrow(scrollPane, Priority.ALWAYS);
        GridPane.setVgrow(scrollPane, Priority.ALWAYS);


        for (Map.Entry<Exception, String> e : ErrorHandler.getExceptions()
                                                          .entrySet()) {
            HBox errorEntry = new HBox();
            Label errorIndicator = new Label("ERROR: ");
            errorIndicator.setStyle("-fx-text-fill: red;");
            Label errorLabel = new Label(e.getValue());
            Label errorMessage = new Label(e.getKey().getMessage());
            errorEntry.getChildren()
                      .addAll(errorIndicator, errorLabel, errorMessage);

            errorList.addRow(errorList.getRowCount(), errorEntry);
        }
    }

    @FXML
    private void backToPrevious(ActionEvent actionEvent) {
        Stage stage = (Stage) errorList.getScene().getWindow();
        stage.setScene(Context.getGameState().getPreviousScene());
        Context.getOptionsController().loadSettings();
    }
}
