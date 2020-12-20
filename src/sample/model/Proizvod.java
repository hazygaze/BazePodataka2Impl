package sample.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proizvod {

    private Integer sifraProizvoda;
    private String nazivProizvoda;
    private Double cenaProizvoda;
    private Integer aktuelnoSNZ;
    private List<StanjeNaZalihama> stanja;

    public Proizvod(Integer sifraProizvoda, String nazivProizvoda, Double cenaProizvoda, Integer aktuelnoSNZ) {
        this.sifraProizvoda = sifraProizvoda;
        this.nazivProizvoda = nazivProizvoda;
        this.cenaProizvoda = cenaProizvoda;
        this.aktuelnoSNZ = aktuelnoSNZ;
        stanja = new ArrayList<>();
    }

    public Proizvod() {
    }

    public Integer getSifraProizvoda() {
        return sifraProizvoda;
    }

    public void setSifraProizvoda(Integer sifraProizvoda) {
        this.sifraProizvoda = sifraProizvoda;
    }

    public String getNazivProizvoda() {
        return nazivProizvoda;
    }

    public void setNazivProizvoda(String nazivProizvoda) {
        this.nazivProizvoda = nazivProizvoda;
    }

    public Double getCenaProizvoda() {
        return cenaProizvoda;
    }

    public void setCenaProizvoda(Double cenaProizvoda) {
        this.cenaProizvoda = cenaProizvoda;
    }

    public Integer getAktuelnoSNZ() {
        return aktuelnoSNZ;
    }

    public void setAktuelnoSNZ(Integer aktuelnoSNZ) {
        this.aktuelnoSNZ = aktuelnoSNZ;
    }

    public List<StanjeNaZalihama> getStanja() {
        return stanja;
    }

    public void setStanja(List<StanjeNaZalihama> stanja) {
        this.stanja = stanja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proizvod)) return false;
        Proizvod proizvod = (Proizvod) o;
        return Objects.equals(getSifraProizvoda(), proizvod.getSifraProizvoda()) &&
                Objects.equals(getNazivProizvoda(), proizvod.getNazivProizvoda());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSifraProizvoda(), getNazivProizvoda());
    }

    @Override
    public String toString() {
        return "Proizvod{" +
                "sifraProizvoda=" + sifraProizvoda +
                ", nazivProizvoda='" + nazivProizvoda + '\'' +
                ", cenaProizvoda=" + cenaProizvoda +
                ", aktuelnoSNZ=" + aktuelnoSNZ +
                '}';
    }
}
