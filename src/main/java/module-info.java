module org.itdhw.advancedwars {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.itdhw.advancedwars to javafx.fxml;
    exports org.itdhw.advancedwars;
}