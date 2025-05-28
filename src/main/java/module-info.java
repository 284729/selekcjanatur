module proj.selekcjanatur {
    requires javafx.controls;
    requires javafx.fxml;


    opens proj.selekcjanatur to javafx.fxml;
    exports proj.selekcjanatur;
}