package proj.selekcjanatur;

public class GenZapotrzebowanieEnergetyczne implements Gen {
    private final float wartosc;

    public GenZapotrzebowanieEnergetyczne() {
        this.wartosc = losuj(5, 20);
    }

    public GenZapotrzebowanieEnergetyczne(float wartosc) {
        this.wartosc = wartosc;
    }

    @Override
    public float wartosc() {
        return wartosc;
    }

    @Override
    public Gen odziedzicz() {
        float mutacja = losuj(-5, 7);
        float nowa = Math.min(5, wartosc + mutacja);
        return new GenZapotrzebowanieEnergetyczne(nowa);
    }

    private static float losuj(float min, float max) {
        return min + App.random.nextFloat() * (max - min);
    }
}
