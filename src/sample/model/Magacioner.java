package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Magacioner {

    private StringProperty jmbg = new SimpleStringProperty();
    public StringProperty jmbgProperty() {
        return jmbg ;
    }

    private StringProperty imePrezime = new SimpleStringProperty();
    public StringProperty imePrezimeProperty() {
        return imePrezime ;
    }

    private Magacin magacin;

    public Magacioner() {
    }

    public Magacioner(String jmbg, String imePrezime, Magacin magacin) {
        setJmbg(jmbg);
        setImePrezime(imePrezime);
        this.magacin = magacin;
    }

    public String getJmbg() {
        return jmbg.get();
    }

    public void setJmbg(String jmbg) {
        this.jmbg.set(jmbg);
    }

    public String getImePrezime() {
        return imePrezime.get();
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime.set(imePrezime);
    }

    public Magacin getMagacin() {
        return magacin;
    }

    public void setMagacin(Magacin magacin) {
        this.magacin = magacin;
    }

    @Override
    public String toString() {
        return "Magacioner{" +
                "jmbg=" + jmbg +
                ", imePrezime=" + imePrezime +
                ", magacin=" + magacin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magacioner that = (Magacioner) o;
        return Objects.equals(jmbg, that.jmbg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbg);
    }
}
