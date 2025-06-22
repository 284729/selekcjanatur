package proj.selekcjanatur;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @file    Symulacja.java
 * @brief   Główna klasa implementująca logikę symulacji ewolucyjnej
 */

/**
 * @class   Symulacja
 * @brief   Implementacja symulacji ewolucyjnej z dziedziczeniem cech
 *
 * Klasa zarządza całym przebiegiem symulacji, w tym:
 * - Inicjalizacją populacji i środowiska
 * - Mechaniką rozmnażania i dziedziczenia cech
 * - Ruchem i interakcjami między organizmami
 * - Systemem dziennika zdarzeń
 * - Warunkami zakończenia symulacji
 */
public abstract class Symulacja implements InterfejsSymulacji {
    /** @brief Statyczne parametry symulacji - szerokość planszy */
    public static int szerokosc;

    /** @brief Statyczne parametry symulacji - wysokość planszy */
    public static int wysokosc;

    /** @brief Statyczne parametry symulacji - początkowa liczba ludzi */
    public static int liczbaLudzi;

    /** @brief Statyczne parametry symulacji - początkowa ilość jedzenia */
    public static int poczatkoweJedzenie;

    /** @brief Statyczne parametry symulacji - nowe jedzenie na cykl */
    public static int jedzenieNaTick;

    /**
     * @brief Ustawia globalne parametry symulacji
     * @param w Szerokość planszy
     * @param h Wysokość planszy
     * @param l Początkowa liczba ludzi
     * @param j Początkowa ilość jedzenia
     * @param jt Ilość nowego jedzenia dodawanego co cykl
     */
    public static void ustawParametry(int w, int h, int l, int j, int jt) {
        szerokosc = w;
        wysokosc = h;
        liczbaLudzi = l;
        poczatkoweJedzenie = j;
        jedzenieNaTick = jt;
    }

    /** @brief Liczba kolumn planszy (szerokość) */
    private final int kolumny;

    /** @brief Liczba wierszy planszy (wysokość) */
    private final int wiersze;

    /** @brief Lista wszystkich ludzi w symulacji */
    private final List<Czlowiek> ludzie = new ArrayList<>();

    /** @brief Zbiór dostępnego jedzenia na planszy */
    private final Set<Jedzenie> jedzenie = new HashSet<>();

    /** @brief Dziennik zdarzeń zapisywany do pliku */
    private static final List<String> dziennikZdarzen = new ArrayList<>();

    /** @brief Licznik klatek symulacji */
    private int licznikKlatek = 0;

    /** @brief Licznik kontrolujący dodawanie nowego jedzenia */
    private int licznikDodawaniaJedzenia = 0;

    /** @brief Mapa zajętości pól na planszy */
    private final boolean[][] zajete;

    /** @brief Lista możliwych kierunków ruchu */
    private static final List<int[]> KIERUNKI = new ArrayList<>(Arrays.asList(
            new int[]{1, 0},   // prawo
            new int[]{-1, 0},  // lewo
            new int[]{0, 1},   // dół
            new int[]{0, -1},  // góra
            new int[]{1, 1},   // prawo-dół
            new int[]{-1, -1}, // lewo-góra
            new int[]{1, -1},  // prawo-góra
            new int[]{-1, 1}   // lewo-dół
    ));

    /**
     * @brief Konstruktor inicjalizujący symulację
     * @param kolumny Szerokość planszy
     * @param wiersze Wysokość planszy
     */
    public Symulacja(int kolumny, int wiersze) {
        this.kolumny = kolumny;
        this.wiersze = wiersze;
        this.zajete = new boolean[wiersze][kolumny];

        dodajDoDziennika("ROZMIAR;" + kolumny + ";" + wiersze);
        dodajLosowychLudzi(liczbaLudzi);
        dodajLosoweJedzenie(poczatkoweJedzenie);
    }

    /**
     * @brief Dodaje wpis do dziennika zdarzeń
     * @param zdarzenie Tekstowy opis zdarzenia
     */
    private void dodajDoDziennika(String zdarzenie) {
        dziennikZdarzen.add(zdarzenie);
    }

    /**
     * @brief Zwraca listę ludzi w symulacji
     * @return Lista obiektów Czlowiek
     */
    @Override
    public List<Czlowiek> getLudzie() {
        return ludzie;
    }

    /**
     * @brief Zwraca zbiór jedzenia w symulacji
     * @return Zbiór obiektów Jedzenie
     */
    @Override
    public Set<Jedzenie> getJedzenie() {
        return jedzenie;
    }

