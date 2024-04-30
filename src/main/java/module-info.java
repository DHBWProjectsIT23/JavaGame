module org.itdhw.futurewars {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.itdhw.futurewars to javafx.fxml;
    exports org.itdhw.futurewars;
}