package com.example.bankclient.ui.models;

public class BankProduct {
    String id, title, time, percentage, creditLimit, minPercentagePay;
    Boolean isIncome;

    public BankProduct(String id,
                       String title,
                       String time,
                       String percentage,
                       Boolean isIncome,
                       String creditLimit,
                       String minPercentagePay) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.percentage = percentage;
        this.isIncome = isIncome;
        this.creditLimit = creditLimit;
        this.minPercentagePay = minPercentagePay;
    }

    public String getMinPercentagePay() {
        return minPercentagePay;
    }

    public void setMinPercentagePay(String minPercentagePay) {
        this.minPercentagePay = minPercentagePay;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIncome() {
        return isIncome;
    }

    public void setIncome(Boolean income) {
        isIncome = income;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
