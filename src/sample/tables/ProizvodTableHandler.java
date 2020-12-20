package sample.tables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.DBBroker;
import sample.model.Proizvod;
import sample.model.Proizvod;

public class ProizvodTableHandler {

    private TableView table;
    private ObservableList<Proizvod> proizvodi;

    public ProizvodTableHandler(TableView table) {
        this.table = table;
        initTable();
    }

    public void initTable() {

        TableColumn<Proizvod, Integer> sifraProzivodaCol = new TableColumn<>("Šifra");
        sifraProzivodaCol.setMinWidth(200);
        sifraProzivodaCol.setCellValueFactory(new PropertyValueFactory<>("sifraProizvoda"));

        TableColumn<Proizvod, String> nazivCol = new TableColumn<>("Naziv");
        nazivCol.setMinWidth(200);
        nazivCol.setCellValueFactory(new PropertyValueFactory<>("nazivProizvoda"));

        TableColumn<Proizvod, Double> cenaCol = new TableColumn<>("Cena");
        cenaCol.setMinWidth(200);
        cenaCol.setCellValueFactory(new PropertyValueFactory<>("cenaProizvoda"));

        TableColumn<Proizvod, Integer> aktStanjeCol = new TableColumn<>("Na zalihama");
        aktStanjeCol.setMinWidth(200);
        aktStanjeCol.setCellValueFactory(new PropertyValueFactory<>("aktuelnoSNZ"));

        table.setItems(getProizvodi());
        table.getColumns().addAll(sifraProzivodaCol, nazivCol, cenaCol, aktStanjeCol);
    }

    public ObservableList<Proizvod> getProizvodi() {
        proizvodi =
                FXCollections.observableArrayList(DBBroker.getInstance().vratiSveProizvode());
        return proizvodi;
    }

}
