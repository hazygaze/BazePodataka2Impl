package sample.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.forms.AlertBox;
import sample.model.Magacin;
import sample.model.Proizvod;
import sample.model.Zaposleni;
import sample.tables.GradTableHandler;
import sample.tables.MagacinTableHandler;
import sample.tables.ProizvodTableHandler;
import sample.tables.ZaposleniTableHandler;
import sample.util.Konstante;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {


    @FXML
    TableView tableZaposleni;
    ZaposleniTableHandler zt;

    @FXML
    TableView tableGrad;
    GradTableHandler gt;
    @FXML
    TableView tableProizvodi;
    ProizvodTableHandler pt;

    @FXML
    TableView tableMagacini;
    MagacinTableHandler mt;

    public MainController() {
    }

    public void initialize() throws Exception {
        zt = new ZaposleniTableHandler(tableZaposleni);
        gt = new GradTableHandler(tableGrad);
        pt = new ProizvodTableHandler(tableProizvodi);
        mt = new MagacinTableHandler(tableMagacini);
    }

    public void start(Stage window) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/forms/MainForm.fxml"));
        Scene scene =  new Scene(root, 1200,800);
        window.setTitle("");
        window.setScene(scene);
        window.show();

    }


    public void insertZaposleni(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/ZaposleniForm.fxml"));
            dialogScene = new Scene(loader.load(), 350, 370);

            ZaposleniController zc = loader.getController();
            zc.init(Konstante.INSERT, zt, new Zaposleni(), dialog);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void editZaposleni(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tableZaposleni.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Zaposleni z = (Zaposleni) tableZaposleni.getItems().get(row);
        System.out.println("Editing::"+z);

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/ZaposleniForm.fxml"));
            dialogScene = new Scene(loader.load(), 350, 370);

            ZaposleniController zc = loader.getController();
            zc.init(Konstante.EDIT, zt, z, dialog);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteZaposleni(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tableZaposleni.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Zaposleni z = (Zaposleni) tableZaposleni.getItems().get(pos.getRow());
        zt.deleteZaposleni(z);
    }

    public void insertProizvod(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/ProizvodForm.fxml"));
            dialogScene = new Scene(loader.load(), 500, 430);

            ProizvodController pc = loader.getController();
            pc.init(Konstante.INSERT, pt, new Proizvod(), dialog);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editProizvod(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tableProizvodi.getSelectionModel().getSelectedCells().get(0);
        if(pos == null) {
            AlertBox.display("Error dialog", "Morate selektovati red.");
            return;
        }
        int row = pos.getRow();
        Proizvod p = (Proizvod) tableProizvodi.getItems().get(row);
        System.out.println("Editing::"+p);

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/ProizvodForm.fxml"));
            dialogScene = new Scene(loader.load(), 500, 430);

            ProizvodController pc = loader.getController();
            pc.init(Konstante.EDIT, pt, p, dialog);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteProizvod(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tableProizvodi.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Proizvod p = (Proizvod) tableProizvodi.getItems().get(pos.getRow());
        pt.deleteProizvod(p);
    }


    public void deleteMagacin(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tableMagacini.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Magacin m = (Magacin) tableMagacini.getItems().get(pos.getRow());
        mt.deleteMagacin(m);
    }

    public void editMagacin(ActionEvent actionEvent) {
        TablePosition pos = (TablePosition) tableMagacini.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Magacin m = (Magacin) tableMagacini.getItems().get(row);
        System.out.println("Editing::"+m);

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/MagacinForm.fxml"));
            dialogScene = new Scene(loader.load(), 350, 370);

            MagacinController mc = loader.getController();
            mc.init(Konstante.EDIT, mt, m, dialog);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertMaga(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        Scene dialogScene = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/forms/MagacinForm.fxml"));
            dialogScene = new Scene(loader.load(), 350, 370);

            MagacinController mc = loader.getController();
            mc.init(Konstante.INSERT, mt, new Magacin(), dialog);
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
