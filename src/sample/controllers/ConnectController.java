package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import sample.db.DBBroker;
import sun.rmi.runtime.Log;

import java.io.PrintWriter;

public class ConnectController {

    @FXML
    Button btnConnect;
    @FXML
    TextField txtUsername;
    @FXML
    TextField txtPassword;

    MainController mainController;

    public void initialize() throws Exception {
        mainController = new MainController();
    }


    public void connect() {
        //Opening the connection
        System.out.println("Opening the connection.");
        DBBroker.getInstance().openConnection("hazy","password");
        System.out.println("Opened the connection.");
        //Opening the MainForm;
        Stage stage = (Stage) btnConnect.getScene().getWindow();
        try {
            mainController.start(stage);
        } catch (Exception e) {
            System.out.println("Exception creating a new page.");
            e.printStackTrace();
        }
    }

    public TreeView initTree() {

        TreeItem root, tables, stats;

        root = new TreeItem("root");
        root.setExpanded(true);

        TreeView tree = new TreeView();

        tables = makeBranch("tables", root);
        makeBranch("Zaposleni", tables);
        makeBranch("Proizvod", tables);

        stats = makeBranch("stats", root);
        makeBranch("Graphs", stats);

        return tree;
    }

    public TreeItem<String> makeBranch(String title, TreeItem parent) {
        TreeItem item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }


}
