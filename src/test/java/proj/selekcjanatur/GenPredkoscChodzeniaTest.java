package proj.selekcjanatur;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenPredkoscChodzeniaTest {

    @Test
    void konstruktorZWartosciaDzialaPoprawnie() {
        GenPredkoscChodzenia gen = new GenPredkoscChodzenia(2.5f);
        assertEquals(2.5f, gen.wartosc());
    }

    @Test
    void odziedziczGenerujeNowyObiektWZakresie() {
        GenPredkoscChodzenia gen = new GenPredkoscChodzenia(2.0f);
        Gen odziedziczony = gen.odziedzicz();
        float w = odziedziczony.wartosc();
        assertTrue(w >= 1.0f && w <= 3.0f);
    }

    @Test
    void odziedziczNieZwracaTegoSamegoObiektu() {
        GenPredkoscChodzenia gen = new GenPredkoscChodzenia(2.0f);
        Gen odziedziczony = gen.odziedzicz();
        assertNotSame(gen, odziedziczony);
    }
}
