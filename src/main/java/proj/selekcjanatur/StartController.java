package proj.selekcjanatur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @file    StartController.java
 * @brief   Kontroler ekranu startowego symulacji
 */

/**
 * @class   StartController
 * @brief   Kontroler GUI dla ekranu konfiguracji symulacji
 *
 * Klasa odpowiada za:
 * - Obsługę interfejsu użytkownika ekranu startowego
 * - Walidację parametrów symulacji
 * - Uruchamianie nowej symulacji
 * - Odtwarzanie symulacji z pliku dziennika zdarzeń
 */
public class StartController {
    /** @brief Suwak do ustawiania szerokości planszy */
    @FXML private Slider sliderWidth;

    /** @brief Suwak do ustawiania wysokości planszy */
    @FXML private Slider sliderHeight;

    /** @brief Suwak do ustawiania liczby ludzi */
    @FXML private Slider sliderPeople;

    /** @brief Suwak do ustawiania początkowej ilości jedzenia */
    @FXML private Slider sliderFood;

    /** @brief Suwak do ustawiania ilości nowego jedzenia na cykl */
    @FXML private Slider sliderFoodPerTick;

    /** @brief Etykieta wyświetlająca wartość szerokości */
    @FXML private Label labelWidth;

    /** @brief Etykieta wyświetlająca wartość wysokości */
    @FXML private Label labelHeight;

    /** @brief Etykieta wyświetlająca liczbę ludzi */
    @FXML private Label labelPeople;

    /** @brief Etykieta wyświetlająca ilość jedzenia */
    @FXML private Label labelFood;

    /** @brief Etykieta wyświetlająca ilość jedzenia na cykl */
    @FXML private Label labelFoodPerTick;

    /** @brief Pole tekstowe na ścierzkę prowadzącą do pliku dziennika zdarzeń */
    @FXML private TextField filePath;

    /**
     * @brief Inicjalizacja kontrolera
     * @details Wiąże suwaki z odpowiadającymi im etykietami,
     * aby wartości były wyświetlane w czasie rzeczywistym.
     */
    @FXML
    public void initialize() {
        bindSliderLabel(sliderWidth, labelWidth);
        bindSliderLabel(sliderHeight, labelHeight);
        bindSliderLabel(sliderPeople, labelPeople);
        bindSliderLabel(sliderFood, labelFood);
        bindSliderLabel(sliderFoodPerTick, labelFoodPerTick);
    }

    /**
     * @brief Wiąże suwak z etykietą wyświetlającą jego wartość
     * @param slider Suwak do powiązania
     * @param label Etykieta wyświetlająca wartość
     */
    private void bindSliderLabel(Slider slider, Label label) {
        label.setText(String.valueOf((int) slider.getValue()));
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                label.setText(String.valueOf(newVal.intValue())));
    }

    /** @brief Uruchamia dialog wybór pliku */
    @FXML
    private void chooseFile() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik dziennika zdarzeń");
        fileChooser.setInitialDirectory(new File("."));
        var file = fileChooser.showOpenDialog(filePath.getScene().getWindow());
        if (file != null) filePath.setText(file.getAbsolutePath());
    }

    /**
     * @brief Uruchamia nową symulację z wybranymi parametrami
     * @details Sprawdza poprawność parametrów i uruchamia symulację.
     * Wyświetla komunikat błędu jeśli liczba obiektów przekracza
     * liczbę dostępnych pól na planszy.
     */
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

        App.dziennikZdarzen = filePath.getText();
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

    /**
     * @brief Odtwarza symulację z pliku dziennika zdarzeń
     * @details Wczytuje podstawowe parametry symulacji z pliku
     * i przełącza na widok symulacji w trybie odtwarzania.
     * Wyświetla komunikaty błędów jeśli plik jest nieprawidłowy.
     */
    @FXML
    private void replayFromFile() {
        var dziennikZdarzen = filePath.getText();
        var path = Path.of(dziennikZdarzen);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            var linia = reader.readLine();
            var dane = linia == null ? null : linia.split(";");
            if (linia == null || !dane[0].equals("ROZMIAR")) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Błądny format pliku");
                alert.setContentText("Plik nie jest poprawnym zapisem symulacji");
                alert.showAndWait();
                return;
            }

            var kolumny = Integer.parseInt(dane[1]);
            var wiersze = Integer.parseInt(dane[2]);
            Symulacja.szerokosc = kolumny;
            Symulacja.wysokosc = wiersze;
            App.dziennikZdarzen = dziennikZdarzen;
            AppController.PLIK = true;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("grid-view.fxml"));
            BorderPane gridView = loader.load();

            Stage stage = (Stage) sliderWidth.getScene().getWindow();
            stage.setScene(new Scene(gridView));
            stage.setTitle("Selekcja Naturalna - Odtworzenie symulacji z pliku");
        } catch (IOException e) {
            e.printStackTrace();
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Błąd odczytania pliku " + dziennikZdarzen);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}