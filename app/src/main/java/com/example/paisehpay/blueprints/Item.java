package com.example.paisehpay.blueprints;


import android.os.Parcel;


public class Item {
    //used in recycleview of item in each expense (add people pg)
    String itemId;

    String itemName;

    Double itemPrice;

    String itemPeople;


    public Item(String itemId,String itemName, Double itemPrice, String itemPeople){
        this.itemId = itemId;
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

    public Double getItemPrice() {
        return itemPrice;
    }

    public String getItemPriceString(){
        return itemPrice.toString();
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPeople(String itemPeople) {
        this.itemPeople = itemPeople;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

}
