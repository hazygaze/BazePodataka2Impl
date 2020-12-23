package sample.db;

import sample.model.*;
import sample.util.Konstante;
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

    public List<Grad> vratiSveGradove() throws MySQLException {
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
            throw new MySQLException("Doslo je do greske prilikom ocitavanja gradova:\n" + ex.getLocalizedMessage());


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

    public List<Magacin> vratiSveMagacine() throws MySQLException {
        List<Magacin> magacini = new ArrayList<>();
        String query = "select * from MAGACIN_VIEW";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                int br_mag = rs.getInt("BROJ_MAGACINA");
                String naziv_mag = rs.getString("NAZIV_MAGACINA");
                String email = rs.getString("EMAIL");
                String tel = rs.getString("TELEFON");
                int postanskiBr = rs.getInt("POSTANSKI_BROJ");
                Grad g = ucitajGrad(postanskiBr);
                Magacin m = new Magacin(br_mag, naziv_mag, email, tel, g);
                magacini.add(m);
            }
        } catch (SQLException | MySQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom citanja magacina:\n" + ex.getLocalizedMessage());
        }
        return magacini;
    }

    public void sacuvajMagacin(Magacin m) throws MySQLException {
        String query = "insert into MAGACIN_VIEW values (?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, m.getBrojMagacina());
            ps.setString(2,m.getNazivMagacina());
            ps.setString(3, m.getTelefon());
            ps.setString(4, m.getEmail());
            ps.setInt(5, m.getGrad().getPostanskiBroj());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Doslo je do greske prilikom cuvanja magacina:\n" + throwables.getLocalizedMessage());
        }
    }

    public void updateMagacin(Magacin m) throws MySQLException {
        String query = "update MAGACIN_VIEW set NAZIV_MAGACINA = ?, EMAIL = ?, TELEFON = ?, POSTANSKI_BROJ = ? where BROJ_MAGACINA = "+m.getBrojMagacina();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,m.getNazivMagacina());
            ps.setString(2, m.getEmail());
            ps.setString(3, m.getTelefon());
            ps.setInt(4, m.getGrad().getPostanskiBroj());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Doslo je do greske prilikom azuiranja magacina: \n"+throwables.getMessage());
        }
    }

    public void sacuvajGrad(Grad g) throws MySQLException {
        String query = "insert into GRAD values (?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, g.getPostanskiBroj());
            ps.setString(2,g.getNazivGrada());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Doslo je do greske prilikom cuvanja grada:\n" + throwables.getLocalizedMessage());
        }
    }

    public void updateGrad(Grad g) throws MySQLException {
        String query = "update GRAD set NAZIV_GRADA = ? where POSTANSKI_BROJ = "+ g.getPostanskiBroj();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, g.getNazivGrada());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Doslo je do greske prilikom azuriranja grada: \n"+throwables.getMessage());
        }
    }

    public void deleteGrad(Grad g) throws MySQLException {
        String query = "delete from GRAD where POSTANSKI_BROJ = "+g.getPostanskiBroj();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Došlo je do greške prilikom brisanja grada: \n"+throwables.getMessage());
        }

    }

    public void deleteMagacin(Magacin m) throws MySQLException {
        String query = "delete from MAGACIN_VIEW where BROJ_MAGACINA = "+m.getBrojMagacina();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Došlo je do greške prilikom brisanja magacina: \n"+throwables.getMessage());
        }

    }

    public Grad ucitajGrad(Integer postanskiBroj) throws MySQLException {
        Grad g = new Grad();
        String query = "select * from GRAD where POSTANSKI_BROJ = " + postanskiBroj;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                g.setPostanskiBroj(rs.getInt(1));
                g.setNazivGrada(rs.getString(2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Došlo je do greške prilikom citanja grada: \n"+throwables.getMessage());
        }
        return g;
    }


    public List<NalogZaTransport> vratiSveNaloge() throws MySQLException {
        List<NalogZaTransport> nalozi = new ArrayList<>();
        String query = "select * from NALOG_ZA_TRANSPORT";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                int sifra = rs.getInt("SIFRA_NZT");
                String opis = rs.getString("OPIS");
                String datum = Konstante.vratiFormatiranDatum(new Date(rs.getDate("DATUM").getTime()));
                String ni = rs.getString("NACIN_ISPORUKE");
                int br_mag = rs.getInt("BROJ_MAGACINA");
                Magacin m = ucitajMagacin(br_mag);
                String jmbgZap = rs.getString("JMBG_ZAPOSLENOG");
                Zaposleni z = ucitajZaposlenog(jmbgZap);
                String jmbgMAg = rs.getString("JMBG_MAGACIONERA");
                Magacioner mag = ucitajMagacionera(jmbgMAg);
                mag.setMagacin(m);
                int sifraPr = rs.getInt("SIFRA_PROIZVODA");
                Proizvod p = vratiProizvod(sifraPr);
                String imePrezZap = rs.getString("IME_PREZIME_ZAPOSLENOG");
                NalogZaTransport nzt = new NalogZaTransport(sifra,opis,datum,mag,z,m,p,ni,imePrezZap);
                nalozi.add(nzt);
            }
        } catch (SQLException | MySQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom citanja naloga:\n" + ex.getLocalizedMessage());
        }
        return nalozi;
    }

    public void updateNalog(NalogZaTransport nzt) throws MySQLException {
        String query = "UPDATE NALOG_ZA_TRANSPORT SET SIFRA_NZT = ?, OPIS = ?, DATUM = TO_DATE(?,'dd.MM.yyyy'), JMBG_MAGACIONERA = ?, JMBG_ZAPOSLENOG = ? , SIFRA_PROIZVODA = ?, BROJ_MAGACINA = ?, NACIN_ISPORUKE = ? where SIFRA_NZT = " + nzt.getSifraNzt();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,nzt.getSifraNzt());
            ps.setString(2, nzt.getOpis());
            ps.setString(3,nzt.getDatum());
            ps.setString(4,nzt.getMagacioner().getJmbg());
            ps.setString(5,nzt.getZaposleni().getJmbg());
            ps.setInt(6,nzt.getProizvod().getSifraProizvoda());
            ps.setInt(7,nzt.getMagacin().getBrojMagacina());
            ps.setString(8,nzt.getNacinIsporuke());

            ps.executeUpdate();
        } catch (SQLException ex)  {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom citanja naloga:\n" + ex.getLocalizedMessage());
        }
    }

    public Magacin ucitajMagacin(int br_mag) throws MySQLException {
        Magacin m = null;
        String query = "select * from MAGACIN_VIEW where broj_magacina = "+br_mag;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                String naziv_mag = rs.getString("NAZIV_MAGACINA");
                String email = rs.getString("EMAIL");
                String tel = rs.getString("TELEFON");
                int postanskiBr = rs.getInt("POSTANSKI_BROJ");
                Grad g = ucitajGrad(postanskiBr);
                m = new Magacin(br_mag, naziv_mag, email, tel, g);
            }
        } catch (SQLException | MySQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom citanja magacina:\n" + ex.getLocalizedMessage());
        }
        return m;

    }

    public Zaposleni ucitajZaposlenog(String jmbg) throws MySQLException {
        Zaposleni z = new Zaposleni();
        String query = "select jmbg_zaposlenog, ime_prezime from ZAPOSLENI where JMBG_ZAPOSLENOG = "+jmbg;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                String jmbgZa = rs.getString("JMBG_ZAPOSLENOG");
                String imePRe = rs.getString("IME_PREZIME");
                z.setJmbg(jmbgZa);
                z.setImePrezime(imePRe);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom citanja zaposlenoga:\n" + ex.getLocalizedMessage());
        }
        return z;

    }

    public Magacioner ucitajMagacionera(String jmbg) throws MySQLException {
        Magacioner m = new Magacioner();
        String query = "select jmbg_magacionera, ime_prezime from MAGACIONER where JMBG_MAGACIONERA = "+jmbg;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                String jmbgZa = rs.getString("JMBG_MAGACIONERA");
                String imePRe = rs.getString("IME_PREZIME");
                m.setJmbg(jmbgZa);
                m.setImePrezime(imePRe);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom citanja magacionera:\n" + ex.getLocalizedMessage());
        }
        return m;

    }

    public void deleteNalog(NalogZaTransport nzt) throws MySQLException {
        String query = "delete from NALOG_ZA_TRANSPORT where SIFRA_NZT = "+nzt.getSifraNzt();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new MySQLException("Došlo je do greške prilikom brisanja magacina: \n"+throwables.getMessage());
        }
    }

    public List<Magacioner> vratiSveMagacionere() throws MySQLException {
        List<Magacioner> magacioneri = new ArrayList<>();
        String query = "select * from MAGACIONER";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                String jmbgZa = rs.getString("JMBG_MAGACIONERA");
                String imePRe = rs.getString("IME_PREZIME");
                Magacin magacin = ucitajMagacin(rs.getInt("BROJ_MAGACINA"));
                Magacioner m = new Magacioner(jmbgZa, imePRe,magacin);
                magacioneri.add(m);
            }
        } catch (SQLException | MySQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom citanja magacionera:\n" + ex.getLocalizedMessage());
        }
        return magacioneri;
    }


    public void insertNalog(NalogZaTransport nzt) throws MySQLException {
        String query = "insert into NALOG_ZA_TRANSPORT (SIFRA_NZT, OPIS, DATUM, JMBG_MAGACIONERA, JMBG_ZAPOSLENOG, SIFRA_PROIZVODA, BROJ_MAGACINA, NACIN_ISPORUKE) VALUES(?,?,TO_DATE(?,'dd.MM.yyyy'),?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,nzt.getSifraNzt());
            ps.setString(2, nzt.getOpis());
            ps.setString(3,nzt.getDatum());
            ps.setString(4,nzt.getMagacioner().getJmbg());
            ps.setString(5,nzt.getZaposleni().getJmbg());
            ps.setInt(6,nzt.getProizvod().getSifraProizvoda());
            ps.setInt(7,nzt.getMagacin().getBrojMagacina());
            ps.setString(8,nzt.getNacinIsporuke());

            ps.executeUpdate();
        } catch (SQLException ex)  {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
            throw new MySQLException("Doslo je do greske prilikom cuvanja naloga:\n" + ex.getLocalizedMessage());
        }

    }

}