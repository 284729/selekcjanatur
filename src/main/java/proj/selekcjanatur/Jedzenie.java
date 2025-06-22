package proj.selekcjanatur;

/**
 * @file    Jedzenie.java
 * @brief   Klasa reprezentująca źródło pożywienia w symulacji
 */

/**
 * @class   Jedzenie
 * @brief   Klasa modelująca pożywienie w symulacji ewolucyjnej
 *
 * Reprezentuje pojedyncze źródło pożywienia na planszy symulacji.
 * Określa pozycję (x,y) oraz wartość energetyczną, która jest losowana
 * przy tworzeniu obiektu. Klasa jest niezmiennicza (immutable) poza
 * modyfikacjami dokonywanymi przez symulację.
 */
public class Jedzenie {
    /**
     * @brief Pozycja x na planszy
     * @details Wartość z zakresu 0 do szerokości planszy - 1
     */
    public int x;

    /**
     * @brief Pozycja y na planszy
     * @details Wartość z zakresu 0 do wysokości planszy - 1
     */
    public int y;

    /**
     * @brief Wartość energetyczna pożywienia
     * @details Losowana z przedziału 10-50 w momencie tworzenia obiektu.
     * Im wyższa wartość, tym więcej energii zyskuje człowiek po zjedzeniu.
     */
    public int wartosc;

    /**
     * @brief Konstruktor tworzący nowe pożywienie
     * @param x Pozycja pozioma na planszy
     * @param y Pozycja pionowa na planszy
     *
     * @details Wartość energetyczna jest losowana z przedziału 10-50.
     * Pozycje powinny być zawarte w wymiarach planszy symulacji.
     */
    public Jedzenie(int x, int y) {
        this.x = x;
        this.y = y;
        this.wartosc = App.random.nextInt(10, 51);
    }
}