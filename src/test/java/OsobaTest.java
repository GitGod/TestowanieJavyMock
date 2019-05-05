import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class OsobaTest {
    private IOsoba osobaRepositoryMock;
    private OsobaMetody osobaMetody;

    @BeforeEach
    public void initEach() {
        osobaRepositoryMock = new FakeMockOsobaRepository();
        osobaMetody = new OsobaMetody(osobaRepositoryMock);
    }

    @Test
    public void dodaj_osobe_true() {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        osobaMetody.addOsoba(osoba);
        assertThat(osobaMetody.getOsobaById(1)).isEqualTo(osoba);
    }
    @Test
    public void dodaj_osobe_false_zajete_id() {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        osobaMetody.addOsoba(osoba);
        boolean result = osobaMetody.addOsoba(osoba);
        assertThat(result).isFalse();
    }
    @Test
    public void dodaj_osobe_notvalid_imie() {
        Osoba osoba = new Osoba(1,"michal","michal@o2.pl");
        assertThatThrownBy(() -> osobaMetody.addOsoba(osoba))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    public void dodaj_osobe_notvalid_email() {
        Osoba osoba = new Osoba(1,"michal","michalo2.pl");
        assertThatThrownBy(() -> osobaMetody.addOsoba(osoba))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void zaktualizuj_osobe_ktorej_nie_ma_false() {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        boolean result = osobaMetody.updateOsoba(osoba);
        assertThat(result).isFalse();
    }


    @Test
    public void zaktualizuj_osobe_true() {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        osobaMetody.addOsoba(osoba);
        osoba.setImie("Marcin");
        osobaMetody.updateOsoba(osoba);
        String result = osobaMetody.getOsobaById(1).getImie();
        assertThat(result).isEqualTo("Marcin");
    }
    @Test
    public void deleteClient_WhenClientDoesNotExist_ReturnsFalse() {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        boolean result = osobaMetody.deleteOsoba(osoba);
        assertThat(result).isFalse();
    }


    @Test
    public void deleteClient_WhenClientDoesExist_ClientIsDeleted() {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        osobaMetody.addOsoba(osoba);
        osobaMetody.deleteOsoba(osoba);
        assertThat(osobaMetody.getAllOsoba()).isEqualTo(new ArrayList<Osoba>());
    }

}
