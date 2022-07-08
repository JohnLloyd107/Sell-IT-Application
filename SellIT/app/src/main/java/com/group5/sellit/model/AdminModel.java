package com.group5.sellit.model;

public class AdminModel {

    String username,phonenumber,password,isadmin,isUser, address, age, email, pin;

    public AdminModel() {
    }

    public AdminModel(String username, String phonenumber, String password, String isadmin, String isUser, String address, String age, String email, String pin) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.password = password;
        this.isadmin = isadmin;
        this.isUser = isUser;
        this.address = address;
        this.age = age;
        this.email = email;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
