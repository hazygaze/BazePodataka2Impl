package sample.model;

import javafx.beans.property.IntegerProperty;

import java.util.Objects;

public class NalogZaTransport {

    private Integer sifraNzt;
    private String opis;
    private String datum;
    private Magacioner Magacioner;
    private Zaposleni Zaposleni;
    private Magacin magacin;
    private Proizvod proizvod;
    private String nacinIsporuke;
    private String imePrezimeZap;

    public NalogZaTransport() {
    }

    public NalogZaTransport(Integer sifraNzt, String opis, String datum, sample.model.Magacioner magacioner, sample.model.Zaposleni zaposleni, Magacin magacin, Proizvod proizvod, String nacinIsporuke, String imePrezimeZap) {
        this.sifraNzt = sifraNzt;
        this.opis = opis;
        this.datum = datum;
        Magacioner = magacioner;
        Zaposleni = zaposleni;
        this.magacin = magacin;
        this.proizvod = proizvod;
        this.nacinIsporuke = nacinIsporuke;
        this.imePrezimeZap = imePrezimeZap;
    }

    public Integer getSifraNzt() {
        return sifraNzt;
    }

    public void setSifraNzt(Integer sifraNzt) {
        this.sifraNzt = sifraNzt;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public sample.model.Magacioner getMagacioner() {
        return Magacioner;
    }

    public void setMagacioner(sample.model.Magacioner magacioner) {
        Magacioner = magacioner;
    }

    public sample.model.Zaposleni getZaposleni() {
        return Zaposleni;
    }

    public void setZaposleni(sample.model.Zaposleni zaposleni) {
        Zaposleni = zaposleni;
    }

    public Magacin getMagacin() {
        return magacin;
    }

    public void setMagacin(Magacin magacin) {
        this.magacin = magacin;
    }

    public Proizvod getProizvod() {
        return proizvod;
    }

    public void setProizvod(Proizvod proizvod) {
        this.proizvod = proizvod;
    }

    public String getNacinIsporuke() {
        return nacinIsporuke;
    }

    public void setNacinIsporuke(String nacinIsporuke) {
        this.nacinIsporuke = nacinIsporuke;
    }

    public String getImePrezimeZap() {
        return imePrezimeZap;
    }

    public void setImePrezimeZap(String imePrezimeZap) {
        this.imePrezimeZap = imePrezimeZap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NalogZaTransport that = (NalogZaTransport) o;
        return Objects.equals(sifraNzt, that.sifraNzt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sifraNzt);
    }

    @Override
    public String toString() {
        return "NalogZaTransport{" +
                "sifraNzt=" + sifraNzt +
                ", opis='" + opis + '\'' +
                ", datum='" + datum + '\'' +
                ", Magacioner=" + Magacioner +
                ", Zaposleni=" + Zaposleni +
                ", magacin=" + magacin +
                ", proizvod=" + proizvod +
                ", nacinIsporuke='" + nacinIsporuke + '\'' +
                ", imePrezimeZap='" + imePrezimeZap + '\'' +
                '}';
    }
}
