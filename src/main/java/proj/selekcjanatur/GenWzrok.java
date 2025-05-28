package proj.selekcjanatur;

public class GenWzrok implements Gen {
    private final float wartosc;

    public GenWzrok() {
        this.wartosc = App.random.nextInt(1, 11);
    }

    private GenWzrok(float wartosc) {
        this.wartosc = wartosc;
    }

    @Override
    public float wartosc() {
        return wartosc;
    }

    @Override
    public Gen odziedzicz() {
        float mutacja = (float) (Math.random() * 2 - 1); // -1 do +1
        float nowa = Math.max(1, Math.min(10, wartosc + mutacja));
        return new GenWzrok(nowa);
    }
}
