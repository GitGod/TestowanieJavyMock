import java.util.List;

public class StolikMetody {
    private IStolik baza;
    public StolikMetody(IStolik repo) {
        this.baza = repo;
    }

    public List<Stolik> getAllStolik(){
        return baza.getAll();
    }

    public Stolik getStolikByNumer(int numer){
        return baza.getByNumer(numer);
    }

    public boolean addStolik(Stolik stolik) {

        return baza.add(stolik);
    }

    public boolean updateStolik(Stolik stolik) {
        if(stolik == null)
            throw new IllegalArgumentException("Nie moze byc puste");

        return baza.update(stolik);
    }

    public boolean deleteStolik(Stolik stolik) {
        if(stolik == null)
            throw new IllegalArgumentException("Nie moze byc puste");

        return baza.delete(stolik);
    }
}
