package com.rentapp;

import com.rentapp.gui.scene.LoginCtrl;
import com.rentapp.util.DBConnection;
import com.rentapp.util.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {
    public static void main(String[] args) {
        launch();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
            System.out.println("Closing connection to DB");
                DBConnection.close();
            } catch (SQLException e) {
                e.printStackTrace(); Logger.logExToFile(e);
            }
        }));
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        LoginCtrl ctrl = fxmlLoader.getController();
        Scene scene = new Scene(root, ctrl.getRootVBox().getPrefWidth(), ctrl.getRootVBox().getPrefHeight());
        stage.setResizable(false);
        stage.setTitle("Wypożyczalnia");
        stage.setScene(scene);
        stage.show();
    }


}