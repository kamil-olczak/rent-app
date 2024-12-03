package com.rentapp.gui.window;

import com.rentapp.gui.scene.SceneCtrl;
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

public class AddUserCtrl implements Initializable {

    @FXML
    private Label error;

    @FXML
    private PasswordField password;

    @FXML
    private ChoiceBox<String> roleSelect;

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

        roleSelect.getItems().addAll("user", "admin");
        roleSelect.getSelectionModel().select(0);
    }

    @FXML
    void add(ActionEvent event) {
        if (password.getText().length() < 8) {
            error.setText("Hasło musi mieć co najmniej 8 znaków");
            return;
        }
        if (Credentials.addUser(userName.getText().getBytes(), password.getText().getBytes(), roleSelect.getValue())) {
            SceneCtrl.closeWindow(event);
            SceneCtrl.showMessageWindow("Dodanie użytkownika", "Użytkownik został dodany.");
        } else {
            error.setText("Użytkownik o podanej nazwie już istnieje.");
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        SceneCtrl.closeWindow(event);
    }

    public VBox getRootVBox() {
        return rootVBox;
    }


}
