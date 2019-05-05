import java.time.LocalDate;
import java.util.Date;

public class Rezerwacja {
    int id;
    public String dzien;
    public LocalDate data;
    public String godzina;
    public String godzinaDo;
    public Stolik stolik;


    public Rezerwacja(int id,LocalDate data, String godzina, String godzinaDo, Stolik stolik) {
        this.id=id;
        this.data = data;
        this.godzina = godzina;
        this.godzinaDo = godzinaDo;
        this.stolik = stolik;
    }

    @Override
    public String toString() {
        return "Stolik numer " + stolik.numer + " " + stolik.liczbaMiejsc + " osobowy " + data + " godzina " + godzina + "-" + godzinaDo;
    }
}
