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
    private String itemPeopleString;
    private double itemIndividualPrice = 0.0;
    private String expenseId;
    private ArrayList<String> itemPeopleArray;
    private boolean isSelected;
    private HashMap<String, String> debtpeople ;// {userid: paid}, not paid



    public Item(String itemId,
                String itemName,
                double itemPrice,
                String expenseId,
                String itemPeopleString){
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.expenseId = expenseId;
        this.itemPeopleString = itemPeopleString;
    }


    protected Item(Parcel in) {
        itemId = in.readString();
        itemName = in.readString();
        itemPrice = in.readDouble();
        itemPeopleString = in.readString();
        itemIndividualPrice = in.readDouble();
        expenseId = in.readString();
        itemPeopleArray = in.createStringArrayList();
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

    public void setItemPeopleString(String itemPeopleString) {
        this.itemPeopleString = itemPeopleString;
    }

    public ArrayList<String> setItemPeopleArray(){
        String people = getItemPeopleString();
        if (people != null){
            itemPeopleArray = new ArrayList<>(Arrays.asList(people.split(",")));
        }
        return itemPeopleArray;
    }

    public ArrayList<String> getItemPeopleArray() {
        if (itemPeopleArray != null) {
            setItemPeopleArray();
        }
        return itemPeopleArray;
    }
    public boolean hasPeople(){
        return setItemPeopleArray() != null && setItemPeopleArray().isEmpty();
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

    public void setItemIndividualPrice(double itemIndividualPrice) {
        this.itemIndividualPrice = itemIndividualPrice;
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
        parcel.writeStringList(itemPeopleArray);
    }

    // users array needs to be a hashmap, not an arraylist
//    public HashMap<String, String> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(HashMap<String, String> friends) {
//        this.friends = friends;
//    }

    public Map<String, Object> ToMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", itemId);
        result.put("itemName", itemName);
        result.put("itemPrice", itemPrice);
        result.put("expenseId", expenseId);
        result.put("itemPeopleString", itemPeopleArray);
        return result;
    }


}
