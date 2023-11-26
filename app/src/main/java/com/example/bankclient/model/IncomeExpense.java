package com.example.bankclient.model;


public class IncomeExpense {
    String id, title, sum, date, period;
    Boolean isLong, isIncome;

    public IncomeExpense(String id, String title, String sum, String date, Boolean isLong, Boolean isIncome, String period) {
        this.id = id;
        this.title = title;
        this.sum = sum;
        this.date = date;
        this.isLong = isLong;
        this.isIncome = isIncome;
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Boolean getIncome() {
        return isIncome;
    }

    public void setIncome(Boolean income) {
        isIncome = income;
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

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getLong() {
        return isLong;
    }

    public void setLong(Boolean aLong) {
        isLong = aLong;
    }
}
