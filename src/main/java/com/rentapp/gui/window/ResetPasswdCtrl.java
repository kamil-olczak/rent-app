package com.rentapp.gui.window;

import com.rentapp.gui.scene.LoginCtrl;
import com.rentapp.gui.scene.SceneCtrl;
import com.rentapp.table.UserRow;
import com.rentapp.util.Credentials;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ResetPasswdCtrl implements Initializable {

    @FXML
    private Label error;
    @FXML
    private PasswordField newPasswd;
    @FXML
    private PasswordField newPasswd0;
    @FXML
    private VBox rootVBox;
    @FXML
    private Label title;
    @FXML
    private TextField userName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootVBox.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                SceneCtrl.closeWindow(keyEvent);
            }
        });
    }

    @FXML
    void save(ActionEvent event) {
        if (newPasswd.getText().length() < 8) {
            error.setText("Hasło musi mieć co najmniej 8 znaków");
            return;
        }
        if(newPasswd.getText().equals(newPasswd0.getText())){
            if(Credentials.changePassword(userName.getText().getBytes(), newPasswd.getText().getBytes())){
                SceneCtrl.closeWindow(event);
                SceneCtrl.showMessageWindow("Zmiana hasła", "Hasło zostało zmienione.");
            }
        } else {
            error.setText("Hasła nie są takie same");
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        SceneCtrl.closeWindow(event);
    }

    public void setUser(UserRow user) {
        this.userName.setText(user.getUserName());
        this.userName.setDisable(true);
    }

    public VBox getRootVBox() {
        return rootVBox;
    }




}
