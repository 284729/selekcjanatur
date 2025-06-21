module proj.selekcjanatur {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens proj.selekcjanatur to javafx.fxml;
    exports proj.selekcjanatur;
}