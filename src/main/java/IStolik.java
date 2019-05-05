import java.util.List;

public interface IStolik {
    Stolik getByNumer(int numer);

    List<Stolik> getAll();

    boolean add(Stolik stolik);

    boolean update(Stolik stolik);

    boolean delete(Stolik stolik);

}
