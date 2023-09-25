package com.example.bankclient.model;

public class Plan {
    String id, title, date, status, response;

    public Plan(String id, String title, String date, String status, String response) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.status = status;
        this.response = response;
    }
}
