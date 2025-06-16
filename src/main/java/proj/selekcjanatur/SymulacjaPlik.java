package proj.selekcjanatur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SymulacjaPlik implements InterfejsSymulacji {
    private final List<String> zdarzenia;
    private int index = 0;

    private final List<Czlowiek> ludzie = new ArrayList<>();
    private final Set<Jedzenie> jedzenie = new HashSet<>();

    public SymulacjaPlik(String nazwaPliku) throws IOException {
        this.zdarzenia = Files.readAllLines(Path.of(nazwaPliku));
    }

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

    public boolean czySymulacjaZakonczona() {
        return zdarzenia.size() == index;
    }

    public List<Czlowiek> getLudzie() {
        return ludzie;
    }

    public Set<Jedzenie> getJedzenie() {
        return jedzenie;
    }

    private void dodajCzlowieka(String[] dane) {
        var mezczyzna = Boolean.parseBoolean(dane[2]);
        int x = Integer.parseInt(dane[3]);
        int y = Integer.parseInt(dane[4]);
        int wiek = Integer.parseInt(dane[5]);
        Czlowiek cz = mezczyzna ? new Mezczyzna(x, y) : new Kobieta(x, y);
        cz.wiek = wiek;
        ludzie.add(cz);
    }

    private void dodajCzlowieka(String czyM, String xStr, String yStr) {
        var mezczyzna = Boolean.parseBoolean(czyM);
        int x = Integer.parseInt(xStr);
        int y = Integer.parseInt(yStr);
        Czlowiek cz = mezczyzna ? new Mezczyzna(x, y) : new Kobieta(x, y);
        ludzie.add(cz);
    }

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

    private void usunJedzenie(String[] dane) {
        int x = Integer.parseInt(dane[1]);
        int y = Integer.parseInt(dane[2]);
        jedzenie.removeIf(j -> j.x == x && j.y == y);
    }

    private void usunCzlowieka(String[] dane) {
        var id = dane[1];
        int x = Integer.parseInt(dane[2]);
        int y = Integer.parseInt(dane[3]);
        ludzie.removeIf(cz -> cz.x == x && cz.y == y && cz.toString().equals(id));
    }
}
