package proj.selekcjanatur;

public class GenWzrok implements Gen {
    private final float wartosc;

    public GenWzrok() {
        this.wartosc = App.random.nextInt(1, 11);
    }

    GenWzrok(float wartosc) {
        this.wartosc = wartosc;
    }

    @Override
    public float wartosc() {
        return wartosc;
    }

    @Override
    public Gen odziedzicz() {
        var mutacja = App.random.nextFloat(-1, 1);
        var nowa = Math.max(1, Math.min(10, wartosc + mutacja));
        return new GenWzrok(nowa);
    }
}
