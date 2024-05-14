package org.itdhbw.futurewars.controller.ui.editor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.itdhbw.futurewars.model.editor.EditorTile;
import org.itdhbw.futurewars.model.game.Context;

import java.io.*;
import java.util.function.Consumer;

public class MapEditorController {
    private static final int MIN_MAP_SIZE = 5;
    private static final int MAX_MAP_SIZE = 50;
    private static final String INVALID_MAP_SIZE = "Invalid map size";
    private static final String MAP_SIZE_MUST_BE_AT_LEAST = "Map size must be at least 5x5";
    private static final String MAP_SIZE_MUST_BE_AT_MOST = "Map size must be at most 50x50";

    private static final Logger LOGGER = LogManager.getLogger(MapEditorController.class);
    private final IntegerProperty width = new SimpleIntegerProperty(0);
    private final IntegerProperty height = new SimpleIntegerProperty(0);
    private final ObjectProperty<EditorTile> activeEditorBox = new SimpleObjectProperty<>();
    private File selectedFile;
    @FXML
    private TextField widthInput;
    @FXML
    private TextField heightInput;
    @FXML
    private Button confirmSizeButton;
    @FXML
    private Label statusLabel;
    @FXML
    private GridPane editorGrid;
    @FXML
    private MenuButton tileDropdown;
    @FXML
    private MenuButton unitDropdown;
    @FXML
    private Button saveButton;

    public void initialize() {
        initializeEditorGrid();
        populateTileDropdown();
        populateUnitDropdown();
        initializeActiveEditorBox();
        initializeInputs();
    }

    private void initializeEditorGrid() {
        editorGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }

    private void populateTileDropdown() {
        String[] tileTypes = Context.getTileRepository().getTileTypes().toArray(new String[0]);
        populateDropdown(tileDropdown, tileTypes, this::setActiveEditorBoxTileType);
    }

    private void populateUnitDropdown() {
        String[] unitTypes = Context.getUnitRepository().getUnitTypes().toArray(new String[0]);
        populateDropdown(unitDropdown, unitTypes, this::setActiveEditorBoxUnitType);
        addMenuItemToDropdown(unitDropdown, "None", _ -> setActiveEditorBoxUnitType(null));
    }

    private <T> void populateDropdown(MenuButton dropdown, T[] values, Consumer<T> action) {
        for (T value : values) {
            addMenuItemToDropdown(dropdown, value.toString(), _ -> action.accept(value));
        }
        dropdown.setDisable(true);
    }

    private void setActiveEditorBoxTileType(String tileType) {
        if (activeEditorBox.get() != null) {
            activeEditorBox.get().setTileType(tileType);
        }
    }

