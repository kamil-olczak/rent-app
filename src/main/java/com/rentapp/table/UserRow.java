package com.rentapp.table;

public class UserRow {
    private String userName;
    private String userRole;

    public UserRow(String userName) {
        this.userName = userName;
        userRole = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
