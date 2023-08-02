package com.project.templeeventregistration.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class PoojaRegistrationUserItem implements Serializable {
    @Exclude
    private String paymentId;
    private String name;
    private String date;
    private String price;
    private String status;


    PoojaRegistrationUserItem() {}

    public PoojaRegistrationUserItem(String name, String date, String price, String paymentId, String status) {
        this.name = name;
        this.date = date;
        this.price = price;
        this.paymentId = paymentId;
        this.status = status;
    }

    public String getId() {
        return paymentId;
    }

    public void setId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}

