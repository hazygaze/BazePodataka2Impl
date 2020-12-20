package sample.model;

import java.util.Objects;

public class Zaposleni {

    private String jmbg;
    private String imePrezime;
    private Kontakt kontakt;

    public Zaposleni(String jmbg, String imePrezime, Kontakt kontakt) {
        this.jmbg = jmbg;
        this.imePrezime = imePrezime;
        this.kontakt = kontakt;
    }

    public Zaposleni() {
        kontakt = new Kontakt();
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zaposleni)) return false;
        Zaposleni zaposleni = (Zaposleni) o;
        return Objects.equals(getJmbg(), zaposleni.getJmbg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJmbg());
    }

    public Kontakt getKontakt() {
        return kontakt;
    }

    public void setKontakt(Kontakt kontakt) {
        this.kontakt = kontakt;
    }

    @Override
    public String toString() {
        return "Zaposleni{" +
                "jmbg='" + jmbg + '\'' +
                ", imePrezime='" + imePrezime + '\'' +
                ", KONTAKT[ telefon: " + kontakt.getTelefon() + " fax: " +kontakt.getFax()+" email: "+kontakt.getEmail()+
                '}';
    }
}