    /**
     * @brief Dodaje losowych ludzi na planszę
     * @param ile Liczba ludzi do dodania
     */
    private void dodajLosowychLudzi(int ile) {
        for (int i = 0; i < ile; i++) {
            Czlowiek cz;
            int x = App.random.nextInt(kolumny);
            int y = App.random.nextInt(wiersze);
            if (App.random.nextBoolean()) {
                cz = new Mezczyzna(x, y);
            } else {
                cz = new Kobieta(x, y);
            }
            cz.wiek = App.random.nextInt(18, 51);
            ludzie.add(cz);
            zajete[cz.y][cz.x] = true;
            dodajDoDziennika("DODANIE_CZLOWIEKA;" + cz + ";" + cz.czyMezczyzna() + ";" + x + ";" + y + ";" + cz.wiek);
        }
    }

    /**
     * @brief Dodaje losowe jedzenie na planszę
     * @param ile Liczba jedzenia do dodania
     */
    private void dodajLosoweJedzenie(int ile) {
        int dodane = 0;
        while (dodane < ile) {
            int x = App.random.nextInt(kolumny);
            int y = App.random.nextInt(wiersze);
            Jedzenie nowe = new Jedzenie(x, y);
            if (jedzenie.add(nowe)) {
                dodane++;
                dodajDoDziennika("DODANIE_JEDZENIA;" + x + ";" + y);
            }
        }
    }

    /**
     * @brief Aktualizuje stan symulacji o jeden krok
     * @details Wykonuje:
     * - Aktualizację stanu co 3 klatki
     * - Ruch ludzi zgodnie z ich predyspozycjami
     * - Dodawanie nowego jedzenia
     * - Rozmnażanie i usuwanie martwych
     */
    @Override
    public void aktualizuj() {
        dodajDoDziennika("KLATKA;" + licznikKlatek);
        licznikKlatek++;
        if (licznikKlatek % 3 == 0) {
            aktualizujStan();
        }

        for (Czlowiek cz : ludzie) {
            if (cz.zywy && cz.powinnienSieRuszyc(licznikKlatek)) {
                wykonajRuch(cz);
            }
        }
    }

    /**
     * @brief Aktualizuje stan symulacji (co 3 klatki)
     * @details Wykonuje:
     * - Dodawanie nowego jedzenia
     * - Aktualizację stanu ludzi
     * - Rozmnażanie i usuwanie martwych
     */
    private void aktualizujStan() {
        licznikDodawaniaJedzenia++;
        if (licznikDodawaniaJedzenia >= 3) {
            dodajLosoweJedzenie(jedzenieNaTick);
            licznikDodawaniaJedzenia = 0;
        }

        for (Czlowiek cz : ludzie) {
            if (cz.zywy) {
                cz.aktualizuj();
            }
        }

        List<Czlowiek> nowi = new ArrayList<>();
        Iterator<Czlowiek> it = ludzie.iterator();
        while (it.hasNext()) {
            Czlowiek cz = it.next();
            if (!cz.zywy) {
                it.remove();
                zajete[cz.y][cz.x] = false;
                dodajDoDziennika("SMIERC;" + cz + ";" + cz.x + ";" + cz.y + ";" + cz.wiek);
                continue;
            }

            if (cz.mozeRozmnazac()) {
                for (Czlowiek inny : ludzie) {
                    if (inny == cz || !inny.zywy || !inny.mozeRozmnazac()) continue;
                    if (cz.czyMezczyzna() == inny.czyMezczyzna()) continue;

                    if (Math.abs(cz.x - inny.x) <= 1 && Math.abs(cz.y - inny.y) <= 1) {
                        Czlowiek dziecko = cz.rozmnazajZ(inny);
                        nowi.add(dziecko);
                        cz.poziomGlodu *= 0.5f;
                        inny.poziomGlodu *= 0.5f;
                        cz.czasOdRozmnazania = 0;
                        inny.czasOdRozmnazania = 0;

                        dodajDoDziennika("ROZMNOZENIE;" + cz + ";" + inny + ";"
                                + dziecko + ";" + dziecko.czyMezczyzna() + ";" + dziecko.x + ";" + dziecko.y);

                        break;
                    }
                }
            }
        }
        ludzie.addAll(nowi);
    }

