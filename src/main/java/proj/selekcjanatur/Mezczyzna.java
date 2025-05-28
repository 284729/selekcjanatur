package proj.selekcjanatur;

public class Mezczyzna extends Czlowiek {

    public Mezczyzna(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean czyMezczyzna() {
        return true;
    }

    @Override
    public boolean mozeRozmnazac() {
        if (jestDzieckiem()) return false;
        return poziomGlodu > 60 && czasOdRozmnazania > 10;
    }
}
