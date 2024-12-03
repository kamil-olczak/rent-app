package com.rentapp.gui.window;

import com.rentapp.gui.scene.LoginCtrl;
import com.rentapp.gui.scene.SceneCtrl;
import com.rentapp.table.Table;
import com.rentapp.table.UserRow;
import com.rentapp.util.Credentials;
import com.rentapp.util.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChoseUserCtrl implements Initializable {
    @FXML
    private VBox rootVBox;
    @FXML
    private Label title;
    @FXML
    private TableView<UserRow> userTable;
    @FXML
    private TableColumn<UserRow, String> userName;
    @FXML
    private TableColumn<UserRow, String> role;
    @FXML
    private Button actionButton;

    private boolean resetPasswd = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        role.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        userTable.getItems().addAll(Table.updateUserTable(DBQuery.queryUserNames()));

        userTable.setRowFactory(tv -> {
            TableRow<UserRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    actionButton.fire();
                }
            });
            return row;
        });

        rootVBox.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                SceneCtrl.closeWindow(keyEvent);
            }
        });

    }

    @FXML
    void action(ActionEvent event) throws IOException {
        if (resetPasswd) {
            if (userTable.getSelectionModel().getSelectedItem() != null) {
                UserRow userRow = userTable.getSelectionModel().getSelectedItem();
                if (userRow.getUserName().equals(LoginCtrl.getLoggedUser())) {
                    SceneCtrl.showMessageWindow("Błąd", "Nie można zmienić hasła aktualnie zalogowanego użytkownika w tym oknie.\nZmień poprzez przycisk 'Zmień hasło' na ekranie głównym");
                    return;
                }
                SceneCtrl.showResetPasswdWindow(userTable.getSelectionModel().getSelectedItem());
            } else {
                SceneCtrl.showMessageWindow("Błąd", "Nie wybrano użytkownika");
            }
        } else {
            if (userTable.getSelectionModel().getSelectedItem() != null) {
                UserRow userRow = userTable.getSelectionModel().getSelectedItem();
                if (userRow.getUserName().equals(LoginCtrl.getLoggedUser())) {
                    SceneCtrl.showMessageWindow("Błąd", "Nie można usunąć aktualnie zalogowanego użytkownika");
                    return;
                }
                Optional<ButtonType> res = SceneCtrl.showMessageWindow("Usuwanie użytkownika", "Czy na pewno chcesz usunąć użytkownika " + userRow.getUserName() + "?", true);
                if (res.isPresent() && res.get() == ButtonType.NO) {
                    return;
                }
                if (DBQuery.deleteUser(userRow.getUserName().getBytes())) {
                    if (Credentials.removeUser(userRow.getUserName().getBytes())) {
                        SceneCtrl.showMessageWindow("", "Usunięto użytkownika");
                        userTable.getItems().remove(userRow);
                    }
                } else {
                    SceneCtrl.showMessageWindow("Błąd", "Nie udało się usunąć użytkownika. Błąd połaczenia z bazą danych");
                }

            } else {
                SceneCtrl.showMessageWindow("Błąd", "Nie wybrano użytkownika do usunięcia");
            }
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        SceneCtrl.closeWindow(event);
    }

    public VBox getRootVBox() {
        return rootVBox;
    }

    public void setResetPasswd() {
        title.setText("Wybierz użytkownika:");
        actionButton.setText("Ustaw nowe hasło");
        resetPasswd = true;
    }
}
