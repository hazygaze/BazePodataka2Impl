package sample.tables;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sample.db.DBBroker;
import sample.model.Kontakt;
import sample.model.Zaposleni;
import sample.util.MySQLException;

public class ZaposleniTableHandler {

    private TableView table;
    private ObservableList<Zaposleni> zaposleni;

    public ZaposleniTableHandler(TableView table) {
        this.table = table;
        zaposleni = getZaposleni();
        initTable();
    }

    public void initTable() {

        TableColumn<Zaposleni, String> jmbgCol = new TableColumn<>("JMBG");
        jmbgCol.setMinWidth(200);
        jmbgCol.setCellValueFactory(new PropertyValueFactory<>("jmbg"));

        TableColumn<Zaposleni, String> imePrezimeCol = new TableColumn<>("Ime i prezime");
        imePrezimeCol.setMinWidth(200);
        imePrezimeCol.setCellValueFactory(new PropertyValueFactory<>("imePrezime"));

        TableColumn<Zaposleni, String> telCol = new TableColumn<>("Telefon");
        telCol.setMinWidth(200);
        telCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zaposleni, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Zaposleni, String> param) {
                return param.getValue().getKontakt().telfonProperty();
            }
        });

        TableColumn<Zaposleni, String> faxCol = new TableColumn<>("Fax");
        faxCol.setMinWidth(200);
        faxCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zaposleni, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Zaposleni, String> param) {
                return param.getValue().getKontakt().faxProperty();
            }
        });

        TableColumn<Zaposleni, String> emailCol = new TableColumn<>("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Zaposleni, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Zaposleni, String> param) {
                return param.getValue().getKontakt().emailProperty();
            }
        });

        table.setItems(getZaposleni());
        table.getColumns().addAll(jmbgCol, imePrezimeCol, emailCol, telCol, faxCol);
    }

    public ObservableList<Zaposleni> getZaposleni() {
        ObservableList<Zaposleni> zaposleni =
                FXCollections.observableArrayList(DBBroker.getInstance().vratiSveZaposlene());
        return zaposleni;
    }

    public void notifyDataChanged() {
        zaposleni = getZaposleni();
        table.setItems(zaposleni);
        table.refresh();
    }

    public Zaposleni zaposleni(int pos) {
        return zaposleni.get(pos);
    }

    public void deleteZaposleni(Zaposleni z) {
        if(zaposleni.contains(z)) {
            try {
                DBBroker.getInstance().deleteZaposleni(z);
                DBBroker.getInstance().commit();
            } catch (MySQLException e) {
                e.printStackTrace();
            }
            notifyDataChanged();

        }
    }
}
