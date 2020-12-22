package sample.tables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sample.db.DBBroker;
import sample.forms.AlertBox;
import sample.model.Magacin;
import sample.util.MySQLException;

public class MagacinTableHandler {
    private TableView table;
    private ObservableList<Magacin> magacins;

    public MagacinTableHandler(TableView table) {
        this.table = table;
        magacins = getMagacini();
        initTable();
    }

    public void initTable() {

        TableColumn<Magacin, Integer> brojMgCol = new TableColumn<>("Broj magacina");
        brojMgCol.setMinWidth(200);
        brojMgCol.setCellValueFactory(new PropertyValueFactory<>("brojMagacina"));

        TableColumn<Magacin, String> nazivCol = new TableColumn<>("Naziv");
        nazivCol.setMinWidth(200);
        nazivCol.setCellValueFactory(new PropertyValueFactory<>("nazivMagacina"));

        TableColumn<Magacin, String> telCol = new TableColumn<>("Telefon");
        telCol.setMinWidth(200);
        telCol.setCellValueFactory(new PropertyValueFactory<>("telefon"));

        TableColumn<Magacin, String> emailCol = new TableColumn<>("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));


        TableColumn<Magacin, Integer> gradCol = new TableColumn<>("Email");
        gradCol.setMinWidth(200);
        gradCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Magacin, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Magacin, Integer> param) {
                ObservableValue<Integer> obsInt = new SimpleIntegerProperty(param.getValue().getGrad().getPostanskiBroj()).asObject();
                return obsInt;
            }
        });

        table.setItems(getMagacini());
        table.getColumns().addAll(brojMgCol, nazivCol, gradCol, emailCol, telCol);
    }

    public ObservableList<Magacin> getMagacini() {
        try {
            magacins = FXCollections.observableArrayList(DBBroker.getInstance().vratiSveMagacine());
        } catch (MySQLException e) {
            e.printStackTrace();
            AlertBox.display("Error", e.getMessage());
        }
        return magacins;
    }

    public void notifyDataChanged() {
        magacins = getMagacini();
        table.setItems(magacins);
        table.refresh();
    }

    public Magacin zaposleni(int pos) {
        return magacins.get(pos);
    }

    public void deleteMagacin(Magacin m) {
        if(magacins.contains(m)) {
            try {
                DBBroker.getInstance().deleteMagacin(m);
                DBBroker.getInstance().commit();
            } catch (MySQLException e) {
                e.printStackTrace();
                AlertBox.display("Error", e.getMessage());
            }
            notifyDataChanged();

        }
    }
}
