package sample.tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.DBBroker;
import sample.model.Grad;

public class GradTableHandler {

    private TableView table;
    private ObservableList<Grad> gradovi;

    public GradTableHandler(TableView table) {
        this.table = table;
        initTable();
    }

    public void initTable() {

        TableColumn<Grad, Integer> postanskiBrCol = new TableColumn<>("Poštanski broj");
        postanskiBrCol.setMinWidth(200);
        postanskiBrCol.setCellValueFactory(new PropertyValueFactory<>("postanskiBroj"));

        TableColumn<Grad, String> nazivCol = new TableColumn<>("Naziv");
        nazivCol.setMinWidth(200);
        nazivCol.setCellValueFactory(new PropertyValueFactory<>("nazivGrada"));


        table.setItems(getGradovi());
        table.getColumns().addAll(postanskiBrCol, nazivCol);
    }

    public ObservableList<Grad> getGradovi() {
        ObservableList<Grad> gradovi =
                FXCollections.observableArrayList(DBBroker.getInstance().vratiSveGradove());
        return gradovi;
    }
}
