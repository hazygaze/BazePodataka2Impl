package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.db.DBBroker;
import sample.model.Zaposleni;
import sample.tables.ZaposleniTableHandler;
import sample.util.Konstante;
import sample.util.MySQLException;

public class ZaposleniController {

    private int akcija = 0;
    Stage dialog;
    Zaposleni zap;
    ZaposleniTableHandler zt;
    @FXML
    Button btnSave;
    @FXML
    Button btnClose;
    @FXML
    TextField txtJmbg;
    @FXML
    TextField txtImePrez;
    @FXML
    TextField txtTelefon;
    @FXML
    TextField txtEmail;
    @FXML
    TextField txtFax;

    public void init(int action, ZaposleniTableHandler zt, Zaposleni zap, Stage dialog) {
        this.zt = zt;
        this.dialog = dialog;
        if(action == Konstante.INSERT) {
            this.zap = new Zaposleni();
            akcija = Konstante.INSERT;
            System.out.println("Inserting::Zaposleni");
        } else {
            akcija = Konstante.EDIT;
            this.zap = zap;
            edit();
        }
    }

    private void edit() {
        txtJmbg.setText(zap.getJmbg());
        txtFax.setEditable(false);
        txtImePrez.setText(zap.getImePrezime());
        txtTelefon.setText(zap.getKontakt().getTelefon());
        txtEmail.setText(zap.getKontakt().getEmail());
        txtFax.setText(zap.getKontakt().getFax());
    }


    public void save(ActionEvent actionEvent) {
        zap.setImePrezime(txtImePrez.getText());
        zap.getKontakt().setTelefon(txtTelefon.getText());
        zap.getKontakt().setEmail(txtEmail.getText());
        zap.getKontakt().setFax(txtFax.getText());
        if(akcija == Konstante.INSERT) {
            try {
                zap.setJmbg(txtJmbg.getText());
                DBBroker.getInstance().sacuvajZaposlenog(zap);
                DBBroker.getInstance().commit();
                zt.notifyDataChanged();
                dialog.close();
            } catch (MySQLException e) {
                System.out.println(e.getMessage());
            }
        }
        if(akcija == Konstante.EDIT) {
            DBBroker.getInstance().updateZaposlenog(zap);
            DBBroker.getInstance().commit();
            zt.notifyDataChanged();
            dialog.close();

        }

    }

    public void close(ActionEvent actionEvent) {
        dialog.close();
    }
}
