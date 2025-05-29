package proj.selekcjanatur;

public class GenZapotrzebowanieEnergetyczne implements Gen {
    private final float wartosc;

    public GenZapotrzebowanieEnergetyczne() {
        this.wartosc = App.random.nextFloat(5, 20);
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
        var mutacja = App.random.nextFloat(-5, 7);
        var nowa = Math.min(5, wartosc + mutacja);
        return new GenZapotrzebowanieEnergetyczne(nowa);
    }
}
