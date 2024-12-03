package com.rentapp.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private static final String LOG_FILE_PATH = "error_log.log";

    public static void logExceptionToFile(Exception ex) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            ex.printStackTrace(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}