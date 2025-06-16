package proj.selekcjanatur;

import java.util.List;
import java.util.Set;

public interface InterfejsSymulacji {
    void aktualizuj();
    List<Czlowiek> getLudzie();
    Set<Jedzenie> getJedzenie();
    boolean czySymulacjaZakonczona();
}
