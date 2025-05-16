package com.example.mycontactapp;

public class ContactEntity {
    private String id;
    private String name;
    private String number;
    private String email;
    private String group;
    private String userId;

    // Required empty constructor for Firebase
    public ContactEntity() {
    }

    public ContactEntity(String name, String number, String email, String group) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}