package com.rentapp.gui.window;

import com.rentapp.table.ClientRow;
import com.rentapp.util.DBQuery;
import com.rentapp.table.Table;
import com.rentapp.gui.scene.SceneCtrl;
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
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChangeClientCtrl implements Initializable {
    @FXML
    private VBox rootVBox;
    @FXML
    private TableView<ClientRow> clientTable;
    @FXML
    private TableColumn<ClientRow, String> name;
    @FXML
    private TableColumn<ClientRow, String> adress;
    @FXML
    private TableColumn<ClientRow, String> company;
    @FXML
    private TableColumn<ClientRow, String> contractNumber;
    @FXML
    private TableColumn<ClientRow, String> fromWhen;
    @FXML
    private TableColumn<ClientRow, String> identityCard;
    @FXML
    private TableColumn<ClientRow, String> peselNip;
    @FXML
    private TableColumn<ClientRow, String> phoneNumber;
    @FXML
    private TextField search;
    @FXML
    private ChoiceBox<String> searchColumnChose;
    @FXML
    private Button selectClient;
    @FXML
    private Button addClient;
    @FXML
    private Button cancel;


    private Map<String, String> columnsNames = new HashMap<>();
    private final TableView<ClientRow> intitializedClientTable = new TableView<>();
    private ClientRow lastAddedClient = null;
    private Function<ClientRow, Boolean> setSelectedClientID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootVBox.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                SceneCtrl.closeWindow(keyEvent);
            }
            if (keyEvent.getCode() == KeyCode.ENTER) {
                selectClientKey(keyEvent);
            }
        });

        clientTable.setRowFactory(tv -> {
            TableRow<ClientRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    selectClient.fire();
                }
            });
            return row;
        });

        try {
            initializeClientTable();
            clientTable.getItems().addAll(DBQuery.queryAndMakeClientTable());
            intitializedClientTable.getItems().addAll(clientTable.getItems());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchColumnChose.getSelectionModel().select(0);


    }

    private void initializeClientTable() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        adress.setCellValueFactory(new PropertyValueFactory<>("adress"));
        company.setCellValueFactory(new PropertyValueFactory<>("company"));
        contractNumber.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));
        fromWhen.setCellValueFactory(new PropertyValueFactory<>("fromWhen"));
        identityCard.setCellValueFactory(new PropertyValueFactory<>("identityCard"));
        peselNip.setCellValueFactory(new PropertyValueFactory<>("peselNip"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        columnsNames.put("Imię nazwisko", "name");
        searchColumnChose.getItems().add("Imię nazwisko");

        columnsNames.put("Firma", "company");
        searchColumnChose.getItems().add("Firma");

        columnsNames.put("PESEL/NIP", "peselNip");
        searchColumnChose.getItems().add("PESEL/NIP");

        columnsNames.put("Adres", "adress");
        searchColumnChose.getItems().add("Adres");

        columnsNames.put("Dowód osobisty", "identityCard");
        searchColumnChose.getItems().add("Dowód osobisty");

        columnsNames.put("Telefon", "phoneNumber");
        searchColumnChose.getItems().add("Telefon");

        columnsNames.put("Od kiedy", "fromWhen");
        searchColumnChose.getItems().add("Od kiedy");

        columnsNames.put("Numer umowy", "contractNumber");
        searchColumnChose.getItems().add("Numer umowy");

        searchColumnChose.getItems().addFirst("Imię nazwisko + Firma + PESEL/NIP");
    }

    @FXML
    void search(KeyEvent event) throws SQLException {
        if (search.getText().isEmpty()) {
            clientTable.getItems().clear();
            clientTable.getItems().addAll(intitializedClientTable.getItems());
        } else {
            clientTable.getItems().clear();
            clientTable.getItems().addAll(intitializedClientTable.getItems());
            if(searchColumnChose.getSelectionModel().getSelectedItem().contains("+")){
                List<String> columns = Arrays.stream(searchColumnChose.getSelectionModel().getSelectedItem().split("\\+"))
                        .map(String::trim)
                        .map(columnsNames::get)
                        .collect(Collectors.toList());
                clientTable = Table.searchClientTable(clientTable, search.getText(), columns);
            } else {
                clientTable = Table.searchClientTable(clientTable, search.getText(), columnsNames.get(searchColumnChose.getValue()));
            }
        }
    }

    @FXML
    void selectClient(ActionEvent event) {
        if (clientTable.getSelectionModel().getSelectedItem() == null) {
            SceneCtrl.showMessageWindow("Błąd", "Wybierz klienta");
        } else {
            setSelectedClientID.apply(clientTable.getSelectionModel().getSelectedItem());
            closeWindow(event);
        }
    }


    void selectClientKey(KeyEvent event) {
        if (clientTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        setSelectedClientID.apply(clientTable.getSelectionModel().getSelectedItem());
        SceneCtrl.closeWindow(event);
    }

    @FXML
    void addClient(ActionEvent event) throws IOException, SQLException {
        SceneCtrl.showAddClientWindow(client -> {
            lastAddedClient = client;
            clientTable.getItems().addFirst(lastAddedClient);
            clientTable.getSelectionModel().select(0);
            intitializedClientTable.getItems().addFirst(lastAddedClient);
            return true;
        });

        //        clientTable.getItems().addAll(Table.updateClientTable(DBQuerry.queryClientTable()));
        //        intitializedClientTable.getItems().clear();
        //        intitializedClientTable.getItems().addAll(clientTable.getItems());
        //        clientTable.getItems().add(lastAddedClient);
        //        clientTable.getSelectionModel().select(lastAddedClient);
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        SceneCtrl.closeWindow(event);
    }

    public void setSetSelectedClient(Function<ClientRow, Boolean> setSelectedClientID) {
        this.setSelectedClientID = setSelectedClientID;
        search.requestFocus();
    }

    public ClientRow getLastAddedClient() {
        return lastAddedClient;
    }
}
