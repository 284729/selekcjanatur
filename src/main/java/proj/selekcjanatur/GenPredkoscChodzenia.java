package proj.selekcjanatur;

public class GenPredkoscChodzenia implements Gen {
    private final float wartosc;

    public GenPredkoscChodzenia() {
        this.wartosc = App.random.nextInt(1, 4);
    }

    public GenPredkoscChodzenia(float wartosc) {
        this.wartosc = wartosc;
    }

    @Override
    public float wartosc() {
        return wartosc;
    }

    @Override
    public Gen odziedzicz() {
        var mutacja = App.random.nextFloat(-0.75f, 0.75f);
        var nowa = Math.max(1, Math.min(3, wartosc + mutacja));
        return new GenPredkoscChodzenia(nowa);
    }
}
