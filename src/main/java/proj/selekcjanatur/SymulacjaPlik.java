package proj.selekcjanatur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @file    SymulacjaPlik.java
 * @brief   Implementacja odtwarzania symulacji z pliku dziennika zdarzeń
 */

/**
 * @class   SymulacjaPlik
 * @brief   Klasa odtwarzająca symulację na podstawie zapisanego dziennika zdarzeń
 *
 * Implementuje ten sam interfejs co Symulacja, ale zamiast obliczać nowe stany,
 * odtwarza je sekwencyjnie z pliku. Pozwala to na odtworzenie przebiegu
 * wcześniejszej symulacji krok po kroku.
 */
public class SymulacjaPlik implements InterfejsSymulacji {
    /** @brief Lista wczytanych zdarzeń z pliku */
    private final List<String> zdarzenia;

    /** @brief Aktualny indeks przetwarzanego zdarzenia */
    private int index = 0;

    /** @brief Lista ludzi w odtwarzanej symulacji */
    private final List<Czlowiek> ludzie = new ArrayList<>();

    /** @brief Zbiór jedzenia w odtwarzanej symulacji */
    private final Set<Jedzenie> jedzenie = new HashSet<>();

    /**
     * @brief Konstruktor wczytujący dziennik zdarzeń z pliku
     * @param nazwaPliku Ścieżka do pliku dziennika zdarzeń
     * @throws IOException Jeśli wystąpi błąd odczytu pliku
     */
    public SymulacjaPlik(String nazwaPliku) throws IOException {
        this.zdarzenia = Files.readAllLines(Path.of(nazwaPliku));
    }

    /**
     * @brief Aktualizuje stan symulacji o jeden krok
     * @details Przetwarza kolejne zdarzenia z dziennika aż do napotkania
     * zdarzenia KLATKA, które oznacza koniec aktualnej klatki symulacji.
     */
    @Override
    public void aktualizuj() {
        while (index < zdarzenia.size()) {
            var linia = zdarzenia.get(index++);
            var dane = linia.split(";");

            switch (dane[0]) {
                case "KLATKA":
                    var id = Integer.parseInt(dane[1]);
                    if (id % 3 == 0) {
                        for (var cz : ludzie) cz.wiek++;
                    }
                    return;
                case "DODANIE_CZLOWIEKA":
                    dodajCzlowieka(dane);
                    break;
                case "DODANIE_JEDZENIA":
                    jedzenie.add(new Jedzenie(Integer.parseInt(dane[1]), Integer.parseInt(dane[2])));
                    break;
                case "PRZEMIESZCZENIE":
                    przemiescCzlowieka(dane);
                    break;
                case "ZJEDZONO":
                    usunJedzenie(dane);
                    break;
                case "SMIERC":
                    usunCzlowieka(dane);
                    break;
                case "ROZMNOZENIE":
                    dodajCzlowieka(dane[4], dane[5], dane[6]);
                    break;
            }
        }
    }

    /**
     * @brief Sprawdza czy symulacja została w pełni odtworzona
     * @return true jeśli wszystkie zdarzenia zostały przetworzone
     */
    @Override
    public boolean czySymulacjaZakonczona() {
        return zdarzenia.size() == index;
    }

    /**
     * @brief Zwraca listę ludzi w odtwarzanej symulacji
     * @return Lista obiektów Czlowiek
     */
    @Override
    public List<Czlowiek> getLudzie() {
        return ludzie;
    }

    /**
     * @brief Zwraca zbiór jedzenia w odtwarzanej symulacji
     * @return Zbiór obiektów Jedzenie
     */
    @Override
    public Set<Jedzenie> getJedzenie() {
        return jedzenie;
    }

    /**
     * @brief Dodaje człowieka na podstawie danych z dziennika
     * @param dane Tablica parametrów zdarzenia DODANIE_CZLOWIEKA
     */
    private void dodajCzlowieka(String[] dane) {
        var mezczyzna = Boolean.parseBoolean(dane[2]);
        int x = Integer.parseInt(dane[3]);
        int y = Integer.parseInt(dane[4]);
        int wiek = Integer.parseInt(dane[5]);
        Czlowiek cz = mezczyzna ? new Mezczyzna(x, y) : new Kobieta(x, y);
        cz.wiek = wiek;
        ludzie.add(cz);
    }

    /**
     * @brief Dodaje dziecko powstałe w wyniku rozmnożenia
     * @param czyM Płeć dziecka (true = mężczyzna)
     * @param xStr Pozycja x dziecka
     * @param yStr Pozycja y dziecka
     */
    private void dodajCzlowieka(String czyM, String xStr, String yStr) {
        var mezczyzna = Boolean.parseBoolean(czyM);
        int x = Integer.parseInt(xStr);
        int y = Integer.parseInt(yStr);
        Czlowiek cz = mezczyzna ? new Mezczyzna(x, y) : new Kobieta(x, y);
        ludzie.add(cz);
    }

    /**
     * @brief Przenosi człowieka na nową pozycję
     * @param dane Tablica parametrów zdarzenia PRZEMIESZCZENIE
     */
    private void przemiescCzlowieka(String[] dane) {
        var id = dane[1];
        int staryX = Integer.parseInt(dane[2]);
        int staryY = Integer.parseInt(dane[3]);
        int nowyX = Integer.parseInt(dane[4]);
        int nowyY = Integer.parseInt(dane[5]);

        for (Czlowiek cz : ludzie) {
            if (cz.x == staryX && cz.y == staryY && cz.toString().equals(id)) {
                cz.x = nowyX;
                cz.y = nowyY;
                break;
            }
        }
    }

    /**
     * @brief Usuwa zjedzone jedzenie
     * @param dane Tablica parametrów zdarzenia ZJEDZONO
     */
    private void usunJedzenie(String[] dane) {
        int x = Integer.parseInt(dane[1]);
        int y = Integer.parseInt(dane[2]);
        jedzenie.removeIf(j -> j.x == x && j.y == y);
    }

    /**
     * @brief Usuwa martwego człowieka
     * @param dane Tablica parametrów zdarzenia SMIERC
     */
    private void usunCzlowieka(String[] dane) {
        var id = dane[1];
        int x = Integer.parseInt(dane[2]);
        int y = Integer.parseInt(dane[3]);
        ludzie.removeIf(cz -> cz.x == x && cz.y == y && cz.toString().equals(id));
    }
}