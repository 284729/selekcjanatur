package proj.selekcjanatur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SymulacjaTest {

    @Test
    void testUstawParametry() {
        // Testujemy, czy ustawienie parametrów działa poprawnie
        Symulacja.ustawParametry(10, 10, 5, 20, 2);
        assertEquals(10, Symulacja.szerokosc);
        assertEquals(10, Symulacja.wysokosc);
        assertEquals(5, Symulacja.liczbaLudzi);
        assertEquals(20, Symulacja.poczatkoweJedzenie);
        assertEquals(2, Symulacja.jedzenieNaTick);
    }

    @Test
    void testSymulacjaConstructor() {
        // Testujemy, czy konstruktor symulacji działa poprawnie
        Symulacja symulacja = new Symulacja(5, 5);
        assertNotNull(symulacja);
    }
    @Test
    void testDodanieLudziIJedzenia() {
        // Testujemy, czy po ustawieniu parametrów i utworzeniu symulacji
        Symulacja.ustawParametry(10, 10, 3, 5, 1);
        Symulacja symulacja = new Symulacja(10, 10);
        assertEquals(3, symulacja.getLudzie().size());
        assertEquals(5, symulacja.getJedzenie().size());
    }

    @Test
    void testAktualizacjiSymulacji() {
        // Testujemy, czy po aktualizacji liczba ludzi nie jest ujemna
        Symulacja.ustawParametry(5, 5, 2, 2, 1);
        Symulacja symulacja = new Symulacja(5, 5);
        int licznikPrzed = symulacja.getLudzie().get(0).wiek;
        symulacja.aktualizuj();
        assertTrue(symulacja.getLudzie().size() >= 0);
    }

    @Test
    void testCzySymulacjaZakonczonaGdyBrakLudzi() {
        // Testujemy, czy symulacja kończy się, gdy nie ma ludzi
        Symulacja.ustawParametry(5, 5, 0, 2, 1);
        Symulacja symulacja = new Symulacja(5, 5);
        assertTrue(symulacja.czySymulacjaZakonczona());
    }

}