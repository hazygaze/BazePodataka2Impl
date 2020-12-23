package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.db.DBBroker;
import sample.forms.AlertBox;
import sample.model.*;
import sample.tables.MagacinTableHandler;
import sample.tables.NalogZaTransportTableHandler;
import sample.util.Konstante;
import sample.util.MySQLException;

import java.util.stream.Collectors;

public class NalogController {

    @FXML
    TextField txtSifra;
    @FXML
    TextField txtOpis;
    @FXML
    TextField txtDatum;
    @FXML
    TextField txtImePrezZap;
    @FXML
    TextField txtNi;
    @FXML
    ComboBox<Zaposleni> cbZaposleni;
    @FXML
    ComboBox<Magacioner> cbMagacioneri;
    @FXML
    ComboBox<Proizvod> cbProizvodi;



    private ObservableList<Zaposleni> zaposlenis;
    private ObservableList<Proizvod> proizvods;
    private ObservableList<Magacioner> magacioners;
    private ObservableList<Magacin> magacins;
    private NalogZaTransportTableHandler nt;
    private Stage dialog;
    private NalogZaTransport nzt;
    private int akcija = 0;

    public void init(int action, NalogZaTransportTableHandler nt, NalogZaTransport nzt, Stage dialog) {
        this.nt = nt;
        this.dialog = dialog;
        setupCmbBox();
        if(action == Konstante.INSERT) {
            this.nzt= new NalogZaTransport();
            akcija = Konstante.INSERT;
            System.out.println("Inserting::NalogZaTransport");
        } else {
            akcija = Konstante.EDIT;
            this.nzt = nzt;
            edit();
        }
    }

    private void edit() {
        txtSifra.setText(nzt.getSifraNzt().toString());
        txtSifra.setEditable(false);
        txtOpis.setText(nzt.getOpis());
        txtDatum.setText(nzt.getDatum());
        cbProizvodi.getSelectionModel().select(nzt.getProizvod());
        txtNi.setText(nzt.getNacinIsporuke());
        cbMagacioneri.getSelectionModel().select(nzt.getMagacioner());
        cbZaposleni.getSelectionModel().select(nzt.getZaposleni());
        txtImePrezZap.setText(nzt.getImePrezimeZap());
    }


    public void close(ActionEvent actionEvent) {
    }

    public void save(ActionEvent actionEvent) {
        nzt.setOpis(txtOpis.getText());
        nzt.setDatum(txtDatum.getText());
        nzt.setNacinIsporuke(txtNi.getText());
        nzt.setProizvod(cbProizvodi.getSelectionModel().getSelectedItem());
        nzt.setZaposleni(cbZaposleni.getSelectionModel().getSelectedItem());
        nzt.setMagacioner(cbMagacioneri.getSelectionModel().getSelectedItem());
        if(akcija == Konstante.INSERT) {
            try {
                nzt.setSifraNzt(Integer.parseInt(txtSifra.getText()));
                if(nzt.getMagacioner() != null) {
                    nzt.setMagacin(nzt.getMagacioner().getMagacin());
                }
                DBBroker.getInstance().insertNalog(nzt);
                DBBroker.getInstance().commit();
                nt.notifyDataChanged();
                dialog.close();
            } catch (MySQLException e) {
                System.out.println(e.getMessage());
                AlertBox.display("Error dialog", e.getMessage());
            }
        }
        if(akcija == Konstante.EDIT) {
            try {
                if(nzt.getMagacioner() != null) {
                    nzt.setMagacin(nzt.getMagacioner().getMagacin());
                }
                DBBroker.getInstance().updateNalog(nzt);
                DBBroker.getInstance().commit();
                nt.notifyDataChanged();
                dialog.close();
            } catch (MySQLException e) {
                AlertBox.display("Error dialog", e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void setupCmbBox() {
        cbMagacioneri.setItems(ucitajMagacionere());

        StringConverter<Magacioner> converter = new StringConverter<Magacioner>() {
            @Override
            public String toString(Magacioner magacioner) {
                return magacioner.getJmbg();
            }

            @Override
            public Magacioner fromString(String id) {
                return ucitajMagacionere().stream()
                        .filter(item -> item.jmbgProperty().equals(id))
                        .collect(Collectors.toList()).get(0);
            }
        };
        cbMagacioneri.setConverter(converter);


        cbProizvodi.setItems(ucitajProizvode());

        StringConverter<Proizvod> converterPr = new StringConverter<Proizvod>() {
            @Override
            public String toString(Proizvod proizvod) {
                return String.valueOf(proizvod.getSifraProizvoda());
            }

            @Override
            public Proizvod fromString(String id) {
                return ucitajProizvode().stream()
                        .filter(item -> item.getSifraProizvoda() == Integer.parseInt(id))
                        .collect(Collectors.toList()).get(0);
            }
        };
        cbProizvodi.setConverter(converterPr);

        cbZaposleni.setItems(ucitajZaposlene());

        StringConverter<Zaposleni> converterZap = new StringConverter<Zaposleni>() {
            @Override
            public String toString(Zaposleni zaposleni) {
                return zaposleni.getJmbg();
            }

            @Override
            public Zaposleni fromString(String id) {
                return ucitajZaposlene().stream()
                        .filter(item -> item.getJmbg().equals(id))
                        .collect(Collectors.toList()).get(0);
            }
        };
        cbZaposleni.setConverter(converterZap);



}

    private ObservableList<Proizvod> ucitajProizvode() {
        ObservableList<Proizvod> proizvods = null;
        try {
            proizvods = FXCollections.observableArrayList(DBBroker.getInstance().vratiSveProizvode());
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error", e.getMessage());
        }
        return proizvods;
        }

    private ObservableList<Zaposleni> ucitajZaposlene() {
        ObservableList<Zaposleni> zaposleni = null;
        try {
            zaposleni = FXCollections.observableArrayList(DBBroker.getInstance().vratiSveZaposlene());
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error", e.getMessage());
        }
        return zaposleni;
    }

    private ObservableList<Magacioner> ucitajMagacionere() {
        ObservableList<Magacioner> magacioneri = null;
        try {
            magacioneri = FXCollections.observableArrayList(DBBroker.getInstance().vratiSveMagacionere());
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.display("Error", e.getMessage());
        }
        return magacioneri;
    }


}
