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
        float mutacja = (float) (Math.random() * 1.5 - 0.75); // -0.75 do +0.75
        float nowa = Math.max(1, Math.min(3, wartosc + mutacja));
        return new GenPredkoscChodzenia(nowa);
    }
}
