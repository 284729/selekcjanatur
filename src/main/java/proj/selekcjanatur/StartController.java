package proj.selekcjanatur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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
            GridPane gridPane = loader.load();

            Stage stage = (Stage) sliderWidth.getScene().getWindow();
            stage.setScene(new Scene(gridPane));
            stage.setTitle("Selekcja Naturalna - Symulacja");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
