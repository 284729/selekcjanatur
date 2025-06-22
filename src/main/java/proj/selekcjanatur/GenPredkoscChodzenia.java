package proj.selekcjanatur;

/**
 * @file    GenPredkoscChodzenia.java
 * @brief   Implementacja genu odpowiedzialnego za prędkość poruszania się
 */

/**
 * @class   GenPredkoscChodzenia
 * @brief   Klasa implementująca gen prędkości chodzenia
 *
 * Gen określa z jaką częstotliwością człowiek porusza się w symulacji.
 * Wartość genu jest z zakresu 1.0-3.0 gdzie:
 * - 1.0 = najwolniejszy ruch (co 3 klatki)
 * - 2.0 = średnia prędkość (co 2 klatki)
 * - 3.0 = najszybszy ruch (co klatkę)
 *
 * Dziedziczenie odbywa się z możliwością mutacji ±0.75,
 * przy czym wartość jest zawszę ograniczana do zakresu 1-3.
 */
public class GenPredkoscChodzenia implements Gen {
    /**
     * @brief Przechowuje wartość genu prędkości
     * @details Wartość z przedziału 1.0-3.0
     */
    private final float wartosc;

    /**
     * @brief Konstruktor losujący początkową wartość genu
     * @details Generuje wartość całkowitą z przedziału 1-3
     */
    public GenPredkoscChodzenia() {
        this.wartosc = App.random.nextInt(1, 4);
    }

    /**
     * @brief Konstruktor z określeniem konkretnej wartości
     * @param wartosc Początkowa wartość genu (zostanie ograniczona do 1-3)
     */
    public GenPredkoscChodzenia(float wartosc) {
        this.wartosc = wartosc;
    }

    /**
     * @brief Zwraca aktualną wartość genu
     * @return Wartość prędkości w zakresie 1.0-3.0
     */
    @Override
    public float wartosc() {
        return wartosc;
    }

    /**
     * @brief Tworzy nowy gen poprzez dziedziczenie z mutacją
     * @return Nowa instancja GenPredkoscChodzenia
     *
     * @details Algorytm działania:
     * 1. Losuje mutację z zakresu ±0.75
     * 2. Dodaje mutację do obecnej wartości
     * 3. Ogranicza wynik do zakresu 1.0-3.0
     * 4. Zwraca nowy gen z zmutowaną wartością
     *
     * Przykład:
     * @code
     * Gen gen = new GenPredkoscChodzenia(2.0f);
     * Gen potomek = gen.odziedzicz(); // np. 2.3
     * @endcode
     */
    @Override
    public Gen odziedzicz() {
        var mutacja = App.random.nextFloat(-0.75f, 0.75f);
        var nowa = Math.max(1, Math.min(3, wartosc + mutacja));
        return new GenPredkoscChodzenia(nowa);
    }
}