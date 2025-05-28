package proj.selekcjanatur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Random;

public class App extends Application {
    public static Random random = new Random();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("grid-view.fxml"));
        GridPane root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Selekcja Naturalna - Symulacja");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
