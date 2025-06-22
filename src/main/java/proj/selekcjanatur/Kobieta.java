package proj.selekcjanatur;

/**
 * @file    Kobieta.java
 * @brief   Klasa reprezentująca kobietę w symulacji ewolucyjnej
 */

/**
 * @class   Kobieta
 * @brief   Specjalizacja klasy Czlowiek reprezentująca kobietę
 *
 * Klasa rozszerza podstawową funkcjonalność człowieka o:
 * - Specyficzne cechy reprodukcyjne
 * - Ograniczoną maksymalną prędkość chodzenia (1-2 zamiast 1-3)
 * - Logikę decydującą o możliwości rozmnażania
 */
public class Kobieta extends Czlowiek {

    /**
     * @brief Konstruktor tworzący nową kobietę
     * @param x Pozycja startowa x na planszy
     * @param y Pozycja startowa y na planszy
     *
     * @details Inicjalizuje kobietę z:
     * - Losową prędkością chodzenia z zakresu 1-2
     * - Standardowymi genami wzroku i metabolizmu
     * - Początkowym poziomem energii 100%
     */
    public Kobieta(int x, int y) {
        super(x, y);
        this.geny[1] = new GenPredkoscChodzenia(App.random.nextInt(1, 3));
    }

    /**
     * @brief Sprawdza płeć człowieka
     * @return Zawsze false (obiekt reprezentuje kobietę)
     */
    @Override
    public boolean czyMezczyzna() {
        return false;
    }

    /**
     * @brief Określa możliwość rozmnażania
     * @return true jeśli spełnione są wszystkie warunki:
     * - Nie jest dzieckiem (wiek ≥ 18 cykli)
     * - Poziom głodu > 70%
     * - Od ostatniego rozmnażania minęło > 20 cykli
     *
     * @details Kobiety mają bardziej restrykcyjne wymagania energetyczne
     * do rozmnażania niż mężczyźni (70% vs 50% energii).
     */
    @Override
    public boolean mozeRozmnazac() {
        if (jestDzieckiem()) return false;
        return poziomGlodu > 70 && czasOdRozmnazania > 20;
    }
}