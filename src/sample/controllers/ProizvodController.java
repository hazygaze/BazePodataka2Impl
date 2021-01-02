package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.db.DBBroker;
import sample.forms.AlertBox;
import sample.model.Proizvod;
import sample.model.StanjeNaZalihama;
import sample.model.Zaposleni;
import sample.tables.ProizvodTableHandler;
import sample.tables.ZaposleniTableHandler;
import sample.util.Konstante;
import sample.util.MySQLException;
import sample.util.Status;

import javax.xml.soap.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ProizvodController {

    private int akcija = 0;
    boolean raise = false;
    Stage dialog;
    Proizvod proizvod;
    ObservableList<StanjeNaZalihama> stanjaZaProizvod;
    ProizvodTableHandler pt;

    @FXML
    TextField txtSifra;
    @FXML
    TextField txtNaziv;
    @FXML
    TextField txtCena;
    @FXML
    TextField txtNaStanju;
    @FXML
    TableView tblStanja;


    public void init(int action, ProizvodTableHandler pt, Proizvod proizvod, Stage dialog) {
        this.pt = pt;
        this.dialog = dialog;
        initTable();
        if(action == Konstante.INSERT) {
            this.proizvod = new Proizvod();
            akcija = Konstante.INSERT;
            System.out.println("Inserting::Proizvod");
        } else {
            akcija = Konstante.EDIT;
            this.proizvod = proizvod;
            tblStanja.setItems(getStanja());
            tblStanja.refresh();

            edit();
        }
    }

    private void initTable() {

        TableColumn<StanjeNaZalihama, Integer> sifraCol = new TableColumn<>("Šifra");
        sifraCol.setMinWidth(50);
        sifraCol.setCellValueFactory(new PropertyValueFactory<>("sifra"));

        TableColumn<StanjeNaZalihama, Integer> iznosCol = new TableColumn<>("Iznos");
        iznosCol.setMinWidth(50);
        iznosCol.setCellValueFactory(new PropertyValueFactory<>("iznos"));

        TableColumn<StanjeNaZalihama, String> dateCol = new TableColumn<>("Datum");
        dateCol.setMinWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datum"));

        TableColumn<StanjeNaZalihama, String> nazivProizvodaCol = new TableColumn<>("Naziv proizvoda");
        nazivProizvodaCol.setMinWidth(190);
        nazivProizvodaCol.setCellValueFactory(new PropertyValueFactory<>("nazivProizvoda"));

        tblStanja.setItems(stanjaZaProizvod);
        tblStanja.getColumns().addAll(sifraCol, iznosCol, dateCol, nazivProizvodaCol);
    }

    public ObservableList<StanjeNaZalihama> getStanja() {
        stanjaZaProizvod =
                FXCollections.observableArrayList(DBBroker.getInstance().vratiSvaStanja(proizvod.getSifraProizvoda()));
        return stanjaZaProizvod;
    }

    public void notifyDataChanged() {
        tblStanja.setItems(stanjaZaProizvod);
        tblStanja.refresh();
    }


    private void edit() {
        txtSifra.setText(String.valueOf(proizvod.getSifraProizvoda()));
        txtNaziv.setText(proizvod.getNazivProizvoda());
        txtCena.setText(proizvod.getCenaProizvoda().toString());
        //txtNaStanju.setText(proizvod.getAktuelnoSNZ().toString());
        stanjaZaProizvod = FXCollections.observableArrayList(proizvod.getStanja());

    }


    public void deleteStanje(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tblStanja.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        StanjeNaZalihama snz = (StanjeNaZalihama) tblStanja.getItems().get(row);
        proizvod.deleteStanje(snz);
        notifyDataChanged();

    }

    public void editStanje(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tblStanja.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        StanjeNaZalihama snz = (StanjeNaZalihama) tblStanja.getItems().get(row);
        updateFields();
        snz.setProizvod(proizvod);
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/StanjeNaZalihamaForm.fxml"));
            dialogScene = new Scene(loader.load(), 350, 370);

            StanjaNaZalihamaController sc = loader.getController();
            sc.init(Konstante.EDIT, snz, dialog, this);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStanje(ActionEvent actionEvent) {

        StanjeNaZalihama snz = new StanjeNaZalihama();
        snz.setStatus(Status.NEW);
        updateFields();
        snz.setProizvod(proizvod);
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/StanjeNaZalihamaForm.fxml"));
            dialogScene = new Scene(loader.load(), 350, 400);

            StanjaNaZalihamaController sc = loader.getController();
            sc.init(Konstante.INSERT, snz, dialog, this);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void close(ActionEvent actionEvent) {
        dialog.close();
    }

    public void save(ActionEvent actionEvent) {
        if(raise) {
            if(akcija == Konstante.INSERT) {
                proizvod.setSifraProizvoda(Integer.parseInt(txtSifra.getText()));
                updateFields();
                try {
                    DBBroker.getInstance().insertProizvodWithNaziv(proizvod);
                    DBBroker.getInstance().commit();
                    pt.notifyDataChanged();
                    dialog.close();
                } catch (MySQLException e) {
                    e.printStackTrace();
                    AlertBox.display("Error dialog", e.getMessage());
                }
            }
            if(akcija == Konstante.EDIT) {
                updateFields();
                try {
                    DBBroker.getInstance().editProizvodWithNaziv(proizvod);
                    DBBroker.getInstance().commit();
                    pt.notifyDataChanged();
                    dialog.close();
                } catch (MySQLException e) {
                    e.printStackTrace();
                    AlertBox.display("Error dialog", e.getMessage());
                }
            }
            return;
        }

        if(akcija == Konstante.INSERT) {
            proizvod.setSifraProizvoda(Integer.parseInt(txtSifra.getText()));
            updateFields();
            try {
                DBBroker.getInstance().insertProizvod(proizvod);
                DBBroker.getInstance().commit();
                pt.notifyDataChanged();
                dialog.close();
            } catch (MySQLException e) {
                e.printStackTrace();
                AlertBox.display("Error dialog", e.getMessage());
            }
        }
        if(akcija == Konstante.EDIT) {
            updateFields();
            try {
                DBBroker.getInstance().editProizvod(proizvod);
                DBBroker.getInstance().commit();
                pt.notifyDataChanged();
                dialog.close();
            } catch (MySQLException e) {
                e.printStackTrace();
                AlertBox.display("Error dialog", e.getMessage());
            }
        }
    }

    public void addToList(StanjeNaZalihama snz) {
        proizvod.getStanja().add(snz);
        stanjaZaProizvod = FXCollections.observableArrayList(proizvod.getStanja());
        notifyDataChanged();
    }

    private void updateFields() {
        proizvod.setNazivProizvoda(txtNaziv.getText());
        if(!txtCena.getText().isEmpty())proizvod.setCenaProizvoda(Double.parseDouble(txtCena.getText()));
        if(!txtSifra.getText().isEmpty())proizvod.setSifraProizvoda(Integer.parseInt(txtSifra.getText()));
    }

    public void editFromDialog(StanjeNaZalihama snz) {
        proizvod.editStanje(snz);
        stanjaZaProizvod = FXCollections.observableArrayList(proizvod.getStanja());
        notifyDataChanged();
    }

    public void setRaise(boolean raise) {
        this.raise = raise;
    }
}