    private void addMenuItemToDropdown(MenuButton dropdown, String text, EventHandler<ActionEvent> eventHandler) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(eventHandler);
        dropdown.getItems().add(menuItem);
    }

    private void setActiveEditorBoxUnitType(String unitType) {
        if (activeEditorBox.get() != null) {
            activeEditorBox.get().setUnitType(unitType);
        }
    }

    private void setupNewGrid(int width, int height) {
        clearGrid();
        initializeGrid(width, height);
        addNewCells(width, height);
        editorGrid.setGridLinesVisible(true);
    }

    private void clearGrid() {
        editorGrid.getChildren().clear();
        editorGrid.getRowConstraints().clear();
        editorGrid.getColumnConstraints().clear();
    }

    private void addNewCells(int width, int height) {
        // Add the new cells
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                EditorTile editorTile = createEditorTile(width, height, j);
                setEditorTileClickHandler(editorTile);
                editorGrid.add(editorTile, j, i);
            }
        }
    }

    private void setEditorTileClickHandler(EditorTile editorTile) {
        editorTile.setOnMouseClicked(event -> {
            activeEditorBox.set(editorTile);
            if (editorTile.getTileType() != null) {
                tileDropdown.setText(editorTile.getTileType());
            } else {
                tileDropdown.setText("No TileType");
            }

            if (editorTile.getUnitType() != null) {
                unitDropdown.setText(editorTile.getUnitType());
            } else {
                unitDropdown.setText("No UnitType");
            }
        });
    }

    private EditorTile createEditorTile(int width, int height, int j) {
        EditorTile editorTile = new EditorTile();
        editorTile.setBorder(new Border(new BorderStroke(j % 2 == 0 ? Color.BLACK : Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        GridPane.setFillWidth(editorTile, true);
        GridPane.setFillHeight(editorTile, true);
        if (width > 28 || height > 20) {
            editorTile.setLabelsVisible(false);
        }
        return editorTile;
    }

    private void initializeGrid(int width, int height) {
        addRowsToGrid(height);
        addColumnsToGrid(width);
    }

    private void onEditorTileSelected(EditorTile editorTile) {
        this.activeEditorBox.get().setOpacityOnSelection(true);
        Double opacity = this.activeEditorBox.get().getOpacity();
        String opacityString = String.valueOf(opacity);
        LOGGER.info("Opacity: {}", opacityString);
        tileDropdown.setDisable(false);
        unitDropdown.setDisable(false);
    }

    private void addRowsToGrid(int height) {
        for (int i = 0; i < height; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / height);
            editorGrid.getRowConstraints().add(rowConstraints);
        }
    }

    private void addColumnsToGrid(int width) {
        for (int j = 0; j < width; j++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / width);
            editorGrid.getColumnConstraints().add(columnConstraints);
        }
    }

    private void initializeActiveEditorBox() {
        LOGGER.info("Initializing active editor box...");
        activeEditorBox.addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Active editor box changed - oldValue: {} - newValue: {}", oldValue, newValue);
            if (oldValue != null) {
                oldValue.setOpacityOnSelection(false);
            }
            if (newValue != null) {
                onEditorTileSelected(newValue);
            } else {
                tileDropdown.setDisable(true);
                unitDropdown.setDisable(true);
            }
        });
    }

    private void initializeInputs() {
        widthInput.setText("5");
        heightInput.setText("5");
        setSize(null);
        this.confirmSizeButton.setDisable(false);
        this.saveButton.setDisable(true);
    }


    @FXML
    private void setSize(ActionEvent actionEvent) {
        LOGGER.info("Setting map size...");
        try {
            int localWidth = Integer.parseInt(widthInput.getText());
            int localHeight = Integer.parseInt(heightInput.getText());
            if (!validateMapSize(localWidth, localHeight)) {
                return;
            }
            this.width.set(localWidth);
            this.height.set(localHeight);
            statusLabel.setText("Map size set to " + localWidth + "x" + localHeight);
            setupNewGrid(localWidth, localHeight);
            editorGrid.setGridLinesVisible(true);
            this.confirmSizeButton.setDisable(true);
        } catch (NumberFormatException e) {
            statusLabel.setText(INVALID_MAP_SIZE);
        }
    }

    private boolean validateMapSize(int width, int height) {
        if (width < MIN_MAP_SIZE || height < MIN_MAP_SIZE) {
            statusLabel.setText(MAP_SIZE_MUST_BE_AT_LEAST);
            return false;
        }
        if (width > MAX_MAP_SIZE || height > MAX_MAP_SIZE) {
            statusLabel.setText(MAP_SIZE_MUST_BE_AT_MOST);
            return false;
        }
        return true;
    }

    @FXML
    private void saveMapAs(ActionEvent actionEvent) {
        LOGGER.info("Saving map as...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map As");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Future Wars Map Files", "*.fwm"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        // Set the initial directory to the current directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            this.selectedFile = file;
            this.saveButton.setDisable(false);
            saveMapToFile(file);
        }
    }


    @FXML
    public void saveMap(ActionEvent actionEvent) {
        LOGGER.info("Saving map...");
        if (selectedFile != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save the map to " + selectedFile.getName() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                saveMapToFile(selectedFile);
            }
        }
    }

    public void saveMapToFile(File file) {
        LOGGER.info("Saving map to file...");
        try {
            PrintWriter writer = new PrintWriter(file);

            // Write the first line
            writer.println("FUTURE_WARS_MAP_FORMAT_NEW");

            // Write an empty line
            writer.println();

            // Write the width and height
            writer.println(width.get() + "," + height.get());

            // Write an empty line
            writer.println();

            // Write the tiles
            for (int i = 0; i < height.get(); i++) {
                for (int j = 0; j < width.get(); j++) {
                    EditorTile editorTile = (EditorTile) getNodeFromGridPane(editorGrid, j, i);

                    String tileType = editorTile.getTileType() != null ? editorTile.getTileType() : "NONE";
                    String unitType = editorTile.getUnitType() != null ? editorTile.getUnitType() : "NONE";

                    writer.print(tileType + "," + unitType);

                    // If this is not the last tile in the row, add a comma
                    if (j < width.get() - 1) {
                        writer.print(",");
                    }
                }

                // End of the row
                writer.println();
            }

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }


    @FXML
    private void loadMap(ActionEvent actionEvent) {
        LOGGER.info("Loading map...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Future Wars Map Files", "*.fwm"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        // Set the initial directory to the current directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            loadMapFromFile(selectedFile);
        } else {
            System.out.println("File selection cancelled.");
        }
    }


    private void loadMapFromFile(File file) {
        LOGGER.info("Loading map from file...");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            for (int i = 0; i < 2; i++) {
                reader.readLine();
            }

            // Read the width and height
            String[] size = reader.readLine().split(",");
            int localWidth = Integer.parseInt(size[0]);
            int localHeight = Integer.parseInt(size[1]);
            this.width.set(localWidth);
            this.height.set(localHeight);

            clearGrid();
            initializeGrid(localWidth, localHeight);

            // Read the tiles
            String line;
            int y = 0;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tileData = line.split(",");
                for (int x = 0; x < localWidth; x++) {
                    EditorTile editorTile = new EditorTile();

                    // Set the TileType and UnitType
                    String tileTypeString = tileData[x * 2];
                    if (!tileTypeString.equals("NONE")) {
                        LOGGER.info("Setting tile type to {}", tileTypeString);
                        editorTile.setTileType(tileTypeString);
                    }

                    String unitTypeString = tileData[x * 2 + 1];
                    if (!unitTypeString.equals("NONE")) {
                        LOGGER.info("Setting unit type to {}", unitTypeString);
                        editorTile.setUnitType(unitTypeString);
                    }
                    setEditorTileClickHandler(editorTile);
                    editorGrid.add(editorTile, x, y);
                }
                y++;
            }

            this.confirmSizeButton.setDisable(true);
            this.saveButton.setDisable(false);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
