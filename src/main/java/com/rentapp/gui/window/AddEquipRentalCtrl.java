package com.rentapp.gui.window;

import com.rentapp.util.DBQuery;
import com.rentapp.gui.scene.SceneCtrl;
import com.rentapp.table.EquipRow;
import com.rentapp.table.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddEquipRentalCtrl implements Initializable {
    @FXML
    private VBox rootVBox;
    @FXML
    private Button addToRental;
    @FXML
    private Button cancel;
    @FXML
    private TableView<EquipRow> equipTable;
    @FXML
    private TableColumn<EquipRow, String> model;
    @FXML
    private TableColumn<EquipRow, String> equipmentID;
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
    private TextField search;
    @FXML
    private ChoiceBox<String> searchColumnChose;

    private Map<String, String> columnsNames = new HashMap<>();

    private final TableView<EquipRow> intitializedEquipTable = new TableView<>();
    private Function<List<EquipRow>, Boolean> setSelectedEquip;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootVBox.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE){
                SceneCtrl.closeWindow(keyEvent);
            }
            if(keyEvent.getCode() == KeyCode.ENTER){
                addToRentalKey(keyEvent);
            }
        });
        equipTable.setRowFactory(tv -> {
            TableRow<EquipRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    addToRental.fire();
                }
            });
            return row;
        });


        try {
            initializeEquipTable();
            equipTable.getItems().addAll(Table.updateEquipTable(DBQuery.queryAvailableEquipTable()));
            intitializedEquipTable.getItems().addAll(equipTable.getItems());
            TableColumn<EquipRow, String> defaultSortColumn = model;
            defaultSortColumn.setSortType(TableColumn.SortType.ASCENDING);
            equipTable.getSortOrder().add(defaultSortColumn);
            equipTable.sort();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchColumnChose.getSelectionModel().select(0);



    }

    private void initializeEquipTable() {
        equipTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        model.setCellValueFactory(new PropertyValueFactory<>("model"));
        equipmentID.setCellValueFactory(new PropertyValueFactory<>("equipmentID"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        deposit.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        perDayNet.setCellValueFactory(new PropertyValueFactory<>("perDayNet"));
        perDayGross.setCellValueFactory(new PropertyValueFactory<>("perDayGross"));
        manufactureYear.setCellValueFactory(new PropertyValueFactory<>("manufactureYear"));
        sn.setCellValueFactory(new PropertyValueFactory<>("sn"));
        reviewDate.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));
        motohours.setCellValueFactory(new PropertyValueFactory<>("motohours"));

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

        searchColumnChose.getItems().addFirst("Model + Nazwa + Nr. wewnętrzny");
    }

    @FXML
    void addToRental(ActionEvent event) {
        if(equipTable.getSelectionModel().getSelectedItem() == null){
            SceneCtrl.showMessageWindow("Błąd", "Nie wybrano sprzętu");
        } else {
            setSelectedEquip.apply(equipTable.getSelectionModel().getSelectedItems());
            SceneCtrl.closeWindow(event);
        }
    }

    void addToRentalKey(KeyEvent event) {
        if(equipTable.getSelectionModel().getSelectedItem() == null){
            return;
        }
        setSelectedEquip.apply(equipTable.getSelectionModel().getSelectedItems());
        SceneCtrl.closeWindow(event);

    }


    @FXML
    void search(KeyEvent event) {
        if(search.getText().isEmpty()){
            equipTable.getItems().clear();
            equipTable.getItems().addAll(intitializedEquipTable.getItems());
        } else {
            equipTable.getItems().clear();
            equipTable.getItems().addAll(intitializedEquipTable.getItems());
            if(searchColumnChose.getSelectionModel().getSelectedItem().contains("+")){
                List<String> columns = Arrays.stream(searchColumnChose.getSelectionModel().getSelectedItem().split("\\+"))
                        .map(String::trim)
                        .map(columnsNames::get)
                        .collect(Collectors.toList());
                equipTable = Table.searchEquipTable(equipTable, search.getText(), columns);
            } else {
                equipTable = Table.searchEquipTable(equipTable, search.getText(), columnsNames.get(searchColumnChose.getValue()));
            }
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        SceneCtrl.closeWindow(event);
    }

    public void setSetSelectedEquip(Function<List<EquipRow>, Boolean> function){
        setSelectedEquip = function;
        search.requestFocus();
    }
}




