

import java.util.List;

public interface IOsoba {
    Osoba getById(int id);

    List<Osoba> getAll();

    boolean add(Osoba osoba);

    boolean update(Osoba osoba);

    boolean delete(Osoba osoba);


}
