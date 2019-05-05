public class Osoba {
   private int id;
    private String imie;
    private String email;
    private static final String EMAIL_REGEX = ".+\\@.+\\..+";
    private static final String IMIE_REGEX = "[A-Z][a-z]*";

    public Osoba(int id,String imie, String email) {
       this.id=id;
        this.imie = imie;
        this.email = email;
    }


    public String getImie() {
        return imie;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
