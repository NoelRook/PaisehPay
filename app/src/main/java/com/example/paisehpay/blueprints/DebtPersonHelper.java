package com.example.paisehpay.blueprints;

// class used to populate people in recycler view adapter expense items
// + in recycler view adapter expense description
public class DebtPersonHelper {
    private String userId;
    private double amount;

    // constructor
    public DebtPersonHelper(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    // getter
    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }
}
