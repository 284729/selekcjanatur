package proj.selekcjanatur;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenWzrokTest {

    @Test
    void odziedziczZwracaNowyObiektWZakresie() {
        // Test sprawdza, czy odziedziczony gen ma wartość w dozwolonym zakresie (1–10)
        GenWzrok gen = new GenWzrok(5.0f);
        Gen odziedziczony = gen.odziedzicz();
        float w = odziedziczony.wartosc();
        assertTrue(w >= 1.0f && w <= 10.0f);
    }

    @Test
    void wartoscZwracaPoprawnaWartosc() {
        // Sprawdza, czy getter działa poprawnie
        GenWzrok gen = new GenWzrok(7.5f);
        assertEquals(7.5f, gen.wartosc());
    }

    @Test
    void odziedziczTworzyNowyObiekt() {
        // Upewnia się, że odziedziczony gen to nowa instancja
        GenWzrok gen = new GenWzrok(3.0f);
        assertNotSame(gen, gen.odziedzicz());
    }
}
