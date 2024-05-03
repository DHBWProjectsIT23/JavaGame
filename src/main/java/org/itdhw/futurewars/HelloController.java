package org.itdhw.futurewars;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class HelloController {
    @FXML
    private GridPane gameGrid;

    public void initialize() {
        System.out.println("Initialize method called");
        Image tileImage = new Image("file:64Sample.png");
        System.out.println("Tile image: " + tileImage);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                ImageView tile = new ImageView();
                tile.setId("tile" + y + "-" + x);
                System.out.println("Tile: " + tile);
                tile.setImage(tileImage);
                gameGrid.add(tile, x, y);
            }
        }
        System.out.println("Game grid: " + gameGrid);
    }
}