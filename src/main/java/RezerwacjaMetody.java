import javax.mail.MessagingException;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RezerwacjaMetody {
    HashMap<String, ArrayList> rezerwacje = new HashMap();
    private static final String EMAIL_REGEX = ".+\\@.+\\..+";
    private static final String IMIE_REGEX = "[A-Z][a-z]*";
    private static final String GODZINA_REGEX = "[1-2]?[0-9]{1}(\\.30)?";
    private boolean plik = true;
    private boolean odczyt = false;
    private IRezerwacje rezerwacjeRepo;

    public RezerwacjaMetody(IRezerwacje rezerwacjeRepo) {
        this.rezerwacjeRepo = rezerwacjeRepo;
    }


    public void setPlik(boolean plik) {
        this.plik = plik;
    }

    public void setOdczyt(boolean odczyt) {
        this.odczyt = odczyt;
    }

    public boolean dodajRezerwacje(Osoba osoba, Rezerwacja rezerwacja, Restauracja restauracja) throws IOException, MessagingException {
        if (osoba == null || rezerwacja == null) {
            throw new NullPointerException();
        }
        if (!sprawdzCzyotwarte(rezerwacja, restauracja)) {
            throw new IllegalArgumentException();
        }
        if (!validOsoba(osoba)) {
            throw new IllegalArgumentException();
        }

        if (Double.parseDouble(rezerwacja.godzinaDo) <= Double.parseDouble(rezerwacja.godzina) || !validGodzina(rezerwacja.godzina,rezerwacja.godzinaDo)) {
            throw new IllegalArgumentException();
        }
        if(odczyt==false) {
            if (LocalDate.now().compareTo(rezerwacja.data) >= 0) {
                throw new IllegalArgumentException();
            }
        }
        PrintWriter plik2 = null;


        if (czyWolny(rezerwacja)) {
            if (rezerwacje.containsKey(osoba.getEmail())) {
                ArrayList lista = rezerwacje.get(osoba.getEmail());
                lista.add(rezerwacja);
                rezerwacje.put(osoba.getEmail(), lista);

                if (plik == true) {
                    try {
                        plik2 = new PrintWriter(new FileWriter("plik.txt", true));
                        plik2.println(osoba.getEmail() + " " + rezerwacja.toString());
                    } finally {
                        if (plik2 != null) {
                            plik2.close();
                        }
                    }
                }

                return rezerwacjeRepo.dodajRezerwacje(osoba,rezerwacja,restauracja);
            } else {
                ArrayList lista = new ArrayList();
                lista.add(rezerwacja);
                rezerwacje.put(osoba.getEmail(), lista);

                if (plik == true) {
                    try {
                        plik2 = new PrintWriter(new FileWriter("plik.txt", true));
                        plik2.println(osoba.getEmail() + " " + rezerwacja.toString());
                    } finally {
                        if (plik2 != null) {
                            plik2.close();
                        }
                    }
                }
                return rezerwacjeRepo.dodajRezerwacje(osoba,rezerwacja,restauracja);
            }
        }
        return false;
    }

    public boolean czyWolny(Rezerwacja rezerwacja) {
        for (Map.Entry<String, ArrayList> entry : rezerwacje.entrySet()) {
            Object value = entry.getValue();
            for (int i = 0; i < ((ArrayList) value).size(); i++) {
                if (compare(rezerwacja, ((ArrayList) value).get(i))) {
                    return false;
                }
            }
        }
        return true;
    }





    public boolean compare(Object o1, Object o2) {
        Rezerwacja p1 = (Rezerwacja) o1;
        Rezerwacja p2 = (Rezerwacja) o2;
        // if last names are the same compare first names
        if (p1.data.equals(p2.data) && p1.stolik.numer == p2.stolik.numer && (p1.godzina.equals(p2.godzina) || p1.godzinaDo.equals(p2.godzinaDo) || (Double.parseDouble(p1.godzina) < Double.parseDouble(p2.godzina) && Double.parseDouble(p1.godzinaDo) > Double.parseDouble(p2.godzina)) || (Double.parseDouble(p1.godzina) > Double.parseDouble(p2.godzina) && Double.parseDouble(p1.godzina) < Double.parseDouble(p2.godzinaDo)))) {
            return true;
        }
        return false;
    }

    public ArrayList WypiszKonkretnego(Osoba osoba) {
        if(osoba == null){
            throw  new IllegalArgumentException();
        }
        if(rezerwacje.get(osoba.getEmail()) == null){
            return new ArrayList();
        }
        return rezerwacje.get(osoba.getEmail());

    }



    public boolean sprawdzCzyotwarte(Rezerwacja rezerwacja, Restauracja restauracja) {
        Calendar c = Calendar.getInstance();

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String godzinki = restauracja.godziny_otwarcia.get(rezerwacja.data.getDayOfWeek().toString());
        String podzielone[] = godzinki.split("-");
        if (Double.parseDouble(rezerwacja.godzina) < Integer.parseInt(podzielone[0]) || Double.parseDouble(rezerwacja.godzina) >= Integer.parseInt(podzielone[1])) {
            return false;
        }
        return true;

    }

    public boolean validOsoba(Osoba osoba) {
        if (!osoba.getEmail().matches(EMAIL_REGEX) || !osoba.getImie().matches(IMIE_REGEX)) {
            return false;
        }
        return true;
    }
    public boolean validGodzina(String godzinaOd, String godzinaDo){
        if(!godzinaOd.matches(GODZINA_REGEX) || !godzinaDo.matches(GODZINA_REGEX)){
            return false;
        }
        return true;
    }


}


