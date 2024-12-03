package com.rentapp.table;

import java.util.Objects;

public class ClientRow {
    private String name;
    private String adress;
    private String company;
    private String fromWhen;
    private String identityCard;
    private String peselNip;
    private String phoneNumber;
    private String contractNumber;
    private String clientID;
    private boolean isDepositFree;


    public ClientRow() {
        this.name = "";
        this.adress = "";
        this.company = "";
        this.contractNumber = "";
        this.fromWhen = "";
        this.identityCard = "";
        this.peselNip = "";
        this.phoneNumber = "";
    }

    public String get(String columnName) {
        return switch (columnName) {
            case "name" -> name;
            case "adress" -> adress;
            case "company" -> company;
            case "contractNumber" -> contractNumber;
            case "fromWhen" -> fromWhen;
            case "identityCard" -> identityCard;
            case "peselNip" -> peselNip;
            case "phoneNumber" -> phoneNumber;
            default -> "";
        };
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setContractNumber(String contractNumber, String contractNumberYear) {
        this.contractNumber = contractNumber + "/" + contractNumberYear.charAt(2) + contractNumberYear.charAt(3);
    }

    public void setFromWhen(String fromWhen) {
        this.fromWhen = fromWhen;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }


    public void setPeselNip(String peselNip) {
        this.peselNip = peselNip;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
    public void setDepositFree(boolean depositFree) {
        isDepositFree = depositFree;
    }


    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public String getFromWhen() {
        return fromWhen;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public String getPeselNip() {
        return peselNip;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public int getContractNumberInt() {
        return Integer.parseInt(contractNumber.split("/")[0]);
    }

    public String getClientID() {
        return clientID;
    }

    public int getClientIDInt() {
        return Integer.parseInt(clientID);
    }

    public boolean isDepositFree() {
        return isDepositFree;
    }

    public String toString(){
//        (imie_nazwisko, pesel_nip, adres, dw_osob, telefon, nazwa_firma, od, nr_umowy_rok, nr_umowy, bez_kaucji) " +
        return name + " " + peselNip  +" " + adress + " " + identityCard + " " + phoneNumber + " " + company + " " + fromWhen + " " + contractNumber + " " + isDepositFree;
    }

    @Override
    public boolean equals(Object object){
        ClientRow other = (ClientRow) object;
        if(this.clientID.equals(other.getClientID())) {
            return true;
        } else {
            return false;
        }
    }


}
