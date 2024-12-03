package com.rentapp.table;

public class ToRentRow {
    private String equipID;
    private String model;
    private String name;
    private String perDayNett;
    private String perDayGross;
    private String rentalID;
    private String quantity;
    private boolean usable;
    private String type;
    private String accessoryID;


    public ToRentRow() {
        this.equipID = "";
        this.model = "";
        this.name = "";
        this.perDayNett = "";
        this.perDayGross = "";
        this.rentalID = "";
        this.quantity = "";
        this.usable = false;
        this.type = "";
        this.accessoryID = "";
    }

    public void setEquipID(String equipID) {
        this.equipID = equipID;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerDayNett(String perDayNett) {
        this.perDayNett = perDayNett;
    }

    public void setPerDayGross(String perDayGross) {
        this.perDayGross = perDayGross;
    }

    public void setRentalID(String rentalID) {
        this.rentalID = rentalID;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = String.valueOf(quantity);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAccessoryID(String accessoryID) {
        this.accessoryID = accessoryID;
    }

    public String getEquipID() {
        return equipID;
    }

    public int getEquipIDInt() {
        return Integer.parseInt(equipID);
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getPerDayNett() {
        return perDayNett;
    }

    public String getPerDayGross() {
        return perDayGross;
    }

    public String getRentalID() {
        return rentalID;
    }

    public String getQuantity() {
        return quantity;
    }

    public boolean isUsable() {
        return usable;
    }

    public String getType() {
        return type;
    }

    public String getAccessoryID() {
        return accessoryID;
    }

    public int getAccessoryIDInt() {
        return Integer.parseInt(accessoryID);
    }

    @Override
    public boolean equals(Object object) {
        ToRentRow toRentRow = (ToRentRow) object;
        if (toRentRow != null) {
            if (this.type.equals("equip")) {
                return this.equipID.equals(toRentRow.getEquipID());
            } else {
                return this.accessoryID.equals(toRentRow.getAccessoryID());
            }
        } else {
            return false;
        }
    }

//    @Override
//    public int hashCode() {
//        int result = equipID.hashCode();
//        result = 31 * result + model.hashCode();
//        result = 31 * result + name.hashCode();
//        result = 31 * result + perDayNett.hashCode();
//        result = 31 * result + perDayGross.hashCode();
//        result = 31 * result + rentalID.hashCode();
//        result = 31 * result + quantity.hashCode();
//        result = 31 * result + Boolean.hashCode(usable);
//        result = 31 * result + type.hashCode();
//        result = 31 * result + accessoryID.hashCode();
//        return result;
//    }
}
