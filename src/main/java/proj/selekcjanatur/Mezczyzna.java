package proj.selekcjanatur;

/**
 * @file    Mezczyzna.java
 * @brief   Klasa reprezentująca mężczyznę w symulacji ewolucyjnej
 */

/**
 * @class   Mezczyzna
 * @brief   Specjalizacja klasy Czlowiek reprezentująca mężczyznę
 *
 * Klasa rozszerza podstawową funkcjonalność człowieka o:
 * - Męską płeć (zawsze zwraca true w czyMezczyzna())
 * - Specyficzne warunki reprodukcyjne (mniejsze wymagania niż kobieta)
 * - Standardową prędkość chodzenia (pełen zakres 1-3)
 */
public class Mezczyzna extends Czlowiek {

    /**
     * @brief Konstruktor tworzący nowego mężczyznę
     * @param x Pozycja startowa x na planszy
     * @param y Pozycja startowa y na planszy
     *
     * @details Inicjalizuje mężczyznę z:
     * - Standardowymi genami (wzrok, prędkość, metabolizm)
     * - Pełnym zakresem możliwej prędkości (1-3)
     * - Początkowym poziomem energii 100%
     */
    public Mezczyzna(int x, int y) {
        super(x, y);
    }

    /**
     * @brief Sprawdza płeć człowieka
     * @return Zawsze true (obiekt reprezentuje mężczyznę)
     */
    @Override
    public boolean czyMezczyzna() {
        return true;
    }

    /**
     * @brief Określa możliwość rozmnażania
     * @return true jeśli spełnione są wszystkie warunki:
     * - Nie jest dzieckiem (wiek ≥ 18 cykli)
     * - Poziom głodu > 60%
     * - Od ostatniego rozmnażania minęło > 10 cykli
     *
     * @details Mężczyźni mają mniejsze wymagania energetyczne
     * do rozmnażania niż kobiety (60% vs 70% energii)
     * i krótszy wymagany czas między rozmnażaniem (10 vs 20 cykli).
     */
    @Override
    public boolean mozeRozmnazac() {
        if (jestDzieckiem()) return false;
        return poziomGlodu > 60 && czasOdRozmnazania > 10;
    }
}