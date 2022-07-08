package com.group5.sellit.model;

public class OrdersModel {
    String customer_address, customer_name, customer_number, date, status, time, total_amount,id, status2, img_prof;

    public OrdersModel() {
    }

    public OrdersModel(String customer_address, String customer_name, String customer_number, String date, String status, String time, String total_amount, String id, String status2, String img_prof) {
        this.customer_address = customer_address;
        this.customer_name = customer_name;
        this.customer_number = customer_number;
        this.date = date;
        this.status = status;
        this.time = time;
        this.total_amount = total_amount;
        this.id = id;
        this.status2 = status2;
        this.img_prof = img_prof;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(String customer_number) {
        this.customer_number = customer_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getImg_prof() {
        return img_prof;
    }

    public void setImg_prof(String img_prof) {
        this.img_prof = img_prof;
    }
}
