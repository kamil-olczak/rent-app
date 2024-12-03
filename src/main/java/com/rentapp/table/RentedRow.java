package com.rentapp.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class RentedRow {
    private String client;
    private String company;
    private String peselNip;
    private String equipment;
    private String equipmentID;
    private String accessories;
    private String notes;
    private String priceGross;
    private String depositGross;
    private String depositPayment;
    private String fromWhen;
    private String probableToWhen;
    private String byWhoColumn;
    private String clientID;
    private String longTerm;
    private String toWhen;
    private String rentalID;
    private int clientRentalID;
    private int clientRentalIDYear;


    public RentedRow() {
        this.client = "";
        this.company = "";
        this.equipment = "";
        this.equipmentID = "";
        this.accessories = "";
        this.fromWhen = "";
        this.probableToWhen = "";
        this.byWhoColumn = "";
    }

    public String get(String columnName) {
        return switch (columnName) {
            case "client" -> getClient();
            case "company" -> getCompany();
            case "peselNip" -> getPeselNip();
            case "equipment" -> getEquipment();
            case "equipmentID" -> getEquipmentID();
            case "accessories" -> getAccessories();
            case "notes" -> getNotes();
            case "priceGross" -> getPriceGross();
            case "depositGross" -> getDepositGross();
            case "fromWhen" -> getFromWhen();
            case "toWhen" -> getToWhen();
            case "byWhoColumn" -> getByWhoColumn();
            case "clientID" -> getClientID();
            case "rentalID" -> getRentalID();
            case "longTerm" -> getLongTerm();
            default -> "";
        };
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(HashMap<String, String> accessoriesNamesAndUsage) throws SQLException {

        accessoriesNamesAndUsage.forEach((accessory, usage) ->{
            this.accessories += accessory + ", ";
            if (!usage.isEmpty()) {
                this.accessories += usage + ", ";
            }
        });
    }

    public String getFromWhen() {
        return fromWhen;
    }

    public void setFromWhen(String fromWhen) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(fromWhen, formatter);
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.fromWhen = dateTime.format(formatter);
    }

    public String getProbableToWhen() {
        return probableToWhen;
    }

    public void setProbableToWhen(String probableToWhen) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(probableToWhen, formatter);
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.probableToWhen = date.format(formatter);

    }

    public String getByWhoColumn() {
        return byWhoColumn;
    }

    public void setByWhoColumn(String byWhoColumn) {
        this.byWhoColumn = byWhoColumn;
    }

    public String getClientID() {
        return clientID;
    }

    public String getRentalID() {
        return rentalID;
    }

    public void setRentalID(String rentalID) {
        this.rentalID = rentalID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPriceGross() {
        return priceGross;
    }

    public void setPriceGross(String priceGross) {
        this.priceGross = priceGross;
    }

    public String getDepositGross() {
        return depositGross;
    }

    public void setDepositGross(String depositGross) {
        this.depositGross = depositGross;
    }

    public String getLongTerm() {
        return longTerm;
    }
    public boolean isLongTerm() {
        return longTerm.equals("Tak");
    }

    public void setLongTerm(String longTerm) {
        this.longTerm = longTerm;
    }

    public String getPeselNip() {
        return peselNip;
    }

    public void setPeselNip(String peselNip) {
        this.peselNip = peselNip;
    }

    public String getToWhen() {
        return toWhen;
    }

    public void setToWhen(String toWhen) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(toWhen, formatter);
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.toWhen= dateTime.format(formatter);
    }

    public String getDepositPayment() {
        return depositPayment;
    }

    public void setDepositPayment(String depositPayment) {
        this.depositPayment = depositPayment;
    }

    public int getClientRentalID() {
        return clientRentalID;
    }

    public void setClientRentalID(int clientRentalID) {
        this.clientRentalID = clientRentalID;
    }

    public int getClientRentalIDYear() {
        return clientRentalIDYear;
    }

    public void setClientRentalIDYear(int rentalIDYear) {
        this.clientRentalIDYear = rentalIDYear;
    }
}



