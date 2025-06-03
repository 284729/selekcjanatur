package proj.selekcjanatur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class AppController {
    private static final int KOLUMNY = 64;
    private static final int WIERSZE = 36;

    @FXML
    private GridPane grid;

    private final Pane[][] komorki = new Pane[WIERSZE][KOLUMNY];

    private Symulacja symulacja;
    private Timeline timeline;

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
                Pane cell = new Pane();
                cell.setPrefSize(20, 20);
                cell.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-width: 0.25;");
                grid.add(cell, x, y);
                komorki[y][x] = cell;
            }
        }

        symulacja = new Symulacja(KOLUMNY, WIERSZE);

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
    }

    private void rysujPlansze() {
        // Reset wszystkich komórek
        for (int y = 0; y < WIERSZE; y++) {
            for (int x = 0; x < KOLUMNY; x++) {
                komorki[y][x].setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-width: 0.25;");
            }
        }

        // Rysowanie jedzenia
        for (Jedzenie j : symulacja.getJedzenie()) {
            komorki[j.y][j.x].setStyle("-fx-background-color: darkgreen; -fx-border-color: gray; -fx-border-width: 0.25;");
        }

        // Rysowanie ludzi
        for (Czlowiek cz : symulacja.getLudzie()) {
            String kolor = cz.czyMezczyzna() ? "blue" : "pink";
            if (cz.jestDzieckiem()) {
                komorki[cz.y][cz.x].setStyle("-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, yellow 50%, " + kolor + " 50%);");
            } else {
                komorki[cz.y][cz.x].setStyle("-fx-background-color: " + kolor + ";");
            }
        }
    }
    private void pokazOknoZakonczenia() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Symulacja zakończona");
            alert.setHeaderText(null);
            alert.setContentText("Symulacja zakończyła się, ponieważ liczba ludzi wynosi 0.");
            alert.showAndWait();
            Symulacja.zapiszDziennikDoPliku("dziennik_zdarzen.txt");
            System.exit(0); // Zakończenie programu
        });
    }
}
