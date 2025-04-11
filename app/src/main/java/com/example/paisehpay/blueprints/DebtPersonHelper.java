package com.example.paisehpay.blueprints;

public class DebtPersonHelper {
    private String userId;
    private double amount;

    public DebtPersonHelper(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }
}
