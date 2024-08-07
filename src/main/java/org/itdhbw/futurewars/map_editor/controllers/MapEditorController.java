package org.itdhbw.futurewars.map_editor.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.itdhbw.futurewars.application.models.Context;
import org.itdhbw.futurewars.application.utils.ConfirmPopup;
import org.itdhbw.futurewars.application.utils.ErrorHandler;
import org.itdhbw.futurewars.game.controllers.tile.factory.TileFactory;
import org.itdhbw.futurewars.map_editor.models.EditorTile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class MapEditorController {
    private static final int MIN_MAP_SIZE = 5;
    private static final int MAX_MAP_SIZE = 50;
    private static final String INVALID_MAP_SIZE = "Invalid map size";
    private static final String MAP_SIZE_MUST_BE_AT_LEAST = "Map size must be at least 5x5";
    private static final String MAP_SIZE_MUST_BE_AT_MOST = "Map size must be at most 50x50";

    private static final Logger LOGGER = Logger.getLogger(MapEditorController.class.getSimpleName());
    private final Map<String, Map<Integer, String>> textureIndexNameMap;
    private final Map<String, TileFactory> tileFactoryMap;
    private final Map<String, List<Image>> textureMap;
    private final IntegerProperty width = new SimpleIntegerProperty(0);
    private final IntegerProperty height = new SimpleIntegerProperty(0);
    private final ObjectProperty<EditorTile> activeEditorBox = new SimpleObjectProperty<>();
    private int tileSize;
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
    @FXML
    private MenuButton textureDropdown;
    @FXML
    private MenuButton teamDropdown;
    @FXML
    private StackPane parentPane;

    public MapEditorController() {
        this.tileFactoryMap = Context.getTileBuilder().getTileFactories();
        this.textureMap = new HashMap<>();
        this.textureIndexNameMap = new HashMap<>();

        for (Map.Entry<String, TileFactory> entry : tileFactoryMap.entrySet()) {
            List<Image> textures = entry.getValue().getTextures();
            textureMap.put(entry.getKey(), textures);

            int i = 0;
            Map<Integer, String> textureNames = new HashMap<>();
            for (Image texture : textures) {
                String textureName = texture.getUrl().substring(texture.getUrl().lastIndexOf("/") + 1);
                textureName = textureName.split("\\.")[0].replace("_", " ");
                textureNames.put(i, textureName);
                i++;
            }
            textureIndexNameMap.put(entry.getKey(), textureNames);
        }
    }

    public void initialize() {
        initializeEditorGrid();
        populateTileDropdown();
        populateUnitDropdown();
        populateTeamDropdown();
        textureDropdown.setDisable(true);
        initializeActiveEditorBox();
        initializeInputs();
    }

    private void initializeEditorGrid() {
        editorGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        editorGrid.setVgap(0);
        editorGrid.setHgap(0);
    }

    private void populateTileDropdown() {
        String[] tileTypes = tileFactoryMap.keySet().toArray(new String[0]);
        populateDropdown(tileDropdown, tileTypes, this::setActiveEditorBoxTileType);
    }

    private void populateUnitDropdown() {
        String[] unitTypes = Context.getUnitRepository().getUnitTypes().toArray(new String[0]);
        populateDropdown(unitDropdown, unitTypes, this::setActiveEditorBoxUnitType);
        addMenuItemToDropdown(unitDropdown, "None", ignored -> setActiveEditorBoxUnitType("No Unit"));
    }

    private <T> void populateDropdown(MenuButton dropdown, T[] values, Consumer<T> action) {
        dropdown.getItems().clear();
        for (T value : values) {
            addMenuItemToDropdown(dropdown, value.toString(), ignored -> action.accept(value));
        }
        dropdown.setDisable(true);
    }

    private void populateTeamDropdown() {
        populateDropdown(teamDropdown, Team.values(), this::setActiveEditorBoxUnitTeam);
    }

    private void populateTextureDropdown(String tileType) {
        textureDropdown.getItems().clear();
        List<Image> textures = textureMap.get(tileType);
        Map<Integer, String> textureNames = textureIndexNameMap.get(tileType);
        for (int i = 0; i < textures.size(); i++) {
            int finalI = i;
            addMenuItemToDropdown(textureDropdown, textureNames.get(i),
                                  ignored -> setActiveEditorBoxTexture(textures.get(finalI), finalI));
        }
    }

    private void setActiveEditorBoxUnitType(String unitType) {
        if (activeEditorBox.get() != null) {
            if (unitDropdown.getText().equals("No Unit")) {
                setActiveEditorBoxUnitTeam(Team.TEAM_1);
            } else {
                activeEditorBox.get().setUnitType(null);
            }
            unitDropdown.setText(unitType);
            activeEditorBox.get().setUnitType(unitType);
            teamDropdown.setDisable(false);
        }
    }

    private void setActiveEditorBoxTexture(Image textureVariant, int index) {
        EditorTile editorTile = activeEditorBox.get();
        if (editorTile != null) {
            textureDropdown.setText(textureVariant.getUrl().substring(textureVariant.getUrl().lastIndexOf("/") + 1));
            editorTile.setTextureVariant(index);
        }
    }

    private void setActiveEditorBoxTileType(String tileType) {
        if (activeEditorBox.get() != null) {
            tileDropdown.setText(tileType);
            activeEditorBox.get().setTileType(tileType);
            activeEditorBox.get().setTextures(textureMap.get(tileType));
            this.onEditorTileSelected(activeEditorBox.get());
            setActiveEditorBoxTexture(textureMap.get(tileType).getFirst(), 0);
        }
    }

    private void setActiveEditorBoxUnitTeam(Team team) {
        if (activeEditorBox.get() != null) {
            teamDropdown.setText(team.toString());
            activeEditorBox.get().setUnitTeam(team.getValue());
        }
    }

    private void addMenuItemToDropdown(MenuButton dropdown, String text, EventHandler<ActionEvent> eventHandler) {
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(eventHandler);
        dropdown.getItems().add(menuItem);
    }

    private void setupNewGrid(int width, int height) {
        clearGrid();
        initializeGrid(width, height);
        addNewCells(width, height);
        editorGrid.setGridLinesVisible(true);
    }

    private void addNewCells(int width, int height) {
        // Add the new cells
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                EditorTile editorTile = createEditorTile(width, height, "PLAIN_TILE");
                setEditorTileClickHandler(editorTile);
                editorGrid.add(editorTile, j, i);
            }
        }

    }

    private void onEditorTileSelected(EditorTile editorTile) {
        this.activeEditorBox.get().setOpacityOnSelection(true);
        populateTextureDropdown(editorTile.getTileType());
        textureDropdown.setDisable(false);
        tileDropdown.setDisable(false);
        unitDropdown.setDisable(false);
    }

    private void initializeActiveEditorBox() {
        LOGGER.info("Initializing active editor box...");
        activeEditorBox.addListener((observable, oldValue, newValue) -> {
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
    private void setSize(ActionEvent ignored) {
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
    private void saveMapAs(ActionEvent ignored) {
        LOGGER.info("Saving map as...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map As");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Future Wars Map Files", "*.fwm"),
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

    public void saveMapToFile(File file) {
        LOGGER.info("Saving map to file...");
        try {
            PrintWriter writer = new PrintWriter(file);

            // Write the first line
            writer.println("FUTURE_WARS_MAP_FORMAT_V3");

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
                    if (editorTile == null) {
                        LOGGER.severe("Editor tile is null!");
                        continue;
                    }

                    String tileType = editorTile.getTileType() != null ? editorTile.getTileType() : "NONE";
                    String textureVariant = String.valueOf(editorTile.getTextureVariant());
                    String unitType = editorTile.getUnitType() != null ? editorTile.getUnitType() : "NONE";
                    if (unitType.equals("No Unit")) {
                        unitType = "NONE";
                    }
                    String unitTeam = String.valueOf(editorTile.getUnitTeam());

                    writer.print(tileType + "," + textureVariant + "," + unitType + "," + unitTeam);

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
            ErrorHandler.addException(e, "Failed to save map!");
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
    public void saveMap(ActionEvent ignored) {
        LOGGER.info("Saving map...");
        if (selectedFile != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Are you sure you want to save the map to " + selectedFile.getName() + "?",
                                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                saveMapToFile(selectedFile);
            }
        }
    }

    @FXML
    private void loadMap(ActionEvent ignored) {
        LOGGER.info("Loading map...");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Future Wars Map Files", "*.fwm"),
                                                 new FileChooser.ExtensionFilter("All Files", "*.*"));
        // Set the initial directory to the current directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            loadMapFromFile(selectedFile);
        } else {
            LOGGER.warning("Couldn't find file");
        }
        this.editorGrid.setMaxWidth(tileSize * (double) width.get());
        this.editorGrid.setMaxHeight(tileSize * (double) height.get());
    }

    private void loadMapFromFile(File file) {
        LOGGER.info("Loading map from file...");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            LOGGER.info("Loading map from file...");

            String validation = reader.readLine().split(",")[0];
            reader.readLine();

            // Read the width and height
            String[] size = reader.readLine().split(",");
            int localWidth = Integer.parseInt(size[0]);
            int localHeight = Integer.parseInt(size[1]);
            this.width.set(localWidth);
            this.height.set(localHeight);

            clearGrid();
            initializeGrid(localWidth, localHeight);

            if (Objects.equals(validation, "FUTURE_WARS_MAP_FORMAT_NEW")) {
                loadOldMap(reader);
            } else if (Objects.equals(validation, "FUTURE_WARS_MAP_FORMAT_V3")) {
                loadNewMap(reader);
            }

        } catch (IOException e) {
            ErrorHandler.addException(e, "Failed to load map!");
        }
    }

    private void clearGrid() {
        editorGrid.getChildren().clear();
        editorGrid.getRowConstraints().clear();
        editorGrid.getColumnConstraints().clear();
    }

    private void initializeGrid(int width, int height) {
        addRowsToGrid(height);
        addColumnsToGrid(width);
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

    private void loadOldMap(BufferedReader reader) throws IOException {
        // Read the tiles
        String line;
        int y = 0;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] tileData = line.split(",");
            for (int x = 0; x < this.width.get(); x++) {

                // Set the TileType and UnitType
                String tileTypeString = tileData[x * 2];
                EditorTile editorTile = this.createEditorTile(this.width.get(), this.height.get(), tileTypeString);

                String unitTypeString = tileData[x * 2 + 1];
                if (!unitTypeString.equals("NONE")) {
                    editorTile.setUnitType(unitTypeString);
                }
                setEditorTileClickHandler(editorTile);
                editorGrid.add(editorTile, x, y);
            }
            y++;
        }

        this.confirmSizeButton.setDisable(true);
        this.saveButton.setDisable(false);

    }

    private EditorTile createEditorTile(int width, int height, String tileType) {
        EditorTile editorTile = new EditorTile(tileType);

        double tileMaxHeight = Context.getPrimaryStage().getHeight() / height / 100 * 80;
        double tileMaxWidth = Context.getPrimaryStage().getWidth() / width / 100 * 80;
        int tileMaxSize = (int) Math.min(tileMaxHeight, tileMaxWidth);
        this.tileSize = tileMaxSize;

        editorTile.setPrefSize(tileMaxSize, tileMaxSize);
        editorTile.setTextures(textureMap.get(tileType));
        editorTile.setTextureVariant(0);

        GridPane.setFillWidth(editorTile, true);
        GridPane.setFillHeight(editorTile, true);

        return editorTile;
    }

    private void setEditorTileClickHandler(EditorTile editorTile) {
        editorTile.setOnMouseClicked(ignored -> {
            LOGGER.info("Editor tile clicked");
            activeEditorBox.set(editorTile);
            if (editorTile.getTileType() != null) {
                tileDropdown.setText(editorTile.getTileType());
                textureDropdown.setDisable(false);

                int textureVariant = editorTile.getTextureVariant();
                Map<Integer, String> textureNames = textureIndexNameMap.get(editorTile.getTileType());
                textureDropdown.setText(textureNames.get(textureVariant));

            } else {
                tileDropdown.setText("No Tile");
                textureDropdown.setDisable(true);
            }

            if (editorTile.getUnitType() != null) {
                unitDropdown.setText(editorTile.getUnitType());
                teamDropdown.setDisable(false);

                teamDropdown.setText(String.valueOf(Team.fromInt(editorTile.getUnitTeam())));

            } else {
                unitDropdown.setText("No Unit");
                teamDropdown.setDisable(true);
            }
        });
    }

    private void loadNewMap(BufferedReader reader) throws IOException {

        // Read the tiles
        LOGGER.info("Reading tiles....");
        String line;
        int y = 0;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] tileData = line.split(",");
            for (int x = 0; x < this.width.get(); x++) {

                // Set the TileType and UnitType
                String tileTypeString = tileData[x * 4];
                EditorTile editorTile = this.createEditorTile(this.width.get(), this.height.get(), tileTypeString);
                editorTile.setTextureVariant(Integer.parseInt(tileData[x * 4 + 1]));

                String unitTypeString = tileData[x * 4 + 2];
                if (!unitTypeString.equals("NONE")) {
                    editorTile.setUnitType(unitTypeString);
                }
                editorTile.setUnitTeam(Integer.parseInt(tileData[x * 4 + 3]));
                setEditorTileClickHandler(editorTile);
                editorGrid.add(editorTile, x, y);
            }
            y++;
        }

        this.confirmSizeButton.setDisable(true);
        this.saveButton.setDisable(false);
    }

    @FXML
    private void goBack(ActionEvent ignored) {
        ConfirmPopup.showWithRunnable(parentPane, "Are you sure you want to leave?",
                                      "All unsave progress will be lost!", this::returnToPrevious);
    }

    private void returnToPrevious() {
        Context.getPrimaryStage().getScene().setRoot(Context.getGameState().getPreviousRoot());
    }

    public enum Team {
        TEAM_1(1), TEAM_2(2);

        private final int value;

        Team(int value) {
            this.value = value;
        }

        public static Team fromInt(int value) {
            for (Team team : Team.values()) {
                if (team.getValue() == value) {
                    return team;
                }
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }

        public int getValue() {
            return value;
        }
    }

}
