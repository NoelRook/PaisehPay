package com.example.paisehpay.blueprints;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Item implements Parcelable {
    //used in recycleview of item in each expense (add people pg)
    private String itemId;
    private String itemName;
    private double itemPrice;
    private String itemPeopleString = ""; // not stored in DB
    private double itemIndividualPrice = 0.0; // remove this later on
    private String expenseId;
    private ArrayList<User> itemPeopleArray = new ArrayList<>(); // not stored in DB
    private boolean isSelected = false;
    private HashMap<String, Double> debtPeople ;// {userid: settled or not settled}, not paid
    // if for all items in totalOwed == 0, user is settled



    public Item(String itemId,
                String itemName,
                double itemPrice,
                String expenseId,
                String itemPeopleString, HashMap<String, Double> debtpeople){
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.expenseId = expenseId;
        this.itemPeopleString = itemPeopleString;
        this.debtPeople = debtpeople;
    }


    protected Item(Parcel in) {
        itemId = in.readString();
        itemName = in.readString();
        itemPrice = in.readDouble();
        itemPeopleString = in.readString();
        itemIndividualPrice = in.readDouble();
        expenseId = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getItemName() {
        return itemName;
    }


    public String getItemPeopleString() {
        return itemPeopleString;
    }

    public void setItemPeopleString(ArrayList<User> itemPeopleArray) {
        StringBuilder sb = new StringBuilder();
        for (User user:itemPeopleArray){
            sb.append(user.getUsername());
            sb.append(", ");
        }
        itemPeopleString = sb.substring(0,sb.length()-2);

    }

    public void setItemPeopleArray(ArrayList<User> arrayList){
        itemPeopleArray = arrayList;
        setItemPeopleString(itemPeopleArray);
    }

    public ArrayList<User> getItemPeopleArray() {
        return itemPeopleArray;
    }
    public boolean hasPeople(){
        return getItemPeopleArray() != null && getItemPeopleArray().isEmpty();
    }

    public String getItemPriceString() {
        return "$" + itemPrice;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public double getItemIndividualPrice() {
        return itemIndividualPrice;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(itemId);
        parcel.writeString(itemName);
        parcel.writeDouble(itemPrice);
        parcel.writeString(itemPeopleString);
        parcel.writeDouble(itemIndividualPrice);
    }

    // users array needs to be a hashmap, not an arraylist
    public HashMap<String, Double> getDebtPeople() {
        return debtPeople;
    }

    public void setDebtPeople(HashMap<String, Double> debtPeople) {
        this.debtPeople = debtPeople;
    }



    public Map<String, Object> ToMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", itemId);
        result.put("itemName", itemName);
        result.put("itemPrice", itemPrice);
        result.put("expenseId", expenseId);
        result.put("debtpeople", debtPeople);
        return result;
    }

    public void calculateDebts() {
        if (itemPeopleArray != null && !itemPeopleArray.isEmpty()) {
            double splitAmount = itemPrice / itemPeopleArray.size();
            for (int i = 0; i<itemPeopleArray.size(); i++) {
                String userId = itemPeopleArray.get(i).getId();
                debtPeople.put(userId, splitAmount);
            }
        }
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
