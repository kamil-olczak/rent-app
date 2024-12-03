package com.rentapp.gui.window;

import com.rentapp.table.EquipRow;
import com.rentapp.util.DBQuery;
import com.rentapp.gui.scene.SceneCtrl;
import com.rentapp.table.AccessoryRow;
import com.rentapp.table.Table;
import javafx.collections.ObservableList;
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

public class AddAccessoryCtrl implements Initializable {
    @FXML
    private VBox rootVBox;
    @FXML
    private Button addButton;
    @FXML
    private Button cancel;
    @FXML
    private TableView<AccessoryRow> accessoryTable;
    @FXML
    private TableColumn<AccessoryRow, String> name;
    @FXML
    private TableColumn<AccessoryRow, String> priceNett;
    @FXML
    private TableColumn<AccessoryRow, String> usable;
    @FXML
    private TextField search;
    @FXML
    private ChoiceBox<String> searchColumnChose;

    private Map<String, String> columnsNames = new HashMap<>();

    private final TableView<AccessoryRow> intitializedAccTable = new TableView<>();
    private Function<List<AccessoryRow>, Boolean> setSelectedAccesssory;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootVBox.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                SceneCtrl.closeWindow(keyEvent);
            }
            if(keyEvent.getCode() == KeyCode.ENTER){
                addActionKey(keyEvent);
            }
        });
        accessoryTable.setRowFactory(tv -> {
            TableRow<AccessoryRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    addButton.fire();
                }
            });
            return row;
        });


        try {
            initializeAccTable();
            accessoryTable.getItems().addAll(Table.updateAccessoryTable(DBQuery.queryAccessoryTable()));
            intitializedAccTable.getItems().addAll(accessoryTable.getItems());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchColumnChose.getSelectionModel().select(0);
    }

    private void initializeAccTable() {
        accessoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        priceNett.setCellValueFactory(new PropertyValueFactory<>("priceNett"));

        usable.setCellValueFactory(new PropertyValueFactory<>("usable"));

        columnsNames.put("Nazwa", "name");
        searchColumnChose.getItems().add("Nazwa");

        columnsNames.put("Za dobę netto", "priceNett");
        searchColumnChose.getItems().add("Za dobę netto");

        columnsNames.put("Zużywalne", "usable");
        searchColumnChose.getItems().add("Zużywalne");
    }

    @FXML
    void addAction(ActionEvent event) {
        if(accessoryTable.getSelectionModel().getSelectedItem() == null){
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano akcesorium.");
        } else {
            setSelectedAccesssory.apply(accessoryTable.getSelectionModel().getSelectedItems());
            SceneCtrl.closeWindow(event);
        }
    }

    void addActionKey(KeyEvent event) {
        if(accessoryTable.getSelectionModel().getSelectedItem() == null){
            return;
        }
        setSelectedAccesssory.apply(accessoryTable.getSelectionModel().getSelectedItems());
        SceneCtrl.closeWindow(event);
    }

    @FXML
    public void addAccBase(ActionEvent event) throws SQLException, IOException {
        SceneCtrl.showAddAccBaseWindow((accessory) -> {
            try {
                ObservableList<AccessoryRow> rows = Table.updateAccessoryTable(DBQuery.queryAccessoryTable());
                accessoryTable.getItems().clear();
                accessoryTable.getItems().addAll(rows);
                accessoryTable.getSelectionModel().select(rows.size()-1);
                return 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void editAcc(ActionEvent event) throws SQLException, IOException {
        if(accessoryTable.getSelectionModel().getSelectedItem() == null){
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano akcesorium.");
        } else {
            SceneCtrl.showEditAccBaseWindow(accessoryTable.getSelectionModel().getSelectedItem(), (accessory) -> {
                try {
                    ObservableList<AccessoryRow> rows = Table.updateAccessoryTable(DBQuery.queryAccessoryTable());
                    if(rows != null) {
                        int index = findIndex(rows, accessory.getAccessoryID() + "");
                        accessoryTable.getItems().clear();
                        accessoryTable.getItems().addAll(rows);
                        accessoryTable.getSelectionModel().select(index);
                    }
                    return 0;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @FXML
    public void deleteAccBase(ActionEvent event) throws SQLException {
        if(accessoryTable.getSelectionModel().getSelectedItem() == null){
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano akcesorium.");
            return;
        }
        if(accessoryTable.getSelectionModel().getSelectedItems().size() > 1){
            SceneCtrl.showMessageWindow("Błąd", "Wybierz tylko jedno akcesorium do usunięcia.");
            return;
        }
        Optional<ButtonType> res = SceneCtrl.showMessageWindow("Potwierdzenie", "Czy na pewno chcesz usunąć akcesorium? \nSpowoduje to znikniecie informacji o wypożyczeniu tego akcesorium w historii", true);
        if(res.isPresent() && res.get().getButtonData() == ButtonBar.ButtonData.YES){
            if(DBQuery.deleteAccessory(accessoryTable.getSelectionModel().getSelectedItem().getAccessoryID())){
                ObservableList<AccessoryRow> rows = Table.updateAccessoryTable(DBQuery.queryAccessoryTable());
                accessoryTable.getItems().clear();
                accessoryTable.getItems().addAll(rows);
            }
        }
    }

    public int findIndex(ObservableList<AccessoryRow> rows, String accID){
        for (int i = 0; i < rows.size(); i++) {
            if(rows.get(i).get("accessoryID").equals(accID)){
                return i;
            }
        }
        return -1;
    }

    @FXML
    void search(KeyEvent event) {
        if(search.getText().isEmpty()){
           accessoryTable.getItems().clear();
           accessoryTable.getItems().addAll(intitializedAccTable.getItems());
        } else {
            accessoryTable.getItems().clear();
            accessoryTable.getItems().addAll(intitializedAccTable.getItems());
            accessoryTable = Table.searchAccessoryTable(accessoryTable, search.getText(),columnsNames.get(searchColumnChose.getValue()));
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        SceneCtrl.closeWindow(event);
    }

    public void setSetSelectedAccesssory(Function<List<AccessoryRow>, Boolean> function){
        setSelectedAccesssory = function;

    }

    public void setAddButtonLabel(String label){
        addButton.setText(label);
    }
}




