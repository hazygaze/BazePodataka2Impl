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
import sample.model.Zaposleni;
import sample.tables.GradTableHandler;
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
    Button btnInsertZaposleni;
    //editZaposleni, deleteZaposleni;
    @FXML
    TableView tableGrad;
    GradTableHandler gt;
    @FXML
    TableView tableProizvodi;
    ProizvodTableHandler pt;

    public MainController() {
    }

    public void initialize() throws Exception {
        zt = new ZaposleniTableHandler(tableZaposleni);
        gt = new GradTableHandler(tableGrad);
        pt = new ProizvodTableHandler(tableProizvodi);
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
}
