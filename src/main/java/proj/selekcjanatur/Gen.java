package proj.selekcjanatur;

/**
 * @file    Gen.java
 * @brief   Interfejs definiujący podstawowe zachowania genów w symulacji
 */

/**
 * @interface Gen
 * @brief   Interfejs reprezentujący gen w symulacji ewolucyjnej
 *
 * Definiuje podstawowe operacje jakie musi implementować każdy gen:
 * - Dostęp do swojej wartości
 * - Mechanizm dziedziczenia z możliwością mutacji
 */
public interface Gen {
    /**
     * @brief Pobiera aktualną wartość cechy genetycznej
     * @return Wartość liczbowa genu (zależna od implementacji)
     *
     * @details Wartość może być stała lub zmieniać się w czasie,
     * w zależności od konkretnej implementacji genu.
     *
     * Przykład:
     * @code
     * Gen wzrok = new GenWzrok();
     * float zasieg = wzrok.wartosc(); // np. 2.5f
     * @endcode
     */
    float wartosc();

    /**
     * @brief Tworzy nowy gen poprzez dziedziczenie z mutacją
     * @return Nowa instancja genu potomnego
     *
     * @details Metoda powinna:
     * 1. Skopiować podstawowe cechy genu rodzica
     * 2. Zastosować losową mutację (zmianę wartości)
     * 3. Zwrócić nową instancję genu
     *
     * Przykład:
     * @code
     * Gen oryginalny = new GenPredkoscChodzenia();
     * Gen potomny = oryginalny.odziedzicz(); // nowa instancja z mutacją
     * @endcode
     */
    Gen odziedzicz();
}