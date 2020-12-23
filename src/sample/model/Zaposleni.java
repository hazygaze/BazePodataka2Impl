package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Zaposleni {

    private StringProperty jmbg = new SimpleStringProperty();
    public StringProperty jmbgProperty() {return jmbg;}
    private String imePrezime;
    private Kontakt kontakt;

    public Zaposleni(String jmbg, String imePrezime, Kontakt kontakt) {
        setJmbg(jmbg);
        this.imePrezime = imePrezime;
        this.kontakt = kontakt;
    }

    public Zaposleni() {
        kontakt = new Kontakt();
    }

    public String getJmbg() {
        return jmbg.get();
    }

    public void setJmbg(String jmbg) {
        this.jmbg.set(jmbg);
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
