package com.rentapp.util;

import com.rentapp.dBObject.Accessory;
import com.rentapp.dBObject.Rent;
import com.rentapp.gui.scene.LoginCtrl;
import com.rentapp.gui.scene.SceneCtrl;
import com.rentapp.table.ClientRow;
import com.rentapp.table.EquipRow;
import com.rentapp.table.UserRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DBQuery {
    public static ResultSet queryRentalTable() throws SQLException {
        String querry = "SELECT k.imie_nazwisko, IFNULL(k.nazwa_firma, \"\") , s.model, s.id_sprzet, IFNULL(w.uwagi, \"\"), w.od, w.przew_data_zw, w.przez_kogo, w.id_klient, w.id_wypozyczenie, w.dlugoterminowe, w.za_d_netto, IFNULL(w.kaucja_brutto, \"0\"), k.pesel_nip, w.platnosc_kaucji, w.nr_wypozycz_klient, w.nr_wypozycz_klient_rok " +
                "FROM Wypozyczenie w " +
                "JOIN Klient k ON w.id_klient = k.id_klient " +
                "JOIN Wypozyczenie_sprzet ws ON w.id_wypozyczenie = ws.id_wypozyczenie " +
                "JOIN Sprzet s ON ws.id_sprzet = s.id_sprzet " +
                "WHERE ws.do IS NULL";

        try{
            //TODO closing statement
            PreparedStatement statement = DBConnection.getConnection().prepareStatement(querry);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet queryHistoryRentalTable(){
        String querry = "SELECT k.imie_nazwisko, IFNULL(k.nazwa_firma, \"\") , s.model, s.id_sprzet, IFNULL(w.uwagi, \"\"), w.od, ws.do, w.przez_kogo, w.id_klient, w.id_wypozyczenie, w.dlugoterminowe, w.za_d_netto, IFNULL(w.kaucja_brutto, \"0\"), k.pesel_nip, w.platnosc_kaucji, w.nr_wypozycz_klient, w.nr_wypozycz_klient_rok " +
                "FROM Wypozyczenie w " +
                "JOIN Klient k ON w.id_klient = k.id_klient " +
                "JOIN Wypozyczenie_sprzet ws ON w.id_wypozyczenie = ws.id_wypozyczenie " +
                "JOIN Sprzet s ON ws.id_sprzet = s.id_sprzet " +
                "WHERE ws.do IS NOT NULL";
        try{
            //TODO closing statement
            PreparedStatement statement = DBConnection.getConnection().prepareStatement(querry);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet queryAccessoriesName(String rentalID) throws SQLException {
            String querry = "SELECT a.nazwa, IFNULL(wa.stopien_zuzycia,\"\") " +
                    "FROM Akcesoria a " +
                    "JOIN Wypozyczenie_akcesoria wa ON wa.id_akcesoria = a.id_akcesoria " +
                    "WHERE wa.id_wypozyczenie = ?";
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                //TODO closing statement
                statement = DBConnection.getConnection().prepareStatement(querry);
                statement.setString(1, rentalID);
                resultSet = statement.executeQuery();
                return resultSet;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
//            } finally {
//                if (statement != null) {
//                    try {
//                        statement.close();
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
    }

    public static ResultSet queryClientTable() throws SQLException {
        String querry = "SELECT imie_nazwisko, IFNULL(nazwa_firma, \"\"), pesel_nip, adres, dw_osob, telefon, od, nr_umowy, nr_umowy_rok, id_klient, bez_kaucji " +
                "FROM Klient";
        PreparedStatement statement = null;
        try {
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } /*finally {
            if (statement != null) { statement.close(); }
        }*/
    }

    public static ResultSet queryEquipTable() throws SQLException {
        String querry = "SELECT model, id_sprzet, nazwa, kaucja_brutto, za_d_netto, rok_produkcji, nr_seryjny, data_przegladu, przeglad_ilosc_motogodzin, wartosc_brutto, sprzedany, dostepny " +
                "FROM Sprzet";
        PreparedStatement statement = null;
        try {
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet queryAvailableEquipTable() throws SQLException {
        String querry = "SELECT model, id_sprzet, nazwa, kaucja_brutto, za_d_netto, rok_produkcji, nr_seryjny, data_przegladu, przeglad_ilosc_motogodzin, wartosc_brutto, sprzedany, dostepny " +
                "FROM Sprzet " +
                "WHERE dostepny = true AND sprzedany IS NULL";
        PreparedStatement statement = null;
        try {
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet queryAccessoryTable() throws SQLException {
        String querry = "SELECT nazwa, cena_netto, zuzywalne, id_akcesoria, info " +
                "FROM Akcesoria";
        PreparedStatement statement = null;
        try {
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet queryAccessories(String equipID){
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String querry = "SELECT a.id_akcesoria, a.nazwa, a.cena_netto, a.zuzywalne, sa.ilosc_akcesoria, a.info " +
                    "FROM Akcesoria a " +
                    "JOIN Sprzet_akcesoria sa ON sa.id_akcesoria = a.id_akcesoria " +
                    "WHERE sa.id_sprzet = ?";
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);
            statement.setString(1, equipID);
            resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ClientRow queryClientData(String clientID){
        String query = "SELECT imie_nazwisko, IFNULL(nazwa_firma, \"\"), pesel_nip, adres, dw_osob, telefon, od, nr_umowy, nr_umowy_rok, bez_kaucji " +
                "FROM Klient " +
                "WHERE id_klient = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){

            statement.setInt(1, Integer.parseInt(clientID));
            ResultSet resultSet = statement.executeQuery();
            ClientRow clientRow = new ClientRow();
            resultSet.next();
            clientRow.setClientID(clientID);
            clientRow.setName(resultSet.getString(1));
            clientRow.setCompany(resultSet.getString(2));
            clientRow.setPeselNip(resultSet.getString(3));
            clientRow.setAdress(resultSet.getString(4));
            clientRow.setIdentityCard(resultSet.getString(5));
            clientRow.setPhoneNumber(resultSet.getString(6));
            clientRow.setFromWhen(resultSet.getString(7));
            clientRow.setContractNumber(resultSet.getString(8) + "/" + resultSet.getString(9).charAt(2) + resultSet.getString(9).charAt(3));
            clientRow.setDepositFree(resultSet.getBoolean(10));

            return clientRow;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Integer, EquipRow> queryRentedEquip(String rentalID){
        String query = "SELECT s.model, s.id_sprzet, s.nazwa, s.kaucja_brutto, s.za_d_netto, s.rok_produkcji, s.nr_seryjny, s.data_przegladu, s.przeglad_ilosc_motogodzin, s.wartosc_brutto " +
                "FROM Sprzet s " +
                "JOIN Wypozyczenie_sprzet ws ON ws.id_sprzet = s.id_sprzet " +
                "WHERE ws.id_wypozyczenie = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, Integer.parseInt(rentalID));
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, EquipRow> rentedIDEquip = new HashMap<>();
            while(resultSet.next()){
                EquipRow equipRow = new EquipRow();
                equipRow.setModel(resultSet.getString(1));
                equipRow.setEquipmentID(resultSet.getString(2));
                equipRow.setName(resultSet.getString(3));
                equipRow.setDeposit(resultSet.getString(4));
                equipRow.setPerDayNet(resultSet.getString(5));
                equipRow.setManufactureYear(resultSet.getString(6));
                equipRow.setSn(resultSet.getString(7));
                equipRow.setReviewDate(resultSet.getString(8));
                equipRow.setMotohours(resultSet.getString(9));
                equipRow.setEquipValueGross(resultSet.getInt(10));
                rentedIDEquip.put(Integer.parseInt(equipRow.getEquipmentID()), equipRow);
            }
            return rentedIDEquip;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Integer, Accessory> queryRentedAccessory(String rentalID){
        String query = "SELECT a.id_akcesoria, a.nazwa, IFNULL(wa.cena_netto, a.cena_netto), a.zuzywalne, a.info " +
                "FROM Akcesoria a " +
                "JOIN Wypozyczenie_akcesoria wa ON wa.id_akcesoria = a.id_akcesoria " +
                "WHERE wa.id_wypozyczenie = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, Integer.parseInt(rentalID));
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, Accessory> rentedIDAccessory = new HashMap<>();
            while(resultSet.next()){
                Accessory accessory = new Accessory(resultSet.getInt(1));
                accessory.setName(resultSet.getString(2));
                accessory.setPriceNett(resultSet.getDouble(3));
                accessory.setIsUsable(resultSet.getBoolean(4));
                accessory.setInfo(resultSet.getString(5));
                rentedIDAccessory.put(accessory.getAccessoryID(), accessory);
            }
            return rentedIDAccessory;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String queryRentedEquipPlaceOfWork(String rentalID){
        String query = "SELECT miejsce_pracy " +
                "FROM Wypozyczenie " +
                "WHERE id_wypozyczenie = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, Integer.parseInt(rentalID));
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Map<Integer, Integer> queryRentedAccessoryQty(String rentalID){
        String query = "SELECT id_akcesoria, ilosc " +
                "FROM Wypozyczenie_akcesoria " +
                "WHERE id_wypozyczenie = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, Integer.parseInt(rentalID));
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, Integer> rentedIDAccessoryCount = new HashMap<>();
            while(resultSet.next()){
                rentedIDAccessoryCount.put(resultSet.getInt(1), resultSet.getInt(2));
            }
            return rentedIDAccessoryCount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String queryClientContractNumber(int clientID){
        String query = "SELECT nr_umowy, nr_umowy_rok " +
                "FROM Klient " +
                "WHERE id_klient = ?";
        try(PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, clientID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString(1) + "/" + resultSet.getString(2).charAt(2) + resultSet.getString(2).charAt(3);
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error querying client contract number");
        }
        return null;
    }

    public static String queryNewContractNumber(){

        if (checkForNewYear()){
            return "1";
        } else {
            try{
                PreparedStatement statement = null;
                ResultSet resultSet = null;
                String querry = "SELECT MAX(nr_umowy) "+
                        "FROM Klient " +
                        "WHERE nr_umowy_rok = ?";
                statement = DBConnection.getConnection().prepareStatement(querry);

                statement.setString(1, Year.now().toString());
                resultSet = statement.executeQuery();
                resultSet.next();
                return resultSet.getInt(1) +1 + "";
            } catch (SQLException e){
                e.printStackTrace();
                System.out.println("Error querying new contract number");
            }
        }
        return null;
    }

    public static int queryNewRentalID(){
        try{
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            String querry = "SELECT MAX(id_wypozyczenie) "+
                    "FROM Wypozyczenie";
            statement = DBConnection.getConnection().prepareStatement(querry);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) +1;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error querying new rental ID");
        }
        return -1;
    }

    public static int queryClientNewRentID(String clientID){
        String querry = "SELECT COUNT(1)"+
                    "FROM Wypozyczenie " +
                    "WHERE id_klient = ? AND nr_wypozycz_klient_rok = ?";
        try(PreparedStatement statement = DBConnection.getConnection().prepareStatement(querry)){
            ResultSet resultSet = null;
            String year = Year.now().toString();
            statement.setString(1, clientID);
            statement.setInt(2, Integer.parseInt(year));
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) +1;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error querying new client rent ID");
        }
        return -1;
    }







    public static String addClient(ClientRow client) {
        String querry = "INSERT INTO Klient (imie_nazwisko, pesel_nip, adres, dw_osob, telefon, nazwa_firma, od, nr_umowy_rok, nr_umowy, bez_kaucji) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(querry);){
            statement.setString(1, client.getName());
            statement.setString(2, client.getPeselNip());
            statement.setString(3, client.getAdress());
            statement.setString(4, client.getIdentityCard());
            statement.setString(5, client.getPhoneNumber());
            statement.setString(6, client.getCompany());
            statement.setString(7, client.getFromWhen()); //TODO
            statement.setInt(8, Integer.parseInt(Year.now().toString()));
            statement.setInt(9, client.getContractNumberInt());
            statement.setBoolean(10, client.isDepositFree());
            statement.executeUpdate();
            querry = "SELECT MAX(id_klient) " +
                    "FROM Klient ";
            try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(querry)){
                ResultSet resultSet = statement0.executeQuery();
                resultSet.next();
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteClient(ClientRow clientRow){
        String query0 = "DELETE FROM Wypozyczenie_sprzet " +
                "WHERE id_wypozyczenie IN (SELECT id_wypozyczenie " +
                "FROM Wypozyczenie " +
                "WHERE id_klient = ?)";
        String query1 = "DELETE FROM Wypozyczenie_akcesoria " +
                "WHERE id_wypozyczenie IN (SELECT id_wypozyczenie " +
                "FROM Wypozyczenie " +
                "WHERE id_klient = ?)";
        String query2 = "DELETE FROM Wypozyczenie " +
                "WHERE id_klient = ?";
        String query3 = "DELETE FROM Klient " +
                "WHERE id_klient = ?";
        try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query0)){
            statement0.setInt(1, clientRow.getClientIDInt());
            statement0.executeUpdate();
            try(PreparedStatement statement1 = DBConnection.getConnection().prepareStatement(query1)){
                statement1.setInt(1, clientRow.getClientIDInt());
                statement1.executeUpdate();
                try(PreparedStatement statement2 = DBConnection.getConnection().prepareStatement(query2)){
                    statement2.setInt(1, clientRow.getClientIDInt());
                    statement2.executeUpdate();
                    try(PreparedStatement statement3 = DBConnection.getConnection().prepareStatement(query3)){
                        statement3.setInt(1, clientRow.getClientIDInt());
                        statement3.executeUpdate();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się usunąć klienta. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean addEquip(EquipRow equipRow){
        String query = "INSERT INTO Sprzet (id_sprzet, nazwa, model, za_d_netto, kaucja_brutto, rok_produkcji, nr_seryjny, wartosc_brutto, data_przegladu, przeglad_ilosc_motogodzin, sprzedany, dostepny, w_serwisie) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, equipRow.getEquipmentIDInt());
            statement.setString(2, equipRow.getName());
            statement.setString(3, equipRow.getModel());
            statement.setDouble(4, equipRow.getPerDayNetDouble());
            statement.setInt(5, equipRow.getDepositGrossInt());
            if(equipRow.getManufactureYear().isEmpty()){
                statement.setString(6, null);
            } else {
                statement.setInt(6, Integer.parseInt(equipRow.getManufactureYear()));
            }
            if(equipRow.getSn().isEmpty()){
                statement.setString(7, null);
            } else {
                statement.setString(7, equipRow.getSn());
            }
            statement.setInt(8, equipRow.getEquipValueGrossInt());
            if(equipRow.getReviewDate().isEmpty()){
                statement.setDate(9, null);
            } else {
                statement.setDate(9, equipRow.getReviewDateSQL());
            }
            if(equipRow.getMotohours().isEmpty()){
                statement.setString(10, null);
            } else {
                statement.setString(10, equipRow.getMotohours());
            }
            statement.setDate(11, null);
            statement.setBoolean(12, true);
            statement.setBoolean(13, false);
            statement.executeUpdate();
            query = "INSERT INTO Sprzet_akcesoria (id_sprzet, id_akcesoria, ilosc_akcesoria) " +
                    "VALUES (?, ?, ?)";
            try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query)){
                Map<Integer, Accessory> iDAccessory = equipRow.getIDAccessory();
                Map<Integer, Integer> iDAccQty = equipRow.getAccessoryIDQty();
                for(int accID: iDAccessory.keySet()){
                    statement0.setInt(1, equipRow.getEquipmentIDInt());
                    statement0.setInt(2, accID);
                    statement0.setInt(3, iDAccQty.get(accID));
                    statement0.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się dodać sprzętu. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean editClient(ClientRow clientRow){
        String querry = "UPDATE Klient " +
                "SET imie_nazwisko = ?, pesel_nip = ?, adres = ?, dw_osob = ?, telefon = ?, nazwa_firma = ?, od = ?, nr_umowy = ?, bez_kaucji = ? " +
                "WHERE id_klient = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(querry)){
            statement.setString(1, clientRow.getName());
            statement.setString(2, clientRow.getPeselNip());
            statement.setString(3, clientRow.getAdress());
            statement.setString(4, clientRow.getIdentityCard());
            statement.setString(5, clientRow.getPhoneNumber());
            statement.setString(6, clientRow.getCompany());
            statement.setString(7, clientRow.getFromWhen());
            statement.setInt(8, clientRow.getContractNumberInt());
            statement.setBoolean(9, clientRow.isDepositFree());
            statement.setInt(10, clientRow.getClientIDInt());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się zaktualizować klienta. Błąd połączenia z bazą danych");
        }
        return false;
    }


    public static boolean updateEquip(EquipRow equipRow){
        String query = "UPDATE Sprzet " +
                "SET nazwa = ?, model = ?, za_d_netto = ?, kaucja_brutto = ?, rok_produkcji = ?, nr_seryjny = ?, wartosc_brutto = ?, data_przegladu = ?, przeglad_ilosc_motogodzin = ? " +
                "WHERE id_sprzet = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setString(1, equipRow.getName());
            statement.setString(2, equipRow.getModel());
            statement.setDouble(3, equipRow.getPerDayNetDouble());
            statement.setInt(4, equipRow.getDepositGrossInt());
            if(equipRow.getManufactureYear().isEmpty()){
                statement.setString(5, null);
            } else {
                statement.setInt(5, Integer.parseInt(equipRow.getManufactureYear()));
            }
            if(equipRow.getSn().isEmpty()){
                statement.setString(6, null);
            } else {
                statement.setString(6, equipRow.getSn());
            }
            statement.setInt(7, equipRow.getEquipValueGrossInt());
            if(equipRow.getReviewDate().isEmpty()){
                statement.setDate(8, null);
            } else {
                statement.setDate(8, equipRow.getReviewDateSQL());
            }
            if(equipRow.getMotohours().isEmpty()){
                statement.setString(9, null);
            } else {
                statement.setString(9, equipRow.getMotohours());
            }
            statement.setInt(10, equipRow.getEquipmentIDInt());
            statement.executeUpdate();
            query = "DELETE FROM Sprzet_akcesoria " +
                    "WHERE id_sprzet = ?";
            try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query)) {
                statement0.setInt(1, equipRow.getEquipmentIDInt());
                statement0.executeUpdate();
                query = "INSERT INTO Sprzet_akcesoria (id_sprzet, id_akcesoria, ilosc_akcesoria) " +
                        "VALUES (?, ?, ?)";
                try (PreparedStatement statement1 = DBConnection.getConnection().prepareStatement(query)) {
                    Map<Integer, Accessory> iDAccessory = equipRow.getIDAccessory();
                    Map<Integer, Integer> iDAccQty = equipRow.getAccessoryIDQty();
                    for (int accID : iDAccessory.keySet()) {
                        statement1.setInt(1, equipRow.getEquipmentIDInt());
                        statement1.setInt(2, accID);
                        statement1.setInt(3, iDAccQty.get(accID));
                        statement1.executeUpdate();
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się zaktualizować sprzętu. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean deleteEquip(EquipRow equipRow){
        String query0 = "DELETE FROM Wypozyczenie_sprzet " +
                "WHERE id_sprzet = ?";
        String query1 = "DELETE FROM Sprzet_akcesoria " +
                "WHERE id_sprzet = ?";
        String query2 = "DELETE FROM Sprzet " +
                "WHERE id_sprzet = ?";
        try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query0)){
            statement0.setInt(1, equipRow.getEquipmentIDInt());
            statement0.executeUpdate();
            try(PreparedStatement statement1 = DBConnection.getConnection().prepareStatement(query1)){
                statement1.setInt(1, equipRow.getEquipmentIDInt());
                statement1.executeUpdate();
                try(PreparedStatement statement2 = DBConnection.getConnection().prepareStatement(query2)){
                    statement2.setInt(1, equipRow.getEquipmentIDInt());
                    statement2.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się usunąć sprzętu. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean markSold(EquipRow equipRow){
        String query = "UPDATE Sprzet " +
                "SET sprzedany = NOW(), dostepny = false " +
                "WHERE id_sprzet = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setInt(1, equipRow.getEquipmentIDInt());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się zaktualizować sprzętu. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean addAccessory(Accessory accessory){
        String query = "INSERT INTO Akcesoria (nazwa, cena_netto, zuzywalne, info) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setString(1, accessory.getName());
            statement.setDouble(2, accessory.getPriceNettDouble());
            statement.setBoolean(3, accessory.isUsable());
            statement.setString(4, accessory.getInfo());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się dodać akcesorium. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean updateAccessory(Accessory accessory){
        String query = "UPDATE Akcesoria " +
                "SET nazwa = ?, cena_netto = ?, zuzywalne = ?, info = ? " +
                "WHERE id_akcesoria = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setString(1, accessory.getName());
            statement.setDouble(2, accessory.getPriceNettDouble());
            statement.setBoolean(3, accessory.isUsable());
            statement.setString(4, accessory.getInfo());
            statement.setInt(5, accessory.getAccessoryID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się zaktualizować akcesorium. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean deleteAccessory(int accID){
        String query0 = "DELETE FROM Wypozyczenie_akcesoria " +
                "WHERE id_akcesoria = ?";
        String query1 = "DELETE FROM Akcesoria " +
                "WHERE id_akcesoria = ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query0)){
            statement.setString(1, accID + "");
            statement.executeUpdate();
            try(PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query1)){
                statement0.setString(1, accID + "");
                statement0.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się usunąć akcesorium. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean checkClientConstraints(ClientRow client) {
        String querry = "SELECT COUNT(1) " +
                "FROM Klient " +
                "WHERE pesel_nip = ? OR dw_osob = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);
            statement.setString(1, client.getPeselNip());
            statement.setString(2, client.getIdentityCard());
            resultSet = statement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkClientPeselNip(ClientRow clientRow){
        String querry = "SELECT COUNT(1) " +
                "FROM Klient " +
                "WHERE pesel_nip = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(querry)){
            statement.setString(1, clientRow.getPeselNip());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkClientIdentityCard(ClientRow clientRow){
        String querry = "SELECT COUNT(1) " +
                "FROM Klient " +
                "WHERE dw_osob = ?";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(querry)){
            statement.setString(1, clientRow.getIdentityCard());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkEquipConstraints(String equipID){
        String querry = "SELECT COUNT(1) " +
                "FROM Sprzet " +
                "WHERE id_sprzet = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);
            statement.setString(1, equipID);

            resultSet = statement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int querryNewEquipID(){
        String query = "SELECT t1.id_sprzet + 1 AS missing_id "+
                "FROM Sprzet t1 "+
                "LEFT JOIN Sprzet t2 ON t1.id_sprzet + 1 = t2.id_sprzet " +
                "WHERE t2.id_sprzet IS NULL " +
                "  AND t1.id_sprzet < (SELECT MAX(id_sprzet) FROM Sprzet) " +
                "ORDER BY t1.id_sprzet";

        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            } else {
                query = "SELECT MAX(id_sprzet) " +
                        "FROM Sprzet";
                try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query)){
                    resultSet = statement0.executeQuery();
                    resultSet.next();
                    return resultSet.getInt(1) + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się pobrać nowego ID sprzętu. Błąd połączenia z bazą danych");
        }
        return -1;
    }



    public static boolean addRent(Rent rent) {
        String updateRentTable = "INSERT INTO Wypozyczenie (od, Id_klient, uwagi, przez_kogo, przew_data_zw, kaucja_brutto, za_d_netto, nr_wypozycz_klient, nr_wypozycz_klient_rok, dlugoterminowe, miejsce_pracy, platnosc_kaucji) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(updateRentTable);){
            statement.setTimestamp(1, rent.getFromWhen());
            statement.setInt(2, rent.getClientID());
            statement.setString(3, rent.getNote());
            statement.setString(4, LoginCtrl.getLoggedUser());
            statement.setDate(5, rent.getProbableToWhen());
            statement.setInt(6, rent.getDepositGross());
            statement.setDouble(7, rent.getTotPerDayNett());
            statement.setInt(8, rent.getClientRentID());
            statement.setInt(9, Integer.parseInt(Year.now().toString()));
            statement.setBoolean(10, rent.isLongTerm());
            statement.setString(11, rent.getPlaceOfWork());
            statement.setString(12, rent.getDepositPayment());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in addRent");
            return false;
        }

        Set<Integer> equipIDs = rent.getIDEquip().keySet();
        for(int iD: equipIDs){
            String addToRent = "UPDATE Sprzet " +
                    "SET dostepny = false " +
                    "WHERE id_sprzet = ?";
            try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(addToRent);){
                statement.setInt(1, iD);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in addRent");
                return false;
            }

            addToRent = "INSERT INTO Wypozyczenie_sprzet (id_wypozyczenie, id_sprzet, do) " +
                    "VALUES (?, ?, ?)";
            try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(addToRent);){
                statement.setInt(1, rent.getRentalID());
                statement.setInt(2, iD);
                statement.setTimestamp(3, null);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in addRent");
                return false;
            }
        }

        Map<Integer, Accessory> iDAccessory = rent.getIDAccessory();
        Map<Integer, Integer> iDAccQty = rent.getAccessoryIDQty();
        for(int iD: iDAccessory.keySet()){
            String addToRent = "INSERT INTO Wypozyczenie_akcesoria " +
                    "(id_wypozyczenie, id_akcesoria, stopien_zuzycia, ilosc) " +
                    "VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(addToRent);){
                statement.setInt(1, rent.getRentalID());
                statement.setInt(2, iD);
                if(iDAccessory.get(iD).isUsable()){
                    statement.setString(3, iDAccessory.get(iD).getUsage());
                } else {
                    statement.setString(3, null);
                }
                statement.setInt(4, iDAccQty.get(iD));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in addRent");
                return false;
            }
        }

        return true;
    }




    public static void setReturned(String equipmentID) throws SQLException{

        String updateRentTable = "UPDATE Wypozyczenie_sprzet " +
                "SET do = NOW() " +
                "WHERE id_sprzet = ?";


        String updateEquipment = "UPDATE Sprzet " +
                "SET dostepny = true " +
                "WHERE id_sprzet = ?";

        try {
            //TODO closing statement
            PreparedStatement statement = DBConnection.getConnection().prepareStatement(updateRentTable);
            statement.setString(1, equipmentID);
            statement.executeUpdate();
            statement = DBConnection.getConnection().prepareStatement(updateEquipment);
            statement.setString(1, equipmentID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in setReturned");
        }
    }

    public static boolean checkForNewYear() {
        String querry = "SELECT COUNT(1) " +
                "FROM Klient " +
                "WHERE nr_umowy_rok = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            //TODO closing statement
            statement = DBConnection.getConnection().prepareStatement(querry);

            statement.setString(1, Year.now().toString());

            resultSet = statement.executeQuery();
            resultSet.next();
            if(resultSet.getInt(1) == 0){
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in checkForNewYear");
        }

        return false;
    }

    public static boolean checkClientStatus(int clientID){
        try{
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            String query = "SELECT id_klient " +
                    "FROM Klient " +
                    "WHERE id_klient NOT IN ( " +
                    "SELECT id_klient FROM Wypozyczenie)";
            statement = DBConnection.getConnection().prepareStatement(query);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getInt(1) == (clientID)){
                    return true;
                }
            }
            return false;
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error querying last client ID");
        }
        return false;
    }

    public static boolean updateDatabaseUser(byte[] login, byte[] password){
        String query = "ALTER USER ?@'%' IDENTIFIED BY ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setString(1, new String(login));
            statement.setString(2, new String(password));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się zaktualizować użytkownika. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean addUser(byte[] userName, byte[] password, String role){
        String query = "CREATE USER ?@'%' IDENTIFIED BY ?";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setString(1, new String(userName));
            statement.setString(2, new String(password));
            statement.executeUpdate();
            if(role.equals("admin")){
                query = "GRANT ALTER, CREATE USER, DELETE, DROP, GRANT OPTION, INSERT, SELECT, UPDATE ON *.* TO ?@'%'";
                try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query)){
                    statement0.setString(1, new String(userName));
                    System.out.println(statement0);
                    statement0.executeUpdate();
                }
            } else{
                query = "GRANT INSERT, SELECT, UPDATE, DELETE ON rent.* TO ?@'%'";
                try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query)){
                    statement0.setString(1, new String(userName));
                    statement0.executeUpdate();
                }
            }
            query = "INSERT INTO Uzytkownik (nazwa) VALUES (?)";
            try (PreparedStatement statement0 = DBConnection.getConnection().prepareStatement(query)){
                statement0.setString(1, new String(userName));
                statement0.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się dodać użytkownika. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static boolean deleteUser(byte[] userName){
        String query = "DROP USER ?@'%'";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            statement.setString(1, new String(userName));
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            SceneCtrl.showMessageWindow("Błąd", "Nie udało się usunąć użytkownika. Błąd połączenia z bazą danych");
        }
        return false;
    }

    public static ObservableList<UserRow> queryUserNames(){
        String query = "SELECT nazwa FROM Uzytkownik";
        try (PreparedStatement statement = DBConnection.getConnection().prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();
            ObservableList<UserRow> userNames = FXCollections.observableArrayList();
            while(resultSet.next()){
                userNames.add(new UserRow(resultSet.getString(1)));
            }
            return userNames;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




}
