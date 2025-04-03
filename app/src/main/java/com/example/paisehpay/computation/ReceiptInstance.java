package com.example.paisehpay.computation;

public class ReceiptInstance {
    private static Receipts instance;

    public static Receipts getInstance() {
        if (instance == null){
            instance = new Receipts();
        }
        return instance;
    }
}
