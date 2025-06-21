package proj.selekcjanatur;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JedzenieTest {

    @Test
    void konstruktorPoprawnieUstawiaPozycje() {
        // Sprawdza, czy x i y są ustawione poprawnie
        Jedzenie j = new Jedzenie(3, 7);
        assertEquals(3, j.x);
        assertEquals(7, j.y);
    }

    @Test
    void wartoscJestWZakresie() {
        // Sprawdza, czy wartość jedzenia mieści się w zakresie 10–50
        for (int i = 0; i < 100; i++) {
            Jedzenie j = new Jedzenie(0, 0);
            assertTrue(j.wartosc >= 10 && j.wartosc <= 50);
        }
    }
}
