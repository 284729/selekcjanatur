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

public class AppController {
    private static final int KOLUMNY = Symulacja.szerokosc;
    private static final int WIERSZE = Symulacja.wysokosc;

    @FXML
    private GridPane grid;

    private final Pane[][] komorki = new Pane[WIERSZE][KOLUMNY];

    private InterfejsSymulacji symulacja;
    private Timeline timeline;

    @FXML
    private Button pauseButton;

    private boolean pauza = false;

    public static boolean PLIK = false;

    public void initialize() {
        // Tworzenie siatki
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
            symulacja = PLIK ? new SymulacjaPlik("dziennik_zdarzen.txt") : new Symulacja(KOLUMNY, WIERSZE);

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
            alert.setTitle("Błąd odczytania pliku dziennik_zdarzen.txt");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void rysujPlansze() {
        // Wyczyść wszystkie komórki - aby uniknąć nakładania/duplikaci się elementów
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

        //Rysuj jedzenie
        for (Jedzenie j : symulacja.getJedzenie()) {
            if (polaLudzi.contains(new Point(j.x, j.y))) continue;

            Region jedzenie = new Region();
            jedzenie.setPrefSize(10, 10);
            jedzenie.setStyle("-fx-background-color: darkgreen;");

            komorki[j.y][j.x].getChildren().add(jedzenie);
        }
        //Rysuj ludzi
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
            if (!PLIK) Symulacja.zapiszDziennikDoPliku("dziennik_zdarzen.txt");
            System.exit(0); // Zakończenie programu
        });
    }

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
