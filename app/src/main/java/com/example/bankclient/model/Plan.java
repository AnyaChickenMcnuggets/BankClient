package com.example.bankclient.model;

public class Plan {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    String title;
    String date;
    String status;
    String response;

    public Plan(String id, String title, String date, String status, String response) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.status = status;
        this.response = response;
    }
}
