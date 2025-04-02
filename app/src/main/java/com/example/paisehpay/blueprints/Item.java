package com.example.paisehpay.blueprints;

public class Item {
    //used in recycleview of item in each expense (add people pg)

    String itemName;

    String itemPrice;

    String itemPeople;


    public Item(String itemName, String itemPrice, String itemPeople){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPeople = itemPeople;
    }

    public String getItemName() {
        return itemName;
    }


    public String getItemPeople() {
        return itemPeople;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPeople(String itemPeople) {
        this.itemPeople = itemPeople;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
