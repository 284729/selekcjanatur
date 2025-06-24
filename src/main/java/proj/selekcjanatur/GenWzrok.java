package proj.selekcjanatur;

/**
 * @file    GenWzrok.java
 * @brief   Implementacja genu odpowiedzialnego za zasięg wzroku
 */

/**
 * @class   GenWzrok
 * @brief   Klasa implementująca gen zasięgu wzroku
 *
 * Gen określa jak daleko człowiek widzi w symulacji.
 * Wartość genu jest z zakresu 1.0-10.0 gdzie:
 * - 1.0 = minimalny zasięg wzroku (1 komórka)
 * - 10.0 = maksymalny zasięg wzroku (10 komórek)
 *
 * Dziedziczenie odbywa się z możliwością mutacji ±1.0,
 * przy czym wartość jest zawsze ograniczana do zakresu 1-10.
 * Konstruktor prywatny jest używany tylko przy dziedziczeniu.
 */
public class GenWzrok implements Gen {
    /**
     * @brief Przechowuje wartość genu zasięgu wzroku
     * @details Wartość z przedziału 1.0-10.0
     */
    private final float wartosc;

    /**
     * @brief Konstruktor losujący początkową wartość genu
     * @details Generuje wartość całkowitą z przedziału 1-10
     */
    public GenWzrok() {
        this.wartosc = App.random.nextInt(1, 11);
    }

    /**
     * @brief Prywatny konstruktor do tworzenia genów potomnych
     * @param wartosc Określona wartość genu (zostanie ograniczona do 1-10)
     */
    public GenWzrok(float wartosc) {
        this.wartosc = wartosc;
    }

    /**
     * @brief Zwraca aktualną wartość genu
     * @return Wartość zasięgu wzroku w zakresie 1.0-10.0
     */
    @Override
    public float wartosc() {
        return wartosc;
    }

    /**
     * @brief Tworzy nowy gen poprzez dziedziczenie z mutacją
     * @return Nowa instancja GenWzrok
     *
     * @details Algorytm działania:
     * 1. Losuje mutację z zakresu ±1.0
     * 2. Dodaje mutację do obecnej wartości
     * 3. Ogranicza wynik do zakresu 1.0-10.0
     * 4. Zwraca nowy gen z zmutowaną wartością
     *
     * Przykład:
     * @code
     * Gen gen = new GenWzrok();
     * Gen potomek = gen.odziedzicz(); // np. 5.3
     * @endcode
     */
    @Override
    public Gen odziedzicz() {
        var mutacja = App.random.nextFloat(-1, 1);
        var nowa = Math.max(1, Math.min(10, wartosc + mutacja));
        return new GenWzrok(nowa);
    }
}