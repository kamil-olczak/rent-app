package com.rentapp.util;

import com.rentapp.gui.scene.LoginCtrl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private static final String LOG_FILE_PATH = "error_log.log";

    public static void logExToFile(Exception ex) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.println("At: " + java.time.LocalDateTime.now()+ "Logged user: " + LoginCtrl.getLoggedUser());
            ex.printStackTrace(writer);
        } catch (IOException e) {
            e.printStackTrace(); Logger.logExToFile(e);
        }
    }
}