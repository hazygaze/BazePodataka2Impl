package sample.model;

import java.util.Objects;

public class Grad {

    private int postanskiBroj;
    private String nazivGrada;

    public Grad(int postanskiBroj, String nazivGrada) {
        this.postanskiBroj = postanskiBroj;
        this.nazivGrada = nazivGrada;
    }

    public Grad() {
    }

    public int getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(int postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
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
                "postanskiBroj=" + postanskiBroj +
                ", nazivGrada='" + nazivGrada + '\'' +
                '}';
    }
}
