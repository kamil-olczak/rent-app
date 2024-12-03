package com.rentapp.table;

import com.rentapp.util.Calculate;

public class AccessoryRow{


    private String name = "";
    private String priceNett = "";
    private String priceGross = "";
    private String qty = "1";
    private String usable = "";
    private int accessoryID = 0;
    private String info;



    public String get(String column){
        return switch (column) {
            case "name" -> getName();
            case "priceNett" -> getPriceNett();
            case "accessoryID" -> getAccessoryIDString();
            case "usable" -> getUsable();
            default -> null;
        };
    }
    public void setAccessoryID(int accessoryID) {
        this.accessoryID = accessoryID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPriceNett(String priceNett) {
        this.priceNett = priceNett;
        this.priceGross = Calculate.calculateGrossString(Double.parseDouble(priceNett));
    }
    public void setPriceGross(String priceGross) {
        this.priceGross = priceGross;
        this.priceNett = Calculate.calculateNettString(Double.parseDouble(priceGross));
    }
    public void setQty(String qty) {
        this.qty = qty;
    }
    public void setUsable(String usable) {
        this.usable = usable;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getName() {
        return name;
    }
    public String getPriceNett() {
        return priceNett;
    }
    public String getPriceGross() {
        return priceGross;
    }
    public String getUsable() {
        return usable;
    }
    public boolean isUsable() {
        if(usable.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
    public int getAccessoryID() {
        return accessoryID;
    }
    public String getAccessoryIDString() {
        return Integer.toString(accessoryID);
    }
    public String getQty() {
        return qty;
    }
    public String getInfo() {
        return info;
    }

}
