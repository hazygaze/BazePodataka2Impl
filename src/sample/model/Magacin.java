package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Magacin {

    private Integer brojMagacina;
    private StringProperty nazivMagacina = new SimpleStringProperty();
    public StringProperty nazivMagacinaProperty() {return nazivMagacina;}
    private String email;
    private String telefon;
    private Grad grad;

    public Magacin(Integer brojMagacina, String nazivMagacina, String email, String telefon, Grad grad) {
        this.brojMagacina = brojMagacina;
        setNazivMagacina(nazivMagacina);
        this.email = email;
        this.telefon = telefon;
        this.grad = grad;
    }

    public Magacin() {
    }

    public Integer getBrojMagacina() {
        return brojMagacina;
    }

    public void setBrojMagacina(Integer brojMagacina) {
        this.brojMagacina = brojMagacina;
    }

    public String getNazivMagacina() {
        return nazivMagacina.get();
    }

    public void setNazivMagacina(String nazivMagacina) {
        this.nazivMagacina.set(nazivMagacina);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Grad getGrad() {
        return grad;
    }

    public void setGrad(Grad grad) {
        this.grad = grad;
    }

    @Override
    public String toString() {
        return "Magacin{" +
                "brojMagacina=" + brojMagacina +
                ", nazivMagacina='" + nazivMagacina + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", grad=" + grad +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magacin magacin = (Magacin) o;
        return Objects.equals(brojMagacina, magacin.brojMagacina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brojMagacina);
    }
}