    /**
     * @brief Wykonuje ruch dla danego człowieka
     * @param cz Człowiek, który ma się poruszyć
     */
    private void wykonajRuch(Czlowiek cz) {
        Czlowiek partner = znajdzPartnera(cz);
        Jedzenie jedzenie = (partner == null) ? znajdzNajblizszeJedzenie(cz) : null;

        int dx = 0, dy = 0;
        if (partner != null) {
            dx = Integer.compare(partner.x, cz.x);
            dy = Integer.compare(partner.y, cz.y);
        } else if (jedzenie != null) {
            dx = Integer.compare(jedzenie.x, cz.x);
            dy = Integer.compare(jedzenie.y, cz.y);
        }

        int nowyX = Math.max(0, Math.min(kolumny - 1, cz.x + dx));
        int nowyY = Math.max(0, Math.min(wiersze - 1, cz.y + dy));

        if (!zajete[nowyY][nowyX]) {
            przemiescSieNaPole(cz, nowyX, nowyY);
        } else {
            Collections.shuffle(KIERUNKI);
            for (int[] dir : KIERUNKI) {
                int sx = cz.x + dir[0];
                int sy = cz.y + dir[1];
                if (sx >= 0 && sx < kolumny && sy >= 0 && sy < wiersze && !zajete[sy][sx]) {
                    przemiescSieNaPole(cz, sx, sy);
                    break;
                }
            }
        }
    }

    /**
     * @brief Znajduje partnera do rozmnażania w zasięgu wzroku
     * @param cz Człowiek szukający partnera
     * @return Znaleziony partner lub null
     */
    private Czlowiek znajdzPartnera(Czlowiek cz) {
        return ludzie.stream()
                .filter(p -> p != cz && p.zywy && p.mozeRozmnazac() && cz.czyMezczyzna() != p.czyMezczyzna())
                .min(Comparator.comparingInt(p -> Math.abs(p.x - cz.x) + Math.abs(p.y - cz.y)))
                .filter(p -> Math.abs(p.x - cz.x) + Math.abs(p.y - cz.y) <= cz.zasiegWzroku())
                .orElse(null);
    }

    /**
     * @brief Znajduje najbliższe jedzenie w zasięgu wzroku
     * @param cz Człowiek szukający jedzenia
     * @return Znalezione jedzenie lub null
     */
    private Jedzenie znajdzNajblizszeJedzenie(Czlowiek cz) {
        return jedzenie.stream()
                .min(Comparator.comparingInt(j -> Math.abs(j.x - cz.x) + Math.abs(j.y - cz.y)))
                .filter(j -> Math.abs(j.x - cz.x) + Math.abs(j.y - cz.y) <= cz.zasiegWzroku())
                .orElse(null);
    }

    /**
     * @brief Sprawdza czy na danym polu jest jedzenie
     * @param x Pozycja x
     * @param y Pozycja y
     * @return Obiekt jedzenia lub null
     */
    private Jedzenie znajdzJedzenieNaPolu(int x, int y) {
        for (Jedzenie j : jedzenie) {
            if (j.x == x && j.y == y) return j;
        }
        return null;
    }

    /**
     * @brief Przenosi człowieka na nowe pole
     * @param cz Człowiek do przeniesienia
     * @param x Nowa pozycja x
     * @param y Nowa pozycja y
     */
    private void przemiescSieNaPole(Czlowiek cz, int x, int y) {
        zajete[cz.y][cz.x] = false;
        dodajDoDziennika("PRZEMIESZCZENIE;" + cz + ";" + cz.x + ";" + cz.y + ";" + x + ";" + y);

        cz.x = x;
        cz.y = y;
        zajete[y][x] = true;

        Jedzenie jedzenie = znajdzJedzenieNaPolu(x, y);
        if (jedzenie != null) {
            cz.zjedz(jedzenie);
            this.jedzenie.remove(jedzenie);
            dodajDoDziennika("ZJEDZONO;" + x + ";" + y);
        }
    }

    /**
     * @brief Zapisuje dziennik zdarzeń do pliku
     * @param nazwaPliku Nazwa pliku do zapisu
     */
    public static void zapiszDziennikDoPliku(String nazwaPliku) {
        if (dziennikZdarzen.isEmpty()) return;
        try {
            Files.write(Path.of(nazwaPliku), dziennikZdarzen);
            System.out.println("Dziennik zdarzeń zapisano do pliku: " + nazwaPliku);
        } catch (IOException e) {
            System.err.println("Nie udało się zapisać dziennika zdarzeń do pliku: " + e.getMessage());
        }
    }

    /**
     * @brief Sprawdza czy symulacja powinna się zakończyć
     * @return true jeśli nie ma żywych ludzi
     */
    @Override
    public boolean czySymulacjaZakonczena() {
        return ludzie.isEmpty();
    }
}