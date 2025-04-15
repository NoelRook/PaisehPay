package com.example.paisehpay.computation;

public class ReceiptInstance {
    //singleton class for receipt - creates instance only when first requested

    private static Receipts instance;

    public static Receipts getInstance() {
        if (instance == null){
            instance = new Receipts();
        }
        return instance;
    }

}
