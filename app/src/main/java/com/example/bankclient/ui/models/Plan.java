package com.example.bankclient.ui.models;

public class Plan {
    String id, title, date, status, response, plot, sum, solution, productSolution;

    public Plan(String id, String title, String date, String sum, String status, String response, String plot, String solution, String productSolution) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.sum = sum;
        this.status = status;
        this.response = response;
        this.plot = plot;
        this.solution = solution;
        this.productSolution = productSolution;
    }

    public String getProductSolution() {
        return productSolution;
    }

    public void setProductSolution(String productSolution) {
        this.productSolution = productSolution;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

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
}
