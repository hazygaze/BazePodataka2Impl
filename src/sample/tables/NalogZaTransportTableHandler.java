package sample.tables;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sample.db.DBBroker;
import sample.model.NalogZaTransport;
import sample.model.Zaposleni;
import sample.util.MySQLException;

public class NalogZaTransportTableHandler {

    private TableView table;
    private ObservableList<NalogZaTransport> nalozi;

    public NalogZaTransportTableHandler(TableView table) {
        this.table = table;
        nalozi = getNalozi();
        initTable();
    }

    public void initTable() {

        TableColumn<NalogZaTransport, Integer> sifraCol = new TableColumn<>("Šifra");
        sifraCol.setMinWidth(50);
        sifraCol.setCellValueFactory(new PropertyValueFactory<>("sifraNzt"));


        TableColumn<NalogZaTransport, String> opisCol = new TableColumn<>("Opis");
        opisCol.setMinWidth(200);
        opisCol.setCellValueFactory(new PropertyValueFactory<>("opis"));

        TableColumn<NalogZaTransport, String> dateCol = new TableColumn<>("Datum");
        dateCol.setMinWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datum"));

        TableColumn<NalogZaTransport, String> jmbgZapCol = new TableColumn<>("JMBG Zaposlenog");
        jmbgZapCol.setMinWidth(130);
        jmbgZapCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NalogZaTransport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NalogZaTransport, String> param) {
                return param.getValue().getZaposleni().jmbgProperty();
            }
        });

        TableColumn<NalogZaTransport, String> jmbgMagCol = new TableColumn<>("JMBG Magacionera");
        jmbgMagCol.setMinWidth(130);
        jmbgMagCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NalogZaTransport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NalogZaTransport, String> param) {
                return param.getValue().getMagacioner().jmbgProperty();
            }
        });

        TableColumn<NalogZaTransport, String> magCol = new TableColumn<>("Magacin");
        magCol.setMinWidth(100);
        magCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NalogZaTransport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NalogZaTransport, String> param) {
                return param.getValue().getMagacin().nazivMagacinaProperty();
            }
        });

        TableColumn<NalogZaTransport, String> proizvodCol = new TableColumn<>("Proizvod");
        proizvodCol.setMinWidth(130);
        proizvodCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<NalogZaTransport, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<NalogZaTransport, String> param) {
                return param.getValue().getProizvod().nazivProizvodaProperty();
            }
        });

        TableColumn<NalogZaTransport, String> imePrezCol = new TableColumn<>("ImePrezime Zap");
        imePrezCol.setMinWidth(200);
        imePrezCol.setCellValueFactory(new PropertyValueFactory<>("imePrezimeZap"));

        TableColumn<NalogZaTransport, String> nacinIsporukeCol = new TableColumn<>("Nacin isporuke");
        nacinIsporukeCol.setMinWidth(130);
        nacinIsporukeCol.setCellValueFactory(new PropertyValueFactory<>("nacinIsporuke"));

        table.setItems(getNalozi());
        table.getColumns().addAll(sifraCol, opisCol, proizvodCol,magCol,nacinIsporukeCol, dateCol, jmbgMagCol, jmbgZapCol,imePrezCol);
    }

    public ObservableList<NalogZaTransport> getNalozi() {
        ObservableList<NalogZaTransport> nalozi = null;
        try {
            nalozi = FXCollections.observableArrayList(DBBroker.getInstance().vratiSveNaloge());
        } catch (MySQLException e) {
            e.printStackTrace();
        }
        return nalozi;
    }

    public void notifyDataChanged() {
        nalozi = getNalozi();
        table.setItems(nalozi);
        table.refresh();
    }

    public NalogZaTransport nalozi(int pos) {
        return nalozi.get(pos);
    }

    public void deleteNalog(NalogZaTransport nzt) {
        if(nalozi.contains(nzt)) {
            try {
                DBBroker.getInstance().deleteNalog(nzt);
                DBBroker.getInstance().commit();
            } catch (MySQLException e) {
                e.printStackTrace();
            }
            notifyDataChanged();

        }
    }
}
