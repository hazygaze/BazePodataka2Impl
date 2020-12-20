package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Kontakt {

    private StringProperty telefon = new SimpleStringProperty();
    public StringProperty telfonProperty() {
        return telefon ;
    }
    private StringProperty email = new SimpleStringProperty();
    public StringProperty emailProperty() {
        return email ;
    }
    private StringProperty fax = new SimpleStringProperty();
    public StringProperty faxProperty() {
        return fax;
    }


    public Kontakt() {
    }
    public Kontakt(String email, String fax, String telefon) {
        setEmail(email);
        setFax(fax);
        setTelefon(telefon);
    }
    public String getTelefon() {
        return telefon.get();
    }

    public void setTelefon(String telefon) {
        this.telefon.set(telefon);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getFax() {
        return fax.get();
    }

    public void setFax(String fax) {
        this.fax.set(fax);
    }

}
