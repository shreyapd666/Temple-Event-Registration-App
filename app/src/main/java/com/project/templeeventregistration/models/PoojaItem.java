package com.project.templeeventregistration.models;

public class PoojaItem {
    private String name;
    private String price;
    private String date;
    private String desc;

    @Override
    public String toString() {
        return "PoojaItem{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public PoojaItem() {
    }

    public PoojaItem(String name, String price, String date, String desc) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
