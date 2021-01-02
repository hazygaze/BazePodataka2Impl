package sample.model;

import javafx.beans.property.*;
import sample.util.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proizvod {

    private IntegerProperty sifraProizvoda = new SimpleIntegerProperty();
    public IntegerProperty sifraProizvodaProperty() {return sifraProizvoda; }
    private StringProperty nazivProizvoda = new SimpleStringProperty();
    public StringProperty nazivProizvodaProperty() {return  nazivProizvoda;}
    private Double cenaProizvoda;
    private Integer aktuelnoSNZ;
    private List<StanjeNaZalihama> stanja;

    public Proizvod(Integer sifraProizvoda, String nazivProizvoda, Double cenaProizvoda, Integer aktuelnoSNZ) {
       setSifraProizvoda(sifraProizvoda);
        setNazivProizvoda(nazivProizvoda);
        this.cenaProizvoda = cenaProizvoda;
        this.aktuelnoSNZ = aktuelnoSNZ;
        stanja = new ArrayList<>();
    }

    public Proizvod() {
        stanja = new ArrayList<>();
    }


    public int getSifraProizvoda() {
        return sifraProizvoda.get();
    }

    public void setSifraProizvoda(int sifraProizvoda) {
        this.sifraProizvoda.set(sifraProizvoda);
    }

    public String getNazivProizvoda() {
        return nazivProizvoda.get();
    }

    public void setNazivProizvoda(String nazivProizvoda) {
        this.nazivProizvoda.set(nazivProizvoda);
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

    public void editStanje(StanjeNaZalihama snz) {
        for(StanjeNaZalihama s: stanja) {
            if(s.getSifra()==snz.getSifra()) {
                s.setIznos(snz.getIznos());
                if(!s.getStatus().equals(Status.NEW)) {
                    s.setStatus(Status.UPDATED);
                }
                s.setDatum(snz.getDatum());
                s.setNazivProizvoda(snz.getNazivProizvoda());
            }
        }
    }

    public void deleteStanje(StanjeNaZalihama snz) {
        for(StanjeNaZalihama s: stanja) {
            if(s.getSifra()==snz.getSifra()) {
                if(snz.getStatus() == Status.NEW) {
                    stanja.remove(s);
                } else {
                    s.setStatus(Status.DELETE);
                }
            }
        }
    }
}
