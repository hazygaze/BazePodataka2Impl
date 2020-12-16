package sample.db;

import sample.model.Zaposleni;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.sql.DriverManager.getConnection;

public class DBBroker {

    private Connection connection;
    private static DBBroker instance;

    public DBBroker() {

    }

    public static DBBroker getInstance() {
        if(instance == null) {
            instance = new DBBroker();
        }
        return instance;
    }

    public void openConnection(String username, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = getConnection("jdbc:oracle:thin:@localhost:1521:xe",username,password);
            connection.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

   public void rollback() {
       try {
           connection.rollback();
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
   }

    public List<Zaposleni> vratiSveZaposlene() {
        List<Zaposleni> zaposlenis = new ArrayList<>();
        String query = "select * from ZAPOSLENI";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                String jmbg = rs.getString("JMBG_ZAPOSLENOG");
                String imePrezime = rs.getString("IME_PREZIME");
                Zaposleni z = new Zaposleni(jmbg,imePrezime);
                zaposlenis.add(z);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return zaposlenis;
    }
}
