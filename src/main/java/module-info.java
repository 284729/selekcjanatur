module proj.selekcjanatur {
    /** Wymaga modułu kontrolek JavaFX */
    requires javafx.controls;

    /** Wymaga modułu FXML JavaFX */
    requires javafx.fxml;
    requires java.desktop;

    /** Udostępnia pakiety do refleksji dla JavaFX FXML */
    opens proj.selekcjanatur to javafx.fxml;

    /** Eksportuje główny pakiet aplikacji */
    exports proj.selekcjanatur;
}