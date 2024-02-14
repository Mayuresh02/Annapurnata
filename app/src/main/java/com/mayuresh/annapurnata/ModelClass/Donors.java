package com.mayuresh.annapurnata.ModelClass;

public class Donors {

    public String quantity;
    public String phone;
    public String aadhar;
    public String description;
    public String map;
    public Donors(String quantity, String phone, String aadhar, String description, String map) {
        this.quantity = quantity;
        this.phone = phone;
        this.aadhar = aadhar;
        this.description = description;
        this.map = map;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
