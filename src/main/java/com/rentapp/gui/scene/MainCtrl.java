package com.rentapp.gui.scene;

import com.rentapp.table.ClientRow;
import com.rentapp.table.EquipRow;
import com.rentapp.util.DBQuery;
import com.rentapp.table.RentedRow;
import com.rentapp.table.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MainCtrl implements Initializable{
    @FXML
    private VBox rootVBox;
    @FXML
    private TableView<RentedRow> rentalTable;
    @FXML
    private TableColumn<RentedRow, String> client;
    @FXML
    private TableColumn<RentedRow, String> company;
    @FXML
    private TableColumn<RentedRow, String> peselNip;
    @FXML
    private TableColumn<RentedRow, String> equipment;
    @FXML
    private TableColumn<RentedRow, String> equipmentID;
    @FXML
    private TableColumn<RentedRow, String> accessories;
    @FXML
    private TableColumn<RentedRow, String> notes;
    @FXML
    private TableColumn<RentedRow, String> priceGross;
    @FXML
    private TableColumn<RentedRow, String> depositGross;
    @FXML
    private TableColumn<RentedRow, String> depositPayment;
    @FXML
    private TableColumn<RentedRow, String> fromWhen;
    @FXML
    private TableColumn<RentedRow, String> probableToWhen;
    @FXML
    private TableColumn<RentedRow, String> byWho;
    @FXML
    private TableColumn<RentedRow, String> longTerm;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane scrollPane;

    private boolean rentalTableInitialized = false;


    //Equip Tab
    @FXML
    private TableView<EquipRow> equipTable;
    @FXML
    private TableColumn<EquipRow, String> model;
    @FXML
    private TableColumn<EquipRow, String> equipmentIDTab;
    @FXML
    private TableColumn<EquipRow, String> name;
    @FXML
    private TableColumn<EquipRow, String> deposit;
    @FXML
    private TableColumn<EquipRow, String> perDayNet;
    @FXML
    private TableColumn<EquipRow, String> perDayGross;
    @FXML
    private TableColumn<EquipRow, String> manufactureYear;
    @FXML
    private TableColumn<EquipRow, String> sn;
    @FXML
    private TableColumn<EquipRow, String> reviewDate;
    @FXML
    private TableColumn<EquipRow, String> motohours;
    @FXML
    private TableColumn<EquipRow, String> sold;
    @FXML
    private TextField searchEquip;
    @FXML
    private ChoiceBox<String> searchColumnChose;
    @FXML
    private CheckBox showAvailable;
    @FXML
    private Button markSold;
    @FXML
    private Button deleteEquip;

    private Map<String, String> columnsNames = new HashMap<>();
    private TableView<EquipRow> equipTableCopy = new TableView<>();
    private boolean equipTableInitialized = false;

    //Client Tab
    @FXML
    private TableView<ClientRow> clientTable;
    @FXML
    private TableColumn<ClientRow, String> nameClient;
    @FXML
    private TableColumn<ClientRow, String> adressClient;
    @FXML
    private TableColumn<ClientRow, String> companyClient;
    @FXML
    private TableColumn<ClientRow, String> contractNumberClient;
    @FXML
    private TableColumn<ClientRow, String> fromWhenClient;
    @FXML
    private TableColumn<ClientRow, String> identityCardClient;
    @FXML
    private TableColumn<ClientRow, String> peselNipClient;
    @FXML
    private TableColumn<ClientRow, String> phoneNumberClient;
    @FXML
    private TextField searchClient;
    @FXML
    private ChoiceBox<String> searchColumnChoseClient;
    @FXML
    private Button deleteClient;

    private TableView<ClientRow> clientTableCopy = new TableView<>();
    private boolean clientTableInitialized = false;


    //Users Tab
    @FXML
    private Label loggedUser;
    @FXML
    private Button addUser;
    @FXML
    private Button deleteUser;
    @FXML
    private Button setPasswd;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Rental Tab
        initializeRentalTable();
        try {
            rentalTable.setItems(DBQuery.queryAndMakeRentalTable());
            rentalTable.setPlaceholder(new Label("Brak wypożyczeń"));
            rentalTableInitialized = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Equip Tab
        try {
            initializeEquipTable();
            equipTable.getItems().addAll(DBQuery.queryAndMakeEquipTable());
            equipTableCopy.getItems().addAll(equipTable.getItems());
            equipTableInitialized = true;
            TableColumn<EquipRow, String> defaultSortColumn = model;
            defaultSortColumn.setSortType(TableColumn.SortType.ASCENDING);
            equipTable.getSortOrder().add(defaultSortColumn);
            equipTable.sort();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchColumnChose.getSelectionModel().select(0);

        //Client Tab
        try {
            initializeClientTable();
            clientTable.getItems().addAll(DBQuery.queryAndMakeClientTable());
            clientTableCopy.getItems().addAll(clientTable.getItems());
            clientTableInitialized = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchColumnChoseClient.getSelectionModel().select(0);


        loggedUser.setText(LoginCtrl.getLoggedUser());
        if(LoginCtrl.getUserRole().equals("user")){
            addUser.setDisable(true);
            addUser.setVisible(false);
            deleteUser.setDisable(true);
            deleteUser.setVisible(false);
            setPasswd.setDisable(true);
            setPasswd.setVisible(false);
            markSold.setDisable(true);
            markSold.setVisible(false);
            deleteEquip.setDisable(true);
            deleteEquip.setVisible(false);
            deleteClient.setDisable(true);
            deleteClient.setVisible(false);
        }
    }

    private void initializeRentalTable(){
        rentalTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        client.setCellValueFactory(new PropertyValueFactory<>("client"));
        company.setCellValueFactory(new PropertyValueFactory<>("company"));
        peselNip.setCellValueFactory(new PropertyValueFactory<>("peselNip"));
        equipment.setCellValueFactory(new PropertyValueFactory<>("equipment"));
        equipmentID.setCellValueFactory(new PropertyValueFactory<>("equipmentID"));
        accessories.setCellValueFactory(new PropertyValueFactory<>("accessories"));
        notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        priceGross.setCellValueFactory(new PropertyValueFactory<>("priceGross"));
        depositGross.setCellValueFactory(new PropertyValueFactory<>("depositGross"));
        depositPayment.setCellValueFactory(new PropertyValueFactory<>("depositPayment"));
        fromWhen.setCellValueFactory(new PropertyValueFactory<>("fromWhen"));
        probableToWhen.setCellValueFactory(new PropertyValueFactory<>("probableToWhen"));
        byWho.setCellValueFactory(new PropertyValueFactory<>("byWhoColumn"));
        longTerm.setCellValueFactory(new PropertyValueFactory<>("longTerm"));


    }

    @FXML
    protected void addRental(ActionEvent event) throws IOException {
        SceneCtrl.showAddRentalWindow((x)->{
            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return 0;
        });
    }

    @FXML
    protected void setReturned(ActionEvent event) throws SQLException {
        List<RentedRow> selectedRows = rentalTable.getSelectionModel().getSelectedItems();
        if (!selectedRows.isEmpty()) {
            Optional<ButtonType> result = SceneCtrl.showMessageWindow("Potwierdzenie zwrotu", "Czy na pewno zwrot został dokonany?", true);
            if (result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                for (RentedRow rentRow : selectedRows) {
                        DBQuery.setReturned(rentRow.getEquipmentID());
                }
                rentalTable.setItems(DBQuery.queryAndMakeRentalTable());
            }
        } else {
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano żadnego wypożyczenia.");
        }
    }

    @FXML
    protected void refresh() throws SQLException {
        if(rentalTableInitialized){
            rentalTable.getItems().clear();
            rentalTable.getItems().addAll(DBQuery.queryAndMakeRentalTable());
        }
        if (equipTableInitialized) {
            equipTable.getItems().clear();
            if(showAvailable.isSelected()){
                equipTable.getItems().addAll(DBQuery.queryAndMakeAvailableEquipTable());
                equipTableCopy.getItems().clear();
                equipTableCopy.getItems().addAll(equipTable.getItems());
            } else {
                equipTable.getItems().addAll(DBQuery.queryAndMakeEquipTable());
                equipTableCopy.getItems().clear();
                equipTableCopy.getItems().addAll(equipTable.getItems());
            }
            equipTable.sort();
        }
        if (clientTableInitialized) {
            clientTable.getItems().clear();
            clientTable.getItems().addAll(DBQuery.queryAndMakeClientTable());
            clientTableCopy.getItems().clear();
            clientTableCopy.getItems().addAll(clientTable.getItems());
        }
    }

    @FXML
    public void showRentHistory(ActionEvent event) throws IOException {
        SceneCtrl.showRentHistoryWindow();
    }

    @FXML
    public void printDocuments(ActionEvent event) throws IOException {
        if(rentalTable.getSelectionModel().getSelectedItem() == null){
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano wypożyczenia.");
            return;
        }
        System.out.println( "ClientID: " + rentalTable.getSelectionModel().getSelectedItem().getClientID() +
                "\nClientRentID: "+ rentalTable.getSelectionModel().getSelectedItem().getClientRentalID()+
                "\nClientRentIDYear: " + rentalTable.getSelectionModel().getSelectedItem().getClientRentalIDYear() +
                "\nClientName: " + rentalTable.getSelectionModel().getSelectedItem().getClient());
        SceneCtrl.showPrintWindow(false, true, rentalTable.getSelectionModel().getSelectedItem());

    }






    //Equip Tab
    private void initializeEquipTable() {
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        equipmentIDTab.setCellValueFactory(new PropertyValueFactory<>("equipmentID"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        deposit.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        perDayNet.setCellValueFactory(new PropertyValueFactory<>("perDayNet"));
        perDayGross.setCellValueFactory(new PropertyValueFactory<>("perDayGross"));
        manufactureYear.setCellValueFactory(new PropertyValueFactory<>("manufactureYear"));
        sn.setCellValueFactory(new PropertyValueFactory<>("sn"));
        reviewDate.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));
        motohours.setCellValueFactory(new PropertyValueFactory<>("motohours"));
        sold.setCellValueFactory(new PropertyValueFactory<>("sold"));

        columnsNames.put("Model", "model");
        searchColumnChose.getItems().add("Model");

        columnsNames.put("Nr. wewnętrzny", "equipmentID");
        searchColumnChose.getItems().add("Nr. wewnętrzny");

        columnsNames.put("Nazwa", "name");
        searchColumnChose.getItems().add("Nazwa");

        columnsNames.put("Kaucja", "deposit");
        searchColumnChose.getItems().add("Kaucja");

        columnsNames.put("Za dobę netto", "perDayNet");
        searchColumnChose.getItems().add("Za dobę netto");

        columnsNames.put("Rok produkcji", "manufactureYear");
        searchColumnChose.getItems().add("Rok produkcji");

        columnsNames.put("Nr. seryjny", "sn");
        searchColumnChose.getItems().add("Nr. seryjny");

        columnsNames.put("Data przeglądu", "reviewDate");
        searchColumnChose.getItems().add("Data przeglądu");

        columnsNames.put("Ilość motogodzin", "motohours");
        searchColumnChose.getItems().add("Ilość motogodzin");

        columnsNames.put("Sprzedany", "sold");
        searchColumnChose.getItems().add("Sprzedany");

        searchColumnChose.getItems().addFirst("Model + Nazwa + Nr. wewnętrzny");
    }
    @FXML
    void search(KeyEvent event) {
        if(searchEquip.getText().isEmpty()){
            equipTable.getItems().clear();
            equipTable.getItems().addAll(equipTableCopy.getItems());
            equipTable.sort();
        } else {
            equipTable.getItems().clear();
            equipTable.getItems().addAll(equipTableCopy.getItems());
            if(searchColumnChose.getSelectionModel().getSelectedItem().contains("+")){
                List<String> columns = Arrays.stream(searchColumnChose.getSelectionModel().getSelectedItem().split("\\+"))
                        .map(String::trim)
                        .map(columnsNames::get)
                        .collect(Collectors.toList());
                equipTable = Table.searchEquipTable(equipTable, searchEquip.getText(), columns);
            } else {
                equipTable = Table.searchEquipTable(equipTable, searchEquip.getText(), columnsNames.get(searchColumnChose.getValue()));
            }
        }
    }

    void search(ActionEvent event) {
        if(searchEquip.getText().isEmpty()){
            equipTable.getItems().clear();
            equipTable.getItems().addAll(equipTableCopy.getItems());
            equipTable.sort();
        } else {
            equipTable.getItems().clear();
            equipTable.getItems().addAll(equipTableCopy.getItems());
            equipTable = Table.searchEquipTable(equipTable, searchEquip.getText(),columnsNames.get(searchColumnChose.getValue()));
        }
    }

    @FXML
    public void addEquip(ActionEvent event) throws IOException {
        SceneCtrl.showAddEquipWindow((x)->{
            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return 0;
        });
    }

    @FXML
    public void addEquipBasedOn(ActionEvent event) throws IOException {
        if(equipTable.getSelectionModel().getSelectedItem() == null){
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano sprzętu");
        } else {
            SceneCtrl.showAddEquipWindow(equipTable.getSelectionModel().getSelectedItem(), (x) -> {
                try {
                    refresh();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return 0;
            });
        }
    }

    @FXML
    public void editEquip(ActionEvent event) throws IOException{
        if(equipTable.getSelectionModel().getSelectedItem() == null){
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano sprzętu");
        } else {
            SceneCtrl.showEditEquipWindow(equipTable.getSelectionModel().getSelectedItem(), (x) -> {
                try {
                    refresh();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return 0;
            });
        }
    }

    @FXML
    public void deleteEquip(ActionEvent event){
        EquipRow selectedRow = equipTable.getSelectionModel().getSelectedItem();
        if(selectedRow != null){
            Optional<ButtonType> result = SceneCtrl.showMessageWindow("Potwierdzenie usunięcia", "Czy na pewno chcesz usunąć sprzęt? \nUWAGA. Wszystkie wypożyczenia powiązane ze sprzętem zostaną usunięte z historii.\nTej czynności nie można cofnąć.", true);
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                try {
                    DBQuery.deleteEquip(selectedRow);
                    refresh();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano żadnego sprzętu.");
        }
    }

    @FXML
    public void markSold(ActionEvent event){
        EquipRow selectedRow = equipTable.getSelectionModel().getSelectedItem();
        if(selectedRow != null){
            Optional<ButtonType> result = SceneCtrl.showMessageWindow("Potwierdzenie sprzedaży", "Czy na pewno sprzęt został sprzedany? UWAGA. Tej czynności nie można cofnąć.", true);
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                try {
                    DBQuery.markSold(selectedRow);
                    refresh();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano żadnego sprzętu.");
        }
    }
    @FXML
    private void showAvailableEquip(ActionEvent event) throws SQLException {
        refresh();
        if(!searchEquip.getText().isEmpty()){
            search(event);
        }
    }




    //Client Tab
    private void initializeClientTable(){
        nameClient.setCellValueFactory(new PropertyValueFactory<>("name"));
        adressClient.setCellValueFactory(new PropertyValueFactory<>("adress"));
        companyClient.setCellValueFactory(new PropertyValueFactory<>("company"));
        contractNumberClient.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));
        fromWhenClient.setCellValueFactory(new PropertyValueFactory<>("fromWhen"));
        identityCardClient.setCellValueFactory(new PropertyValueFactory<>("identityCard"));
        peselNipClient.setCellValueFactory(new PropertyValueFactory<>("peselNip"));
        phoneNumberClient.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        columnsNames.put("Imię nazwisko", "name");
        searchColumnChoseClient.getItems().add("Imię nazwisko");

        columnsNames.put("Firma", "company");
        searchColumnChoseClient.getItems().add("Firma");

        columnsNames.put("PESEL/NIP", "peselNip");
        searchColumnChoseClient.getItems().add("PESEL/NIP");

        columnsNames.put("Adres", "adress");
        searchColumnChoseClient.getItems().add("Adres");

        columnsNames.put("Dowód osobisty", "identityCard");
        searchColumnChoseClient.getItems().add("Dowód osobisty");

        columnsNames.put("Telefon", "phoneNumber");
        searchColumnChoseClient.getItems().add("Telefon");

        columnsNames.put("Od kiedy", "fromWhen");
        searchColumnChoseClient.getItems().add("Od kiedy");

        columnsNames.put("Numer umowy", "contractNumber");
        searchColumnChoseClient.getItems().add("Numer umowy");

        searchColumnChoseClient.getItems().addFirst("Imię nazwisko + Firma + PESEL/NIP");
    }

    @FXML
    void searchClient(KeyEvent event) {
        if(searchClient.getText().isEmpty()){
            clientTable.getItems().clear();
            clientTable.getItems().addAll(clientTableCopy.getItems());
        } else {
            clientTable.getItems().clear();
            clientTable.getItems().addAll(clientTableCopy.getItems());
            if(searchColumnChoseClient.getSelectionModel().getSelectedItem().contains("+")){
                List<String> columns = Arrays.stream(searchColumnChoseClient.getSelectionModel().getSelectedItem().split("\\+"))
                        .map(String::trim)
                        .map(columnsNames::get)
                        .collect(Collectors.toList());
                clientTable = Table.searchClientTable(clientTable, searchClient.getText(), columns);
            } else {
                clientTable = Table.searchClientTable(clientTable, searchClient.getText(), columnsNames.get(searchColumnChoseClient.getValue()));
            }
        }
    }

    @FXML
    void addClient(ActionEvent event) throws IOException, SQLException {
        SceneCtrl.showAddClientWindow(client -> {
            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        });
    }

    @FXML
    void editClient(ActionEvent event) throws IOException {
        if (clientTable.getSelectionModel().getSelectedItem() != null) {
            SceneCtrl.showEditClientWindow(clientTable.getSelectionModel().getSelectedItem(), (x) -> {
                try {
                    refresh();
                    return true;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano żadnego klienta.");
        }
    }

    @FXML
    void deleteClient(ActionEvent event){
        ClientRow selectedRow = clientTable.getSelectionModel().getSelectedItem();
        if(selectedRow != null){
            Optional<ButtonType> result = SceneCtrl.showMessageWindow("Potwierdzenie usunięcia", "Czy na pewno chcesz usunąć klienta? \nUWAGA. Wszystkie wypożyczenia powiązane z klientem zostaną usunięte z historii.\nTej czynności nie można cofnąć.", true);
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                try {
                    DBQuery.deleteClient(selectedRow);
                    refresh();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano żadnego klienta.");
        }
    }

    public VBox getRootVBox(){
        return rootVBox;
    }


    //User Tab
    @FXML
    void changePasswd(ActionEvent event) throws IOException {
        SceneCtrl.showChangePasswdWindow();
    }

    @FXML
    void addUser(ActionEvent event) throws IOException {
        SceneCtrl.showAddUserWindow();
    }

    @FXML
    void deleteUser(ActionEvent event) throws IOException {
        SceneCtrl.selectUserToDeleteWindow();
    }

    @FXML
    void setNewPasswd(ActionEvent event) throws IOException {
        SceneCtrl.selectUserToResetWindow();
    }

}
