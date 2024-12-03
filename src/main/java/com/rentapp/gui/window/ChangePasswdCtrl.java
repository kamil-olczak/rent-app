package com.rentapp.gui.window;

import com.rentapp.gui.scene.LoginCtrl;
import com.rentapp.gui.scene.SceneCtrl;
import com.rentapp.util.Credentials;
import com.rentapp.util.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswdCtrl implements Initializable {
    @FXML
    private PasswordField current;
    @FXML
    private PasswordField newP;
    @FXML
    private PasswordField newP0;
    @FXML
    private VBox rootVBox;
    @FXML
    private Label error;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootVBox.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                SceneCtrl.closeWindow(keyEvent);
            }
        });
    }

    @FXML
    void closeWindow(ActionEvent event) {
        SceneCtrl.closeWindow(event);
    }

    @FXML
    void save(ActionEvent event) {
        if(Credentials.checkCredentials(LoginCtrl.getLoggedUser().getBytes(), current.getText().getBytes())){
            if(newP.getText().length() < 8){
                error.setText("Hasło musi mieć co najmniej 8 znaków");
                return;
            }
            if(newP.getText().equals(newP0.getText())){
                if(Credentials.changePassword(LoginCtrl.getLoggedUser().getBytes(), newP.getText().getBytes())){
                    SceneCtrl.closeWindow(event);
                    SceneCtrl.showMessageWindow("Zmiana hasła", "Hasło zostało zmienione. \nW celu uniknięcia błedów uruchom ponownie aplikację i zaloguj się nowym hasłem.");
                }
            }else {
                error.setText("Hasła nie są takie same");
            }
        } else {
            error.setText("Obecne hasło jest nieprawidłowe");
        }
    }

    public VBox getRootVBox() {
        return rootVBox;
    }


}
