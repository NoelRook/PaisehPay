package com.example.paisehpay;

public class Receipt {
    //used in receipt overview page recycle view
    String itemNumber;
    String itemName;
    String itemPrice;
    public Receipt(String itemNumber, String itemName, String itemPrice){
        this.itemNumber = itemNumber;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }
}
