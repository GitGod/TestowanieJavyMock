import java.util.ArrayList;
import java.util.List;

public class FakeMockOsobaRepository implements IOsoba {

	private List<Osoba> listaOsob;
	
	public FakeMockOsobaRepository() {
		this.listaOsob = new ArrayList<Osoba>();
	}




	@Override
	public Osoba getById(int id) {
		return this.listaOsob.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}

	@Override
	public List<Osoba> getAll() {
		return listaOsob;
	}

	@Override
	public boolean add(Osoba osoba) {
		if(this.getById(osoba.getId()) != null) {
			return false;
		}
		listaOsob.add(osoba);
		return true;

	}

	@Override
	public boolean update(Osoba osoba) {
		Osoba osobaToUpdate = listaOsob.stream().filter(x -> x.getId() == osoba.getId()).findFirst().orElse(null);

		if(osobaToUpdate == null)
			return false;

		listaOsob.remove(osobaToUpdate);
		listaOsob.add(osoba);
		return true;

	}

	@Override
	public boolean delete(Osoba osoba) {
		Osoba osobaToDelete = listaOsob.stream().filter(x -> x.getId() == osoba.getId()).findFirst().orElse(null);
		if(osobaToDelete == null) {
			return false;
		}
		listaOsob.remove(osoba);
		return true;

	}
}
