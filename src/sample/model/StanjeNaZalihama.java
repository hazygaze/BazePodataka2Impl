package sample.model;


import java.util.Date;
import java.util.Objects;

public class StanjeNaZalihama {

    private int sifra;
    private int iznos;
    private Date datum;
    private Proizvod proizvod;
    private String nazivProizvoda;

    public StanjeNaZalihama() {
    }

    public StanjeNaZalihama(int sifra, int iznos, Date datum, Proizvod proizvod, String nazivProizvoda) {
        this.sifra = sifra;
        this.iznos = iznos;
        this.datum = datum;
        this.proizvod = proizvod;
        this.nazivProizvoda = nazivProizvoda;
    }

    public int getSifra() {
        return sifra;
    }

    public void setSifra(int sifra) {
        this.sifra = sifra;
    }

    public int getIznos() {
        return iznos;
    }

    public void setIznos(int iznos) {
        this.iznos = iznos;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }


    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
    }

    public String getNazivProizvoda() {
        return nazivProizvoda;
    }

    public void setNazivProizvoda(String nazivProizvoda) {
        this.nazivProizvoda = nazivProizvoda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StanjeNaZalihama)) return false;
        StanjeNaZalihama that = (StanjeNaZalihama) o;
        return getSifra() == that.getSifra() &&
                Objects.equals(getProizvod(), that.getProizvod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSifra(), getProizvod());
    }

    @Override
    public String toString() {
        return "StanjeNaZalihama{" +
                "sifra=" + sifra +
                ", iznos=" + iznos +
                ", datum=" + datum +
                ", proizvod=" + proizvod +
                ", nazivProizvoda='" + nazivProizvoda + '\'' +
                '}';
    }
}