package com.group5.sellit;

public class Users {
    String username,phonenumber,password,isadmin,isUser, address, age, email, keyid, prof_img;

    public Users() {
    }

    public Users(String username, String phonenumber, String password, String isadmin, String isUser, String address, String age, String email, String keyid, String prof_img) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.password = password;
        this.isadmin = isadmin;
        this.isUser = isUser;
        this.address = address;
        this.age = age;
        this.email = email;
        this.keyid = keyid;
        this.prof_img = prof_img;
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

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getProf_img() {
        return prof_img;
    }

    public void setProf_img(String prof_img) {
        this.prof_img = prof_img;
    }
}
