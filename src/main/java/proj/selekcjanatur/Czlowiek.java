package proj.selekcjanatur;

/**
 * @file    Czlowiek.java
 * @brief   Abstrakcyjna klasa bazowa reprezentująca człowieka w symulacji
 */

/**
 * @class   Czlowiek
 * @brief   Abstrakcyjna klasa reprezentująca człowieka w symulacji ewolucyjnej
 *
 * Klasa zawiera podstawowe mechanizmy:
 * - Śledzenie stanu życiowego
 * - Zarządzanie głodem i energią
 * - Mechanizmy rozmnażania
 * - Dziedziczenie genów
 * - Starzenie się i śmierć
 */
public abstract class Czlowiek {
    /** @brief Pozycja x na planszy */
    public int x;

    /** @brief Pozycja y na planszy */
    public int y;

    /** @brief Flaga określająca czy człowiek jest żywy */
    protected boolean zywy = true;

    /** @brief Poziom głodu (0-100) */
    protected float poziomGlodu = 100;

    /** @brief Liczba cykli od ostatniego rozmnożenia */
    protected int czasOdRozmnazania = 20;

    /** @brief Tablica genów człowieka (wzrok, prędkość, metabolizm) */
    protected Gen[] geny = new Gen[3];

    /** @brief Licznik globalny do generowania unikalnych ID */
    private static int globalneId = 0;

    /** @brief Unikalny identyfikator człowieka */
    public final int id;

    /**
     * @brief Konstruktor tworzący nowego człowieka
     * @param x Pozycja startowa x
     * @param y Pozycja startowa y
     */
    public Czlowiek(int x, int y) {
        this.x = x;
        this.y = y;
        this.geny[0] = new GenWzrok();
        this.geny[1] = new GenPredkoscChodzenia();
        this.geny[2] = new GenZapotrzebowanieEnergetyczne();

        this.id = globalneId++;
    }

    /** @brief Wiek człowieka w cyklach symulacji */
    protected int wiek = 0;

    /**
     * @brief Sprawdza czy człowiek jest dzieckiem
     * @return true jeśli wiek < 18 cykli
     */
    public boolean jestDzieckiem() {
        return wiek < 18;
    }

    /**
     * @brief Oblicza zasięg wzroku na podstawie genów
     * @return Zasięg wzroku w komórkach
     */
    public int zasiegWzroku() {
        return Math.round(geny[0].wartosc());
    }

    /**
     * @brief Oblicza prędkość poruszania na podstawie genów
     * @return Prędkość (1-3)
     */
    public int predkosc() {
        return Math.round(geny[1].wartosc());
    }

    /**
     * @brief Oblicza zużycie energii na cykl
     * @return Wartość zużycia energii
     */
    public float zuzycieEnergii() {
        return geny[2].wartosc();
    }

    /**
     * @brief Aktualizuje stan człowieka w każdym cyklu symulacji
     *
     * Zwiększa wiek, zmniejsza poziom energii,
     * sprawdza warunki śmierci z głodu i starości
     */
    public void aktualizuj() {
        wiek++;
        poziomGlodu -= zuzycieEnergii();
        czasOdRozmnazania++;

        if (poziomGlodu <= 0) {
            zywy = false;
        } else if (czyUmieraZeStarosci()) {
            zywy = false;
        }
    }

    /**
     * @brief Decyduje czy człowiek powinien się poruszyć w danej klatce
     * @param klatki Numer klatki symulacji
     * @return true jeśli powinien się poruszyć
     */
    public boolean powinnienSieRuszyc(int klatki) {
        return switch (predkosc()) {
            case 1 -> klatki % 3 == 0;
            case 2 -> klatki % 2 == 0;
            case 3 -> true;
            default -> false;
        };
    }

    /**
     * @brief Sprawdza możliwość rozmnożenia
     * @return true jeśli może się rozmnożyć
     */
    public abstract boolean mozeRozmnazac();

    /**
     * @brief Zjada podane jedzenie
     * @param jedzenie Obiekt jedzenia do spożycia
     */
    public void zjedz(Jedzenie jedzenie) {
        poziomGlodu += jedzenie.wartosc;
    }

    /**
     * @brief Sprawdza płeć człowieka
     * @return true jeśli to mężczyzna
     */
    public abstract boolean czyMezczyzna();

    /**
     * @brief Sprawdza czy człowiek umiera ze starości
     * @return true jeśli umiera
     *
     * @details Szansa rośnie liniowo od wieku 70 cykli:
     * - 70 cykli: 0% szansy
     * - 100 cykli: 30% szansy
     * - 170 cykli: 100% szansy
     */
    public boolean czyUmieraZeStarosci() {
        if (wiek < 70) return false;
        float szansa = (wiek - 70) * 0.01f;
        return App.random.nextFloat() < szansa;
    }

    /**
     * @brief Rozmnaża się z innym człowiekiem
     * @param inny Drugi rodzic
     * @return Nowy człowiek (dziecko)
     *
     * @details Dziecko dziedziczy losowo geny od rodziców,
     * powstaje w pozycji pierwszego rodzica
     */
    public Czlowiek rozmnazajZ(Czlowiek inny) {
        Czlowiek dziecko = this instanceof Mezczyzna
                ? new Mezczyzna(x, y)
                : new Kobieta(x, y);

        for (int i = 0; i < geny.length; i++) {
            dziecko.geny[i] = App.random.nextBoolean()
                    ? this.geny[i].odziedzicz()
                    : inny.geny[i].odziedzicz();
        }

        return dziecko;
    }

    /**
     * @brief Reprezentacja tekstowa człowieka
     * @return String w formacie "Klasa@ID"
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + id;
    }
}