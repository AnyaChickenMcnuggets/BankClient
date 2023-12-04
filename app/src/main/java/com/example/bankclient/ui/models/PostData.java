package com.example.bankclient.ui.models;

public class PostData {
    String matrix, n, m;

    public PostData(String matrix, String n, String m) {
        this.matrix = matrix;
        this.n = n;
        this.m = m;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
