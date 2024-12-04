package com.rentapp.gui.window;

import com.rentapp.util.DBQuery;
import com.rentapp.gui.scene.SceneCtrl;
import com.rentapp.table.ClientRow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AddClientCtrl implements Initializable {
    @FXML
    private VBox rootVBox;
    @FXML
    private Label title;
    @FXML
    private Button endButton;
    @FXML
    private TextArea adress;
    @FXML
    private TextField company;
    @FXML
    private CheckBox depositFree;
    @FXML
    private TextField identityCard;
    @FXML
    private TextField name;
    @FXML
    private TextField peselNip;
    @FXML
    private TextField phoneNumber;



    private Function<ClientRow, Boolean> invokeUpdate;
    private boolean isEdited = false;
    private ClientRow client = new ClientRow();
    private String oldPeselNip;
    private String oldIdentityCard;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootVBox.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                closeWindow(keyEvent);
            }
        });
        adress.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                peselNip.requestFocus();
            }
        });
    }

    @FXML
    public void endAction(ActionEvent event) throws SQLException {
        trimFields();
        if (!gatherData()){
            return;
        }
        if (isEdited) {
                if (DBQuery.editClient(client)) {
                    if (invokeUpdate.apply(client)) {
                        SceneCtrl.closeWindow(event);
                    }
                }
            //else if (oldIdentityCard.equals(client.getIdentityCard())) {
//                if (DBQuery.checkClientPeselNip(client)) {
//                    if (DBQuery.editClient(client)) {
//                        if (invokeUpdate.apply(client)) {
//                            SceneCtrl.closeWindow(event);
//                        }
//                    }
//                } else {
//                    SceneCtrl.showMessageWindow("Błąd, klient isnieje", "Klient o podanym numerze PESEL/NIP lub numerze dowodu tożsamości już istnieje");
//                    return;
//                }
//            } else if (oldPeselNip.equals(client.getPeselNip())) {
//                if (DBQuery.checkClientIdentityCard(client)) {
//                    if (DBQuery.editClient(client)) {
//                        if (invokeUpdate.apply(client)) {
//                            SceneCtrl.closeWindow(event);
//                        }
//                    }
//                } else {
//                    SceneCtrl.showMessageWindow("Błąd, klient isnieje", "Klient o podanym numerze PESEL/NIP lub numerze dowodu tożsamości już istnieje");
//                    return;
//                }
//            } else {
//                if (DBQuery.checkClientConstraints(client)) {
//                    if (DBQuery.editClient(client)) {
//                        if (invokeUpdate.apply(client)) {
//                            SceneCtrl.closeWindow(event);
//                        }
//                    }
//                } else {
//                    SceneCtrl.showMessageWindow("Błąd, klient isnieje", "Klient o podanym numerze PESEL/NIP lub numerze dowodu tożsamości już istnieje");
//                    return;
//                }
//            }
        } else {
//            if (DBQuery.checkClientConstraints(client)) {
                client.setFromWhen(LocalDate.now().toString());
                client.setContractNumber(DBQuery.queryNewContractNumber(), Year.now().toString());
                client.setClientID(DBQuery.addClient(client));
                if (invokeUpdate.apply(client)) {
                    SceneCtrl.closeWindow(event);
                }
//            } else {
//                SceneCtrl.showMessageWindow("Błąd, klient isnieje", "Klient o podanym numerze PESEL/NIP lub numerze dowodu tożsamości już istnieje");
//            }
        }
    }
    public void trimFields(){
        name.setText(name.getText().trim());
        company.setText(company.getText().trim());
        adress.setText(adress.getText().trim());
        peselNip.setText(peselNip.getText().trim());
        identityCard.setText(identityCard.getText().trim());
        phoneNumber.setText(phoneNumber.getText().trim());
    }

    public boolean gatherData(){
        if (checkIfFieldsAreFilled()) {
            client.setName(name.getText().replaceAll("[\\t\\n\\r\\f\\v]", ""));
            client.setCompany(company.getText().replaceAll("[\\t\\n\\r\\f\\v]", ""));
            client.setAdress(adress.getText().replaceAll("\t", "")); //TODO check
            client.setPeselNip(peselNip.getText().replaceAll("[\\t\\n\\r\\f\\v]", ""));
            client.setIdentityCard(identityCard.getText().replaceAll("[\\t\\n\\r\\f\\v]", ""));
            client.setPhoneNumber(phoneNumber.getText().replaceAll("[\\t\\n\\r\\f\\v]", ""));
            client.setDepositFree(depositFree.isSelected());
            return true;
        } else {
            SceneCtrl.showMessageWindow("Błąd", "Wypełnij wymagane pola");
            return false;
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        if (checkIfAnyFieldIsFilled()) {
            Optional<ButtonType> res = SceneCtrl.showMessageWindow("Zamknij okno", "Czy na pewno chcesz zamknąć okno? Wprowadzone dane zostaną utracone", true);
            if(res.isPresent() && res.get().getButtonData() == ButtonBar.ButtonData.YES){
                SceneCtrl.closeWindow(event);
            } else {
                return;
            }
        } else {
            SceneCtrl.closeWindow(event);
        }
    }

    public void closeWindow(KeyEvent event) {
        if (checkIfAnyFieldIsFilled()) {
            Optional<ButtonType> res = SceneCtrl.showMessageWindow("Zamknij okno", "Czy na pewno chcesz zamknąć okno? Wprowadzone dane zostaną utracone", true);
            if(res.isPresent() && res.get().getButtonData() == ButtonBar.ButtonData.YES){
                SceneCtrl.closeWindow(event);
            } else {
                return;
            }
        } else {
            SceneCtrl.closeWindow(event);
        }
    }

    @FXML
    void search(KeyEvent event) {

    }

    public void setInvokeUpdate(Function<ClientRow, Boolean> invokeUpdate) {
        this.invokeUpdate = invokeUpdate;
    }

    public void setEditedClient(ClientRow clientRow){
        client = clientRow;
        title.setText("Edytuj klienta: ");
        isEdited = true;
        oldPeselNip = clientRow.getPeselNip();
        oldIdentityCard = clientRow.getIdentityCard();
        endButton.setText("Zapisz");
        name.setText(clientRow.getName());
        company.setText(clientRow.getCompany());
        adress.setText(clientRow.getAdress());
        peselNip.setText(clientRow.getPeselNip());
        identityCard.setText(clientRow.getIdentityCard());
        phoneNumber.setText(clientRow.getPhoneNumber());
        depositFree.setSelected(clientRow.isDepositFree());
    }

    private boolean checkIfFieldsAreFilled() {
        if (name.getText().isEmpty() || adress.getText().isEmpty() || peselNip.getText().isEmpty() || identityCard.getText().isEmpty() || phoneNumber.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkIfAnyFieldIsFilled() {
        if (name.getText().isEmpty() && adress.getText().isEmpty() && peselNip.getText().isEmpty() && identityCard.getText().isEmpty() && phoneNumber.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}
