package proj.selekcjanatur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StartController {

    @FXML private Slider sliderWidth, sliderHeight, sliderPeople, sliderFood, sliderFoodPerTick;
    @FXML private Label labelWidth, labelHeight, labelPeople, labelFood, labelFoodPerTick;

    @FXML
    public void initialize() {
        bindSliderLabel(sliderWidth, labelWidth);
        bindSliderLabel(sliderHeight, labelHeight);
        bindSliderLabel(sliderPeople, labelPeople);
        bindSliderLabel(sliderFood, labelFood);
        bindSliderLabel(sliderFoodPerTick, labelFoodPerTick);
    }

    private void bindSliderLabel(Slider slider, Label label) {
        label.setText(String.valueOf((int) slider.getValue()));
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                label.setText(String.valueOf(newVal.intValue())));
    }

    @FXML
    private void startSimulation() {
        int width = (int) sliderWidth.getValue();
        int height = (int) sliderHeight.getValue();
        int people = (int) sliderPeople.getValue();
        int food = (int) sliderFood.getValue();
        int foodPerTick = (int) sliderFoodPerTick.getValue();

        int numberOfFields = width * height;
        int numberOfEntities = people + food;

        if (numberOfEntities > numberOfFields) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd konfiguracji planszy");
            alert.setHeaderText("Zbyt dużo obiektów na mapie");

            String message = "-> Liczba pól: " + numberOfFields + "\n" +
                    "-> Ludzie: " + people + "\n" +
                    "-> Jedzenie: " + food + "\n" +
                    "-> Łącznie zajętych pól: " + numberOfEntities + "\n\n" +
                    "Łączna liczba ludzi i jedzenia nie może przekraczać liczby dostępnych pól.";

            alert.setContentText(message);
            alert.showAndWait();
            return;
        }

        Symulacja.ustawParametry(width, height, people, food, foodPerTick);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("grid-view.fxml"));
            BorderPane gridView = loader.load();

            Stage stage = (Stage) sliderWidth.getScene().getWindow();
            stage.setScene(new Scene(gridView));
            stage.setTitle("Selekcja Naturalna - Symulacja");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void replayFromFile() {
        var path = Path.of("dziennik_zdarzen.txt");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            var linia = reader.readLine();
            var dane = linia == null ? null : linia.split(";");
            if (linia == null || !dane[0].equals("ROZMIAR")) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błądny format pliku");
                alert.setContentText("Plik nie jest poprawnym zapisem symulacji");
                alert.showAndWait();
                return;
            }

            var kolumny = Integer.parseInt(dane[1]);
            var wiersze = Integer.parseInt(dane[2]);
            Symulacja.szerokosc = kolumny;
            Symulacja.wysokosc = wiersze;
            AppController.PLIK = true;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("grid-view.fxml"));
            BorderPane gridView = loader.load();

            Stage stage = (Stage) sliderWidth.getScene().getWindow();
            stage.setScene(new Scene(gridView));
            stage.setTitle("Selekcja Naturalna - Odtworzenie symulacji z pliku");
        } catch (IOException e) {
            e.printStackTrace();
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd odczytania pliku dziennik_zdarzen.txt");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
