import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.mail.MessagingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class RezerwacjeMockitoTest {

    private IRezerwacje orderRepoMock;
    private RezerwacjaMetody rezerwacjaMetody;
    private Restauracja restauracja;
    private Rezerwacja rezerwacyja;

    @BeforeEach
    public void initEach() throws IOException {
        orderRepoMock = Mockito.mock(IRezerwacje.class);
        rezerwacjaMetody = new RezerwacjaMetody(orderRepoMock);
        restauracja = new Restauracja();
        restauracja.readFile("Restauracja.txt");
        rezerwacyja = new Rezerwacja(1000, LocalDate.of(2022, 1, 17), "10", "11", restauracja.stoliki.get(0));
    }

    @Test
    public void wypisanie_rezerwacji_pusta_lista() {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        List<String> expected = new ArrayList<String>();
        doReturn(expected).when(orderRepoMock).WypiszKonkretnego(osoba);

        assertThat(rezerwacjaMetody.WypiszKonkretnego(osoba)).isEqualTo(expected);
    }
    @Test
    public void wypisanie_rezerwacji_null_osoba_exception() {
        Osoba osoba = null;
        assertThatThrownBy(() -> rezerwacjaMetody.WypiszKonkretnego(osoba))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    public void wypisanie_rezerwacji_zwroc_liste() throws IOException, MessagingException {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        Rezerwacja rezerwacja1 = new Rezerwacja(1, LocalDate.of(2022, 1, 17), "12", "13", restauracja.stoliki.get(0));
        Rezerwacja rezerwacja2 = new Rezerwacja(2, LocalDate.of(2022, 1, 17), "13", "14", restauracja.stoliki.get(0));
        rezerwacjaMetody.dodajRezerwacje(osoba,rezerwacja1,restauracja);
        rezerwacjaMetody.dodajRezerwacje(osoba,rezerwacja2,restauracja);
        List<Rezerwacja> expected = new ArrayList<Rezerwacja>();
        expected.add(rezerwacja1);
        expected.add(rezerwacja2);
        doReturn(expected).when(orderRepoMock).WypiszKonkretnego(osoba);
        assertThat(rezerwacjaMetody.WypiszKonkretnego(osoba)).isEqualTo(expected);
    }


    @Test
    public void dodajRezerwacje_zlaGodzina() throws IOException, MessagingException {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        Rezerwacja rezerwacja1 = new Rezerwacja(1, LocalDate.of(2022, 1, 17), "9", "13", restauracja.stoliki.get(0));
        assertThatThrownBy(() -> rezerwacjaMetody.dodajRezerwacje(osoba,rezerwacja1,restauracja))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void dodajRezerwacje_zajeta() throws IOException, MessagingException {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        rezerwacjaMetody.dodajRezerwacje(osoba,rezerwacyja,restauracja);
        assertThat(rezerwacjaMetody.dodajRezerwacje(osoba,rezerwacyja,restauracja)).isFalse();
    }
    @Test
    public void czyWolny_false() throws IOException, MessagingException {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        rezerwacjaMetody.dodajRezerwacje(osoba,rezerwacyja,restauracja);
        assertThat(rezerwacjaMetody.czyWolny(rezerwacyja)).isFalse();
    }
    @Test
    public void czyWolnytrue() throws IOException, MessagingException {
        Osoba osoba = new Osoba(1,"Michal","michal@o2.pl");
        assertThat(rezerwacjaMetody.czyWolny(rezerwacyja)).isTrue();
    }

}
