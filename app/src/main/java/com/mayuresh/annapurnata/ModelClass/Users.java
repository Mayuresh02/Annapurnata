package com.mayuresh.annapurnata.ModelClass;

public class Users {

    public String name;
    public String phone;
    public String aadhar;
    public String email;
    public String password;
    public Users(String name, String phone, String aadhar, String email, String password) {
        this.name=name;
        this.phone=phone;
        this.aadhar=aadhar;
        this.email=email;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
