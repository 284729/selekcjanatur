package proj.selekcjanatur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

/**
 * @file    App.java
 * @brief   Główna klasa aplikacji odpowiedzialna za uruchomienie symulacji.
 * @author  Michał Kowalewski
 * @author  Meteusz Sobański
 * @author  Krystian Szydłowski
 * @date    20.06.2025
 */

/**
 * @class   App
 * @brief   Główna klasa aplikacji dziedzicząca po Application z JavaFX.
 *
 * Klasa odpowiedzialna za inicjalizację interfejsu użytkownika
 * oraz uruchomienie symulacji.
 */
public class App extends Application {
    /**
     * @brief   Współdzielony generator liczb pseudolosowych.
     * @details Używany w całej aplikacji do generowania losowych wartości.
     */
    public static Random random = new Random();

    /** @brief Nazwa pliku dziennika zdarzeń */
    public static String dziennikZdarzen = "dziennik_zdarzen.txt";

    /**
     * @brief   Metoda startująca aplikację JavaFX.
     * @param   stage Główny kontener okna aplikacji.
     * @throws  Exception Może wyrzucić wyjątek przy problemach z ładowaniem FXML.
     *
     * @details Ładuje plik FXML z interfejsem użytkownika,
     *          ustawia scenę i tytuł okna, a następnie je wyświetla.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start-view.fxml"));
        VBox root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Ustawienia Symulacji");
        stage.show();
    }

    /**
     * @brief   Główna metoda uruchamiająca aplikację.
     * @param   args Argumenty wiersza poleceń (niewykorzystywane).
     *
     * @details Po zamknięciu aplikacji, jeśli nie zapisano do pliku,
     *          automatycznie zapisuje dziennik zdarzeń.
     */
    public static void main(String[] args) {
        launch(args);
        if (!AppController.PLIK) Symulacja.zapiszDziennikDoPliku(dziennikZdarzen);
    }
}