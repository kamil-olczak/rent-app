package com.rentapp.gui.scene;

import com.rentapp.util.Credentials;
import com.rentapp.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private Label Error;
    @FXML
    private VBox rootVBox;


    private static String loggedUser;
    private static String userRole;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootVBox.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == javafx.scene.input.KeyCode.ENTER){
                loginButton.fire();
            }
        });
    }

    @FXML
    protected void login(ActionEvent event) throws IOException, ClassNotFoundException,  InterruptedException {
        if (userName.getText().isEmpty() || password.getText().isEmpty()) {
            Error.setText("Wypełnij wszystkie pola.");
            return;
        }
        if (Credentials.checkCredentials(userName.getText().getBytes(), password.getText().getBytes(), role -> {setUserRole(role); return 0;})) {
            loggedUser = userName.getText();
            if(DBConnection.connect(userName.getText().getBytes(), password.getText().getBytes())){
                SceneCtrl.switchToMainScreen(event);
            } else {
                SceneCtrl.showMessageWindow("Błąd", "Nie udało się połączyć z bazą danych.");
            }
        } else {
            Error.setText("Nieprawidłowa nazwa użytkownika lub hasło.");
        }
    }

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static String getUserRole() {
        return userRole;
    }

    private static void setUserRole(String role){
        userRole = role;
    }

    public VBox getRootVBox() {
        return rootVBox;
    }


}