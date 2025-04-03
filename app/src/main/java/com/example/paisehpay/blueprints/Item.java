package com.example.paisehpay.blueprints;

import com.example.paisehpay.R;

import java.util.ArrayList;

public class Item {
    //used in recycleview of item in each expense (add people pg)

    String itemName;

    Double itemPrice;

    String itemPeople;


    public Item(String itemName, Double itemPrice, String itemPeople){
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
}
