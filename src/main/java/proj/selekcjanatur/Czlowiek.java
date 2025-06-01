package proj.selekcjanatur;

public abstract class Czlowiek {
    public int x, y;
    protected boolean zywy = true;
    protected float poziomGlodu = 100;
    protected int czasOdRozmnazania = 20;

    protected Gen[] geny = new Gen[3];

    public Czlowiek(int x, int y) {
        this.x = x;
        this.y = y;
        this.geny[0] = new GenWzrok();
        this.geny[1] = new GenPredkoscChodzenia();
        this.geny[2] = new GenZapotrzebowanieEnergetyczne();
    }

    protected int wiek = 0; // liczba cykli od urodzenia

    public boolean jestDzieckiem() {
        return wiek < 18;
    }

    public int zasiegWzroku() {
        return Math.round(geny[0].wartosc());
    }

    public int predkosc() {
        return Math.round(geny[1].wartosc());
    }

    public float zuzycieEnergii() {
        return geny[2].wartosc();
    }

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

    public boolean powinnienSieRuszyc(int klatki) {
        // Prędkość określa jak często się porusza:
        // 1 - co 3 klatki, 2 - co 2, 3 - co 1
        return switch (predkosc()) {
            case 1 -> klatki % 3 == 0;
            case 2 -> klatki % 2 == 0;
            case 3 -> true;
            default -> false;
        };
    }

    public abstract boolean mozeRozmnazac();

    public void zjedz(Jedzenie jedzenie) {
        poziomGlodu += jedzenie.wartosc;
    }

    public abstract boolean czyMezczyzna();

    public boolean czyUmieraZeStarosci() {
        if (wiek < 70) return false;

        // Szansa rośnie z wiekiem
        float szansa = (wiek - 70) * 0.01f;

        return App.random.nextFloat() < szansa;
    }

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
}
