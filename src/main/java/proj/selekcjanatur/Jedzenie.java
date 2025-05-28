package proj.selekcjanatur;

public class Jedzenie {
    public int x, y;
    public int wartosc;

    public Jedzenie(int x, int y) {
        this.x = x;
        this.y = y;
        this.wartosc = App.random.nextInt(10, 51);
    }
}
