package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Grad {

    private IntegerProperty postanskiBroj = new SimpleIntegerProperty();
    public IntegerProperty postbrProperty() {
        return postanskiBroj ;
    }

    private String nazivGrada;

    public Grad(int postanskiBroj, String nazivGrada) {
        setPostanskiBroj(postanskiBroj);
        this.nazivGrada = nazivGrada;
    }

    public Grad() {
    }

    public Integer getPostanskiBroj() {
        return postanskiBroj.get();
    }

    public void setPostanskiBroj(Integer postanskiBroj) {
        this.postanskiBroj.set(postanskiBroj);
    }

    public String getNazivGrada() {
        return nazivGrada;
    }

    public void setNazivGrada(String nazivGrada) {
        this.nazivGrada = nazivGrada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grad)) return false;
        Grad grad = (Grad) o;
        return getPostanskiBroj() == grad.getPostanskiBroj();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostanskiBroj());
    }

    @Override
    public String toString() {
        return "Grad{" +
                "postanskiBroj=" + postanskiBroj.get() +
                ", nazivGrada='" + nazivGrada + '\'' +
                '}';
    }
}
