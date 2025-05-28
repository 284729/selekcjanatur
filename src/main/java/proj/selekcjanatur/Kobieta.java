package proj.selekcjanatur;

public class Kobieta extends Czlowiek {

    public Kobieta(int x, int y) {
        super(x, y);

        this.geny[1] = new GenPredkoscChodzenia(App.random.nextInt(1, 3));
    }

    @Override
    public boolean czyMezczyzna() {
        return false;
    }

    @Override
    public boolean mozeRozmnazac() {
        if (jestDzieckiem()) return false;
        return poziomGlodu > 70 && czasOdRozmnazania > 20;
    }
}
