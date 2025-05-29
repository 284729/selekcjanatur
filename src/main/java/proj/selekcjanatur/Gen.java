package proj.selekcjanatur;

public interface Gen {
    float wartosc();  // aktualna wartość cechy
    Gen odziedzicz(); // tworzy nowy gen z mutacją wartości
}
