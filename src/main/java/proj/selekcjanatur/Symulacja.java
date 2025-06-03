package proj.selekcjanatur;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Symulacja {
    public static int szerokosc;
    public static int wysokosc;
    public static int liczbaLudzi;
    public static int poczatkoweJedzenie;
    public static int jedzenieNaTick;

    public static void ustawParametry(int w, int h, int l, int j, int jt) {
        szerokosc = w;
        wysokosc = h;
        liczbaLudzi = l;
        poczatkoweJedzenie = j;
        jedzenieNaTick = jt;
    }

    private final int kolumny;
    private final int wiersze;

    private final List<Czlowiek> ludzie = new ArrayList<>();
    private final Set<Jedzenie> jedzenie = new HashSet<>();
    private static final List<String> dziennikZdarzen = new ArrayList<>(); // Dziennik zdarzeń


    private int licznikKlatek = 0;
    private int licznikDodawaniaJedzenia = 0;

    private final boolean[][] zajete;

    private static final List<int[]> KIERUNKI = new ArrayList<>(Arrays.asList(
            new int[]{1, 0},
            new int[]{-1, 0},
            new int[]{0, 1},
            new int[]{0, -1},
            new int[]{1, 1},
            new int[]{-1, -1},
            new int[]{1, -1},
            new int[]{-1, 1}
    ));


    public Symulacja(int kolumny, int wiersze) {
        this.kolumny = kolumny;
        this.wiersze = wiersze;
        this.zajete = new boolean[wiersze][kolumny];

        dodajLosowychLudzi(liczbaLudzi);
        dodajLosoweJedzenie(poczatkoweJedzenie);
    }

    private void dodajDoDziennika(String zdarzenie) {
        dziennikZdarzen.add(zdarzenie);
    }

    public List<Czlowiek> getLudzie() {
        return ludzie;
    }

    public Set<Jedzenie> getJedzenie() {
        return jedzenie;
    }

    public void dodajLosowychLudzi(int ile) {
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
            dodajDoDziennika("Dodano człowieka: " + cz);
        }
    }

    public void dodajLosoweJedzenie(int ile) {
        int dodane = 0;
        while (dodane < ile) {
            int x = App.random.nextInt(kolumny);
            int y = App.random.nextInt(wiersze);
            Jedzenie nowe = new Jedzenie(x, y);
            if (jedzenie.add(nowe)) {
                dodane++;
                dodajDoDziennika("Dodano jedzenie na pozycji: (" + x + ", " + y + ")");
            }
        }
    }

    public void aktualizuj() {
        // Aktualizacja stanu tylko co 3 klatki
        licznikKlatek++;
        if (licznikKlatek >= 3) {
            aktualizujStan();
            licznikKlatek = 0;
        }

        // Wykonaj pojedynczy krok dla każdego człowieka
        for (Czlowiek cz : ludzie) {
            if (cz.zywy && cz.powinnienSieRuszyc(licznikKlatek)) {
                wykonajRuch(cz);
            }
        }
    }

    private void aktualizujStan() {
        // Dodawanie jedzenia co pewien czas
        licznikDodawaniaJedzenia++;
        if (licznikDodawaniaJedzenia >= 3) {
            dodajLosoweJedzenie(jedzenieNaTick);
            licznikDodawaniaJedzenia = 0;
        }

        // Aktualizacja stanu ludzi
        for (Czlowiek cz : ludzie) {
            if (cz.zywy) {
                cz.aktualizuj();
            }
        }

        // Rozmnażanie
        List<Czlowiek> nowi = new ArrayList<>();
        Iterator<Czlowiek> it = ludzie.iterator();
        while (it.hasNext()) {
            Czlowiek cz = it.next();
            if (!cz.zywy) {
                it.remove();
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

                        dodajDoDziennika("Rozmnożenie: " + cz + " i " + inny + " -> " + dziecko);
                        break;
                    }
                }
            }
        }
        ludzie.addAll(nowi);
    }

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

    private Czlowiek znajdzPartnera(Czlowiek cz) {
        return ludzie.stream()
                .filter(p -> p != cz && p.zywy && p.mozeRozmnazac() && cz.czyMezczyzna() != p.czyMezczyzna())
                .min(Comparator.comparingInt(p -> Math.abs(p.x - cz.x) + Math.abs(p.y - cz.y)))
                .filter(p -> Math.abs(p.x - cz.x) + Math.abs(p.y - cz.y) <= cz.zasiegWzroku())
                .orElse(null);
    }

    private Jedzenie znajdzNajblizszeJedzenie(Czlowiek cz) {
        return jedzenie.stream()
                .min(Comparator.comparingInt(j -> Math.abs(j.x - cz.x) + Math.abs(j.y - cz.y)))
                .filter(j -> Math.abs(j.x - cz.x) + Math.abs(j.y - cz.y) <= cz.zasiegWzroku())
                .orElse(null);
    }

    private Jedzenie znajdzJedzenieNaPolu(int x, int y) {
        for (Jedzenie j : jedzenie) {
            if (j.x == x && j.y == y) return j;
        }
        return null;
    }

    private void przemiescSieNaPole(Czlowiek cz, int x, int y) {
        zajete[cz.y][cz.x] = false;
        cz.x = x;
        cz.y = y;
        zajete[y][x] = true;

        dodajDoDziennika("Człowiek " + cz + " przemieścił się na pozycję: (" + x + ", " + y + ")");

        Jedzenie jedzenie = znajdzJedzenieNaPolu(x, y);
        if (jedzenie != null) {
            cz.zjedz(jedzenie);
            this.jedzenie.remove(jedzenie);
        }
    }
    public static void zapiszDziennikDoPliku(String nazwaPliku) {
        try {
            Files.write(Path.of(nazwaPliku), dziennikZdarzen);
            System.out.println("Dziennik zdarzeń zapisano do pliku: " + nazwaPliku);
        } catch (IOException e) {
            System.err.println("Nie udało się zapisać dziennika zdarzeń do pliku: " + e.getMessage());
        }
    }
    public boolean czySymulacjaZakonczona() {
        return ludzie.isEmpty();
    }
}
