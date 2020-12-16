package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.db.DBBroker;
import sample.model.Zaposleni;

import java.util.List;

public class MainController {

    @FXML
    TreeView tvMenu;
    @FXML
    VBox treeBox;

    String string;

    public MainController() {
    }

    public void initialize() throws Exception {
        initTree();
    }

    public void start(Stage window) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample/forms/MainForm.fxml"));
        Scene scene =  new Scene(root, 800 ,600);
        window.setTitle("");
        window.setScene(scene);
        window.show();

    }

    public void initTree() {

        TreeItem root, zaposleni, proizvod;

        root = new TreeItem("root");
        root.setExpanded(true);

        zaposleni = makeBranch("Zaposleni", root);
        proizvod = makeBranch("Proizvod", root);

        tvMenu.setRoot(root);
        tvMenu.setShowRoot(false);

        tvMenu.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    TreeItem item = (TreeItem) newValue;
                    String value = (String) item.getValue();
                    otvoriTabelu(value);
                });

    }

    public TreeItem<String> makeBranch(String title, TreeItem parent) {
        TreeItem item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    private void otvoriTabelu(String value) {
        System.out.println("Pravim tabelu za "+value);
        List<Zaposleni> zaposleni = DBBroker.getInstance().vratiSveZaposlene();
        for(Zaposleni z: zaposleni) {
            System.out.println(z);
        }
    }
}
