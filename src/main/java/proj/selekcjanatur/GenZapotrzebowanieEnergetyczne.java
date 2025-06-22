package proj.selekcjanatur;

/**
 * @file    GenZapotrzebowanieEnergetyczne.java
 * @brief   Implementacja genu określającego metabolizm organizmu
 */

/**
 * @class   GenZapotrzebowanieEnergetyczne
 * @brief   Klasa implementująca gen zapotrzebowania energetycznego
 *
 * Gen określa ile energii człowiek traci w każdym cyklu symulacji.
 * Wartość genu reprezentuje procentowy ubytek energii na cykl:
 * - Niższe wartości: organizm oszczędniejszy (dłużej przeżyje bez jedzenia)
 * - Wyższe wartości: szybsze zużycie energii (większa potrzeba jedzenia)
 *
 * Dziedziczenie odbywa się z asymetryczną mutacją (-5 do +7),
 * przy czym minimalna wartość wynosi 5 (organizm nie może być całkowicie bierny).
 */
public class GenZapotrzebowanieEnergetyczne implements Gen {
    /**
     * @brief Przechowuje wartość genu metabolizmu
     * @details Wartość określa procentowy ubytek energii na cykl symulacji
     */
    private final float wartosc;

    /**
     * @brief Konstruktor losujący początkową wartość genu
     * @details Generuje wartość zmiennoprzecinkową z przedziału 5-20
     */
    public GenZapotrzebowanieEnergetyczne() {
        this.wartosc = App.random.nextFloat(5, 20);
    }

    /**
     * @brief Konstruktor z określeniem konkretnej wartości
     * @param wartosc Początkowa wartość genu (minimalna wartość to 5)
     */
    public GenZapotrzebowanieEnergetyczne(float wartosc) {
        this.wartosc = wartosc;
    }

    /**
     * @brief Zwraca aktualną wartość genu
     * @return Wartość zapotrzebowania energetycznego
     */
    @Override
    public float wartosc() {
        return wartosc;
    }

    /**
     * @brief Tworzy nowy gen poprzez dziedziczenie z mutacją
     * @return Nowa instancja GenZapotrzebowanieEnergetyczne
     *
     * @details Algorytm działania:
     * 1. Losuje mutację z zakresu -5 do +7 (asymetrycznie)
     * 2. Dodaje mutację do obecnej wartości
     * 3. Gwarantuje minimalną wartość 5
     * 4. Zwraca nowy gen z zmutowaną wartością
     *
     * @note Mutacja jest celowo asymetryczna - większa szansa na zwiększenie
     * metabolizmu niż na jego zmniejszenie, co symuluje ewolucyjną tendencję
     * do zwiększania aktywności organizmów.
     */
    @Override
    public Gen odziedzicz() {
        var mutacja = App.random.nextFloat(-5, 7);
        var nowa = Math.max(5, wartosc + mutacja);
        return new GenZapotrzebowanieEnergetyczne(nowa);
    }
}