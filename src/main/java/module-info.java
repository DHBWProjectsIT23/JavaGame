module org.itdhw.futurewars {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.desktop;


    opens org.itdhbw.futurewars to javafx.fxml;
    exports org.itdhbw.futurewars;
    exports org.itdhbw.futurewars.game.controllers.tile.factory;
    opens org.itdhbw.futurewars.game.controllers.tile.factory to javafx.fxml;
    exports org.itdhbw.futurewars.game.controllers.unit.factory;
    exports org.itdhbw.futurewars.exceptions;
    exports org.itdhbw.futurewars.map_editor.controllers;
    opens org.itdhbw.futurewars.map_editor.controllers to javafx.fxml;
    exports org.itdhbw.futurewars.game.models.tile;
    exports org.itdhbw.futurewars.game.models.unit;
    exports org.itdhbw.futurewars.game.controllers.tile;
    opens org.itdhbw.futurewars.game.controllers.tile to javafx.fxml;
    exports org.itdhbw.futurewars.game.controllers.unit;
    exports org.itdhbw.futurewars.game.controllers.ui;
    opens org.itdhbw.futurewars.game.controllers.ui to javafx.fxml;
    exports org.itdhbw.futurewars.application.controllers.ui;
    opens org.itdhbw.futurewars.application.controllers.ui to javafx.fxml;
    exports org.itdhbw.futurewars.game.utils;
    exports org.itdhbw.futurewars.application.utils;
    exports org.itdhbw.futurewars.game.views;
}