import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IRezerwacje {
    public boolean dodajRezerwacje(Osoba osoba, Rezerwacja rezerwacja, Restauracja restauracja) throws IOException, MessagingException;

    public ArrayList<String> WypiszKonkretnego(Osoba osoba);

    public boolean czyWolny(Rezerwacja rezerwacja);
}
