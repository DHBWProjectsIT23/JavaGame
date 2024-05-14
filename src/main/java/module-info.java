module org.itdhw.futurewars {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.apache.logging.log4j;


    opens org.itdhbw.futurewars to javafx.fxml;
    exports org.itdhbw.futurewars;
    exports org.itdhbw.futurewars.view.tile;
    exports org.itdhbw.futurewars.view.unit;
    exports org.itdhbw.futurewars.model.tile;
    exports org.itdhbw.futurewars.model.unit;
    exports org.itdhbw.futurewars.controller.tile;
    exports org.itdhbw.futurewars.controller.unit;
    exports org.itdhbw.futurewars.util;
    opens org.itdhbw.futurewars.controller.tile to javafx.fxml;
    exports org.itdhbw.futurewars.controller.ui;
    opens org.itdhbw.futurewars.controller.ui to javafx.fxml;
    exports org.itdhbw.futurewars.controller.tile.factory;
    opens org.itdhbw.futurewars.controller.tile.factory to javafx.fxml;
    exports org.itdhbw.futurewars.controller.unit.factory;
    exports org.itdhbw.futurewars.controller.ui.editor;
    opens org.itdhbw.futurewars.controller.ui.editor to javafx.fxml;
    exports org.itdhbw.futurewars.view;
}