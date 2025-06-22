package proj.selekcjanatur;

import java.util.List;
import java.util.Set;

/**
 * @file    InterfejsSymulacji.java
 * @brief   Interfejs definiujący podstawowe operacje symulacji ewolucyjnej
 */

/**
 * @interface InterfejsSymulacji
 * @brief   Główny interfejs systemu symulacji ewolucyjnej
 *
 * Definiuje kontrakt dla wszystkich implementacji symulacji,
 * zarówno działających w czasie rzeczywistym jak i odtwarzających
 * z zapisanych danych. Interfejs jest implementowany przez:
 * - Klasę Symulacja (podstawowa implementacja)
 * - Klasę SymulacjaPlik (odtwarzanie z dziennika zdarzeń)
 */
public interface InterfejsSymulacji {
    /**
     * @brief Aktualizuje stan symulacji o jeden krok
     * @details Metoda powinna:
     * - Wykonać wszystkie obliczenia dla bieżącego cyklu
     * - Zaktualizować stan wszystkich obiektów
     * - Sprawdzić warunki zakończenia symulacji
     */
    void aktualizuj();

    /**
     * @brief Pobiera listę wszystkich ludzi w symulacji
     * @return Lista obiektów Czlowiek
     * @details Zwraca zarówno żywych jak i martwych uczestników symulacji.
     * Kolejność na liście może mieć znaczenie dla renderowania.
     */
    List<Czlowiek> getLudzie();

    /**
     * @brief Pobiera zbiór dostępnego jedzenia
     * @return Nieuporządkowany zbiór obiektów Jedzenie
     * @details Zwraca tylko jedzenie obecne w bieżącym cyklu symulacji.
     * Jedzenie zjedzone w poprzednich cyklach nie jest uwzględniane.
     */
    Set<Jedzenie> getJedzenie();

    /**
     * @brief Sprawdza warunki zakończenia symulacji
     * @return true jeśli symulacja powinna się zakończyć
     * @details Typowe warunki zakończenia:
     * - Wyczerpanie się populacji (brak żywych ludzi)
     * - Osiągnięcie maksymalnej liczby cykli
     * - Odtworzenie wszystkich zdarzeń z pliku (w trybie odtwarzania)
     */
    boolean czySymulacjaZakonczona();

    boolean czySymulacjaZakonczena();
}