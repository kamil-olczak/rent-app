package com.rentapp.util;

import com.rentapp.gui.scene.SceneCtrl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static boolean connect(byte [] userName, byte [] password) throws ClassNotFoundException {
        String dbDriver = "com.mysql.cj.jdbc.Driver";
        String dbURL = readConnectionString();
        if(dbURL == null) return false;
        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbURL, new String(userName), new String(password));
            connection.setAutoCommit(false);
            System.out.println("Connected to database");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); Logger.logExToFile(e);
            return false;
        }
    }
    public static void close() throws SQLException {
        if(connection != null && !connection.isClosed()) {connection.close();}
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String readConnectionString(){
        try (BufferedReader bReader = new BufferedReader(new FileReader("config.cfg"))) {
            return new String(bReader.readLine());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(); Logger.logExToFile(ex);
            SceneCtrl.showMessageWindow("Błąd", "Brak pliku konfiguracyjnego");
        } catch (IOException e) {
            SceneCtrl.showMessageWindow("Błąd", "Błąd odczytu pliku konfiguracyjnego");
            throw new RuntimeException(e);
        }
        return null;
    }

}
