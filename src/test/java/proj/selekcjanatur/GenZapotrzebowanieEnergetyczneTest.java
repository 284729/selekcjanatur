package proj.selekcjanatur;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenZapotrzebowanieEnergetyczneTest {

    @Test
    void konstruktorZParametremDzialaPoprawnie() {
        // Sprawdza, czy konstruktor ustawia wartość poprawnie
        GenZapotrzebowanieEnergetyczne gen = new GenZapotrzebowanieEnergetyczne(10.0f);
        assertEquals(10.0f, gen.wartosc());
    }

    @Test
    void odziedziczNiePrzekraczaGornegoLimitu() {
        // Sprawdza, czy mutacja nie przekracza górnego limitu (5) – prawdopodobnie błąd w kodzie
        GenZapotrzebowanieEnergetyczne gen = new GenZapotrzebowanieEnergetyczne(4.5f);
        float nowa = gen.odziedzicz().wartosc();
        assertTrue(nowa <= 5.0f); // Zgodnie z kodem: Math.min(5, ...)
    }

    @Test
    void odziedziczTworzyNowyObiekt() {
        // Upewnia się, że odziedziczenie tworzy nową instancję
        GenZapotrzebowanieEnergetyczne gen = new GenZapotrzebowanieEnergetyczne(7.0f);
        assertNotSame(gen, gen.odziedzicz());
    }
}
