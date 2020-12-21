package sample.db;

import oracle.jdbc.proxy.annotation.Pre;
import sample.model.*;
import sample.util.MySQLException;

import javax.xml.transform.Result;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        String query = "select z.JMBG_ZAPOSLENOG, z.IME_PREZIME, z.kontakt.get_telefon(), z.KONTAKT.GET_FAX(), z.KONTAKT.GET_EMAIL() from ZAPOSLENI z";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Zaposleni z = new Zaposleni();
                z.setJmbg(rs.getString(1));
                z.setImePrezime(rs.getString(2));
                Kontakt k = new Kontakt();
                k.setTelefon(rs.getString(3));
                k.setFax(rs.getString(4));
                k.setEmail(rs.getString(5));
                z.setKontakt(k);
                zaposlenis.add(z);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return zaposlenis;
    }

    public List<Grad> vratiSveGradove() {
        List<Grad> gradovi = new ArrayList<>();
        String query = "select * from GRAD";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                int postanskiBr = rs.getInt("POSTANSKI_BROJ");
                String naziv = rs.getString("NAZIV_GRADA");
                Grad g = new Grad(postanskiBr,naziv);
                gradovi.add(g);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gradovi;
    }

    public List<Proizvod> vratiSveProizvode() {
        List<Proizvod> proizvods = new ArrayList<>();
        String query = "select * from PROIZVOD";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                int sifra = rs.getInt("SIFRA_PROIZVODA");
                double cena = rs.getDouble("CENA_PROIZVODA");
                String naziv = rs.getString("NAZIV_PROIZVODA");
                int aktuelno_snz = rs.getInt("AKTUELNO_SNZ");
                Proizvod p = new Proizvod(sifra,naziv,cena,aktuelno_snz);
                List<StanjeNaZalihama> stanjas = vratiSvaStanja(sifra);
                p.setStanja(stanjas);
                proizvods.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return proizvods;
    }

    public List<StanjeNaZalihama> vratiSvaStanja(int sifraProizvoda) {
        List<StanjeNaZalihama> stanjas = new ArrayList<>();
        String query = "select * from STANJE_NA_ZALIHAMA where SIFRA_PROIZVODA = " +sifraProizvoda;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                int sifra = rs.getInt("SIFRA_SNZ");
                int iznos = rs.getInt("IZNOS_SNZ");
                Date datum = new Date(rs.getDate("DATUM").getTime());
                Proizvod p = vratiProizvod(sifraProizvoda);
                String naziv_pr  = rs.getString("NAZIV_PROIZVODA");
                StanjeNaZalihama snz = new StanjeNaZalihama(sifra,iznos,datum,p,naziv_pr);
                stanjas.add(snz);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stanjas;
    }

    public Proizvod vratiProizvod(int sifraProizvoda) {
        Proizvod p = null;
        String query = "select * from PROIZVOD where SIFRA_PROIZVODA = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,sifraProizvoda);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                double cena = rs.getDouble("CENA_PROIZVODA");
                String naziv = rs.getString("NAZIV_PROIZVODA");
                int aktuelno_snz = rs.getInt("AKTUELNO_SNZ");
                p = new Proizvod(sifraProizvoda,naziv,cena,aktuelno_snz);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return p;
    }

    public void sacuvajZaposlenog(Zaposleni zap) throws MySQLException {
        String query = "insert into ZAPOSLENI values (?,?,KONTAKT(?,?,?))";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, zap.getJmbg());
            ps.setString(2,zap.getImePrezime());
            ps.setString(3, zap.getKontakt().getTelefon());
            ps.setString(4, zap.getKontakt().getFax());
            ps.setString(5, zap.getKontakt().getEmail());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Doslo je do greske prilikom cuvanja zaposlenog:\n" + throwables.getLocalizedMessage());
        }
    }

    public void updateZaposlenog(Zaposleni zap) {
        String query = "update ZAPOSLENI set IME_PREZIME = ?, KONTAKT = KONTAKT(?,?,?) where JMBG_ZAPOSLENOG = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, zap.getImePrezime());
            ps.setString(2, zap.getKontakt().getTelefon());
            ps.setString(3, zap.getKontakt().getFax());
            ps.setString(4, zap.getKontakt().getEmail());
            ps.setString(5, zap.getJmbg());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteZaposleni(Zaposleni z) throws MySQLException {
        String query = "delete from ZAPOSLENI where JMBG_ZAPOSLENOG = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, z.getJmbg());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Došlo je do greške prilikom brisanja zaposlenog: \n"+throwables.getMessage());
        }
    }

    public void deleteProizvod(Proizvod p) throws MySQLException {
        String query = "delete from PROIZVOD where SIFRA_PROIZVODA = "+p.getSifraProizvoda();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Došlo je do greške prilikom brisanja proizvoda: \n"+throwables.getMessage());
        }

    }

    public void insertProizvod(Proizvod proizvod) throws MySQLException {
        String query = "insert into PROIZVOD (SIFRA_PROIZVODA, NAZIV_PROIZVODA, CENA_PROIZVODA) values (?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,proizvod.getSifraProizvoda());
            ps.setString(2, proizvod.getNazivProizvoda());
            ps.setDouble(3,proizvod.getCenaProizvoda());

            int uspesno = ps.executeUpdate();
            ps.close();

            if(uspesno != 0) {
                for(StanjeNaZalihama snz: proizvod.getStanja()) {
                    String query2 = "insert into STANJE_NA_ZALIHAMA(SIFRA_SNZ,IZNOS_SNZ,DATUM,SIFRA_PROIZVODA) VALUES (?,?,TO_DATE(?,'DD.MM.YYY'),?)";
                    ps = connection.prepareStatement(query2);
                    ps.setInt(1,snz.getSifra());
                    ps.setInt(2,snz.getIznos());
                    ps.setString(3,new SimpleDateFormat("DD.MM.YYYY").format(snz.getDatum()));
                    ps.setInt(4,proizvod.getSifraProizvoda());
                    ps.executeUpdate();

                }
                ps.close();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Doslo je go greske prilikom cuvanja Proizvoda:\n"+throwables.getMessage());
        }
    }
}
