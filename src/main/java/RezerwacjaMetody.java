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
                generujPotwierdzenie(osoba, rezerwacja);

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
                generujPotwierdzenie(osoba, rezerwacja);
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


    public void generujDane(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        HashMap<String, Integer> emaile = new HashMap();
        HashMap<String, Integer> dni = new HashMap();
        HashMap<Integer, Integer> godziny = new HashMap();
        HashMap<Integer, Integer> stoliki = new HashMap();

        String line = bufferedReader.readLine();
        Restauracja res = new Restauracja();
        // res.readFile("Restauracja.txt");
        plik = false;
        LocalDate data;
        while (line != null) {
            String[] podzielone = line.split(" ");
            String[] podzielone2 = podzielone[8].split("-");
            String[] podzielone3 = podzielone[6].split("-");
            if (emaile.containsKey(podzielone[0])) {
                emaile.put(podzielone[0], emaile.get(podzielone[0]) + 1);
            } else {
                emaile.put(podzielone[0], 1);
            }
            if (godziny.containsKey(Integer.parseInt(podzielone2[0]))) {
                godziny.put(Integer.parseInt(podzielone2[0]), godziny.get(Integer.parseInt(podzielone2[0])) + 1);
            } else {
                godziny.put(Integer.parseInt(podzielone2[0]), 1);

            }
            if (stoliki.containsKey(Integer.parseInt(podzielone[3]))) {
                stoliki.put(Integer.parseInt(podzielone[3]), stoliki.get(Integer.parseInt(podzielone[3])) + 1);
            } else {
                stoliki.put(Integer.parseInt(podzielone[3]), 1);

            }
            data = LocalDate.of(Integer.parseInt(podzielone3[0])
                    , Integer.parseInt(podzielone3[1]),
                    Integer.parseInt(podzielone3[2]));
            if (dni.containsKey(data.getDayOfWeek().toString())) {
                dni.put(data.getDayOfWeek().toString(), dni.get(data.getDayOfWeek().toString()) + 1);
            } else {
                dni.put(data.getDayOfWeek().toString(), 1);

            }
            line = bufferedReader.readLine();
        }
        PrintWriter plik2 = null;
        String najczestszyEmail = Collections.max(emaile.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
        String najczestszyDzien = Collections.max(dni.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
        Integer najczestszaGodzina = Collections.max(godziny.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
        Integer najczestszyStolik = Collections.max(stoliki.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
        try {
            plik2 = new PrintWriter(new FileWriter("plik2.txt"));
            plik2.print("Najczesciej rezerwujacy ");
            plik2.println(najczestszyEmail + " " + emaile.get(najczestszyEmail));
            plik2.print("Najwiecej rezerwacji w dniu ");
            plik2.println(najczestszyDzien + " " + dni.get(najczestszyDzien));
            plik2.print("Najczesciej rezerwowana godzina ");
            plik2.println(najczestszaGodzina + " " + godziny.get(najczestszaGodzina));
            plik2.print("Najczesciej rezerwowany stolik ");
            plik2.println(najczestszyStolik + " " + stoliki.get(najczestszyStolik));
        } finally {
            if (plik2 != null) {
                plik2.close();
            }
        }
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

    public void generujPotwierdzenie(Osoba osoba, Rezerwacja rezerwacja) throws IOException {
        PrintWriter potwierdzenie = null;

        try {
            potwierdzenie = new PrintWriter(new FileWriter("potwierdzenie.txt"));
            potwierdzenie.println("Brawo udalo ci sie zarezerwowac stolik w naszej restauracji! ");
            potwierdzenie.println("Rezerwacja na :\nimie: " + osoba.getImie() + " email: " + osoba.getEmail());
            potwierdzenie.print("Informacje o rezerwacji: ");
            potwierdzenie.println(rezerwacja.data + " " + rezerwacja.godzina + "-" + rezerwacja.godzinaDo + " Stolik numer: " + rezerwacja.stolik.numer + " Liczba miejsc: " + rezerwacja.stolik.liczbaMiejsc);
        } finally {
            if (potwierdzenie != null) {
                potwierdzenie.close();
            }
        }
    }

    public String generujPotwierdzenie2(Osoba osoba, Rezerwacja rezerwacja) throws IOException {
        String potwierdzenie = "";
        potwierdzenie = potwierdzenie.concat("Brawo udalo ci sie zarezerwowac stolik w naszej restauracji!\n ");
        potwierdzenie = potwierdzenie.concat("Rezerwacja na :\nimie: " + osoba.getImie() + " email: " + osoba.getEmail() + "\n");
        potwierdzenie = potwierdzenie.concat("Informacje o rezerwacji: ");
        potwierdzenie = potwierdzenie.concat(rezerwacja.data + " " + rezerwacja.godzina + "-" + rezerwacja.godzinaDo + " Stolik numer: " + rezerwacja.stolik.numer + " Liczba miejsc: " + rezerwacja.stolik.liczbaMiejsc);
        return potwierdzenie;
    }

    public boolean sprawdzCzyotwarte(Rezerwacja rezerwacja, Restauracja restauracja) {
        Calendar c = Calendar.getInstance();
        //  c.setTime(rezerwacja.data);
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


