package proj.selekcjanatur;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.util.Duration;

/**
 * @file    AppController.java
 * @brief   Kontroler głównego interfejsu symulacji ewolucyjnej
 */

/**
 * @class   AppController
 * @brief   Główny kontroler zarządzający interfejsem i logiką symulacji
 *
 * Klasa odpowiedzialna za:
 * - Inicjalizację siatki komórek
 * - Obsługę symulacji
 * - Wyświetlanie symulacji
 * - Obsługę zdarzeń kończących symulację
 */
public class AppController {
    /** @brief Szerokość planszy w komórkach (pobrana z klasy Symulacja) */
    private static final int KOLUMNY = Symulacja.szerokosc;

    /** @brief Wysokość planszy w komórkach (pobrana z klasy Symulacja) */
    private static final int WIERSZE = Symulacja.wysokosc;

    /** @brief Kontener GridPane przechowujący siatkę komórek */
    @FXML
    private GridPane grid;

    /** @brief Dwuwymiarowa tablica przechowująca panele komórek */
    private final Pane[][] komorki = new Pane[WIERSZE][KOLUMNY];

    /** @brief Referencja do obiektu symulacji */
    private InterfejsSymulacji symulacja;

    /** @brief Timeline kontrolująca cykl symulacji */
    private Timeline timeline;

    /** @brief Przycisk do wstrzymywania/wznawiania symulacji */
    @FXML
    private Button pauseButton;

    /** @brief Flaga określająca czy symulacja jest wstrzymana */
    private boolean pauza = false;

    /** @brief Flaga określająca czy symulacja działa w trybie odtwarzania z pliku */
    public static boolean PLIK = false;

    /**
     * @brief Metoda inicjalizująca kontroler
     * @details Wykonywana automatycznie po załadowaniu widoku FXML.
     * Tworzy siatkę komórek, inicjalizuje symulację i uruchamia timer.
     */
    public void initialize() {
        for (int x = 0; x < KOLUMNY; x++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / KOLUMNY);
            col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);
        }

        for (int y = 0; y < WIERSZE; y++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / WIERSZE);
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }

        for (int y = 0; y < WIERSZE; y++) {
            for (int x = 0; x < KOLUMNY; x++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(20, 20);
                cell.setStyle("-fx-background-color: transparent; -fx-border-color: gray; -fx-border-width: 0.25;");
                grid.add(cell, x, y);
                komorki[y][x] = cell;
            }
        }

        try {
            symulacja = PLIK ? new SymulacjaPlik(App.dziennikZdarzen) : new Symulacja(KOLUMNY, WIERSZE);

            timeline = new Timeline(
                    new KeyFrame(Duration.millis(250), event -> {
                        symulacja.aktualizuj();
                        rysujPlansze();

                        if (symulacja.czySymulacjaZakonczona()) {
                            timeline.stop();
                            pokazOknoZakonczenia();
                        }
                    })
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } catch (Exception e) {
            e.printStackTrace();
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Błąd odczytania pliku " + App.dziennikZdarzen);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * @brief Aktualizuje wygląd planszy na podstawie stanu symulacji
     * @details Metoda:
     * 1. Resetuje wszystkie komórki do koloru białego
     * 2. Rysuje jedzenie na ciemnozielono
     * 3. Rysuje ludzi (mężczyzn na niebiesko, kobiety na różowo, dzieci z gradientem)
     */
    private void rysujPlansze() {
        // Reset wszystkich komórek
        for (int y = 0; y < WIERSZE; y++) {
            for (int x = 0; x < KOLUMNY; x++) {
                komorki[y][x].getChildren().clear();
            }
        }

        //Zajęte pola
        Set<Point> polaLudzi = new HashSet<>();
        for (Czlowiek cz : symulacja.getLudzie()) {
            polaLudzi.add(new Point(cz.x, cz.y));
        }

        // Rysowanie jedzenia
        for (Jedzenie j : symulacja.getJedzenie()) {
            if (polaLudzi.contains(new Point(j.x, j.y))) continue;

            Region jedzenie = new Region();
            jedzenie.setPrefSize(10, 10);
            jedzenie.setStyle("-fx-background-color: darkgreen;");

            komorki[j.y][j.x].getChildren().add(jedzenie);
        }

        // Rysowanie ludzi
        for (Czlowiek cz : symulacja.getLudzie()) {
            Region kolko = new Region();
            kolko.setPrefSize(16, 16);

            String kolor = cz.czyMezczyzna() ? "blue" : "pink";
            String border = cz.jestDzieckiem() ? "yellow" : "transparent";

            kolko.setStyle(
                    "-fx-background-color: " + kolor + ";" +
                            "-fx-background-radius: 50%;" +
                            "-fx-border-radius: 50%;" +
                            "-fx-border-color: " + border + ";" +
                            "-fx-border-width: 2;"
            );

            komorki[cz.y][cz.x].getChildren().add(kolko);
        }
    }

    /**
     * @brief Wyświetla okno dialogowe po zakończeniu symulacji
     * @details W zależności od trybu (plik/live) wyświetla odpowiedni komunikat
     * i w trybie live zapisuje dziennik zdarzeń do pliku.
     */
    private void pokazOknoZakonczenia() {
        Platform.runLater(() -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Symulacja zakończona");
            alert.setHeaderText(null);
            if (PLIK) {
                alert.setContentText("Wszystkie zdarzenia z pliku zostały odtworzone.");
            } else {
                alert.setContentText("Symulacja zakończyła się, ponieważ liczba ludzi wynosi 0.");
            }
            alert.showAndWait();
            if (!PLIK) Symulacja.zapiszDziennikDoPliku(App.dziennikZdarzen);
            System.exit(0); // Zakończenie programu
        });
    }

    /**
     * @brief Obsługuje wciśnięcie przycisku pauzy
     * @details Zmienia stan symulacji (pauza/wznów) i aktualizuje tekst przycisku
     */
    @FXML
    private void togglePause() {
        pauza = !pauza;
        if (pauza) {
            pauseButton.setText("Wznów");
            timeline.pause();
        } else {
            pauseButton.setText("Pauza");
            timeline.play();
        }
    }
}