package com.example.kasq.Model;

public class Transaksi {
    public String id;
    private String date;
    private String type;
    private String category;
    private String value;
    private String description;
    private User user;

    public Transaksi(String id,String date, String type, String category, String value, String description, User user) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.value = value;
        this.description = description;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
