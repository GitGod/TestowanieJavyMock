import java.util.List;


public class OsobaMetody {
    private static final String EMAIL_REGEX = ".+\\@.+\\..+";
    private static final String IMIE_REGEX = "[A-Z][a-z]*";
    private IOsoba baza;

    public OsobaMetody(IOsoba repo) {
        this.baza = repo;
    }

    public List<Osoba> getAllOsoba(){
        return baza.getAll();
    }

    public Osoba getOsobaById(int id){
        return baza.getById(id);
    }

    public boolean addOsoba(Osoba osoba) {
        if (!validOsoba(osoba)) {
            throw new IllegalArgumentException();
        }
        return baza.add(osoba);
    }

    public boolean updateOsoba(Osoba osoba) {
        if(osoba == null)
            throw new IllegalArgumentException("Nie moze byc puste");

        return baza.update(osoba);
    }

    public boolean deleteOsoba(Osoba osoba) {
        if(osoba == null)
            throw new IllegalArgumentException("Nie moze byc puste");

        return baza.delete(osoba);
    }
    public boolean validOsoba(Osoba osoba) {
        if (!osoba.getEmail().matches(EMAIL_REGEX) || !osoba.getImie().matches(IMIE_REGEX)) {
            return false;
        }
        return true;
    }
}

