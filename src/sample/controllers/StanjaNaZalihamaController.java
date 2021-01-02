package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.forms.AlertBox;
import sample.model.StanjeNaZalihama;
import sample.model.Zaposleni;
import sample.util.Konstante;
import sample.util.Status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StanjaNaZalihamaController {

    private int akcija = 0;
    private StanjeNaZalihama snz;
    private ProizvodController pc;
    Stage dialog;
    Zaposleni zap;
    boolean raise = false;

    @FXML
    TextField txtSifra;
    @FXML
    TextField txtIznos;
    @FXML
    TextField txtDatum;
    @FXML
    TextField txtProizvod;
    @FXML
    TextField txtNazivPr;

    public void init(int action, StanjeNaZalihama snz, Stage dialog, ProizvodController pc) {
        this.dialog = dialog;
        this.pc = pc;
        txtProizvod.setEditable(false);
        if(action == Konstante.INSERT) {
            this.snz = snz;
            akcija = Konstante.INSERT;
            System.out.println("Inserting::StanjeNaZalihama");
            txtProizvod.setText(String.valueOf(snz.getProizvod().getSifraProizvoda()));
            txtNazivPr.setText(snz.getProizvod().getNazivProizvoda());

        } else {
            akcija = Konstante.EDIT;
            this.snz = snz;
            edit();
        }
        txtNazivPr.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                raise = true;
            }
        });
    }


    private void edit() {
        snz.setStatus(Status.UPDATED);
        txtSifra.setEditable(false);
        txtSifra.setText(String.valueOf(snz.getSifra()));
        txtIznos.setText(String.valueOf(snz.getIznos()));
        txtDatum.setText(String.valueOf(snz.getDatum()));
        txtProizvod.setText(String.valueOf(snz.getProizvod().getSifraProizvoda()));
        txtNazivPr.setText(snz.getProizvod().getNazivProizvoda());

    }

    public void close(ActionEvent actionEvent) {
        dialog.close();
    }

    public void save(ActionEvent actionEvent) {
        if(raise) {
            pc.setRaise(raise);
        }

        if(akcija == Konstante.INSERT) {
            snz.setSifra(Integer.parseInt(txtSifra.getText()));
            snz.setIznos(Integer.parseInt(txtIznos.getText()));
            snz.setNazivProizvoda(txtNazivPr.getText());
            snz.setDatum(txtDatum.getText());
            pc.addToList(snz);
            dialog.close();
        }

        if(akcija == Konstante.EDIT) {
            snz.setIznos(Integer.parseInt(txtIznos.getText()));
            snz.setNazivProizvoda(txtNazivPr.getText());
            snz.setDatum(txtDatum.getText());
            pc.editFromDialog(snz);
            dialog.close();
        }
    }



}
