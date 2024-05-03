module org.itdhw.futurewars {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens org.itdhbw.futurewars to javafx.fxml;
    exports org.itdhbw.futurewars;
    exports org.itdhbw.futurewars.controller;
    opens org.itdhbw.futurewars.controller to javafx.fxml;
}