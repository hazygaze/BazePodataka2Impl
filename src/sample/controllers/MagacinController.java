package sample.controllers;

import com.sun.prism.shader.Solid_TextureFirstPassLCD_AlphaTest_Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import sample.db.DBBroker;
import sample.forms.AlertBox;
import sample.model.Grad;
import sample.model.Magacin;
import sample.model.Zaposleni;
import sample.tables.MagacinTableHandler;
import sample.tables.ZaposleniTableHandler;
import sample.util.Konstante;
import sample.util.MySQLException;

import java.util.stream.Collectors;


public class MagacinController {

    @FXML
    TextField txtBrMag;
    @FXML
    TextField txtNazivMag;
    @FXML
    TextField txtEmail;
    @FXML
    TextField txtTelefon;
    @FXML
    ComboBox<Grad> cmbGrad;

    private ObservableList<Grad> gradovi;
    private MagacinTableHandler mt;
    private Stage dialog;
    private Magacin magacin;
    private int akcija = 0;

    public void init(int action, MagacinTableHandler mt, Magacin mag, Stage dialog) {
        this.mt = mt;
        this.dialog = dialog;
        setupCmbBox();
        if(action == Konstante.INSERT) {
            this.magacin = new Magacin();
            akcija = Konstante.INSERT;
            System.out.println("Inserting::Zaposleni");
        } else {
            akcija = Konstante.EDIT;
            this.magacin = mag;
            edit();
        }
    }

    private void edit() {
        txtBrMag.setText(magacin.getBrojMagacina().toString());
        txtBrMag.setEditable(false);
        txtNazivMag.setText(magacin.getNazivMagacina());
        txtTelefon.setText(magacin.getTelefon());
        txtEmail.setText(magacin.getEmail());
        cmbGrad.getSelectionModel().select(magacin.getGrad());
    }


    public void close(ActionEvent actionEvent) {
    }

    public void save(ActionEvent actionEvent) {
        magacin.setNazivMagacina(txtNazivMag.getText());
        magacin.setTelefon(txtTelefon.getText());
        magacin.setEmail(txtEmail.getText());
        magacin.setGrad(cmbGrad.getSelectionModel().getSelectedItem());
        if(akcija == Konstante.INSERT) {
            try {
                magacin.setBrojMagacina(Integer.parseInt(txtBrMag.getText()));
                DBBroker.getInstance().sacuvajMagacin(magacin);
                DBBroker.getInstance().commit();
                mt.notifyDataChanged();
                dialog.close();
            } catch (MySQLException e) {
                System.out.println(e.getMessage());
                AlertBox.display("Error dialog", e.getMessage());
            }
        }
        if(akcija == Konstante.EDIT) {
            try {
                DBBroker.getInstance().updateMagacin(magacin);
                DBBroker.getInstance().commit();
                mt.notifyDataChanged();
                dialog.close();
            } catch (MySQLException e) {
                AlertBox.display("Error dialog", e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private ObservableList<Grad> ucitajGradove() {
        ObservableList<Grad> gradovi = null;
        try {
            gradovi = FXCollections.observableArrayList(DBBroker.getInstance().vratiSveGradove());
        } catch (MySQLException e) {
            e.printStackTrace();
            AlertBox.display("Error", e.getMessage());
        }
        return gradovi;
    }

    private void setupCmbBox() {
        cmbGrad.setItems(ucitajGradove());

        StringConverter<Grad> converter = new StringConverter<Grad>() {
            @Override
            public String toString(Grad grad) {
                return String.valueOf(grad.postbrProperty().get());
            }

            @Override
            public Grad fromString(String id) {
                return ucitajGradove().stream()
                        .filter(item -> item.postbrProperty().get() == Integer.parseInt(id))
                        .collect(Collectors.toList()).get(0);
            }
        };
        cmbGrad.setConverter(converter);
//
    }



}
