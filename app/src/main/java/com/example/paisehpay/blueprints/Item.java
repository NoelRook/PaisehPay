package com.example.paisehpay.blueprints;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;


public class Item implements Parcelable {
    //used in recycleview of item in each expense (add people pg)
    private String itemId;
    private String itemName;
    private double itemPrice;
    private String itemPeopleString;
    private double itemIndividualPrice = 0.0;
    private ArrayList<String> itemPeopleArray;



    public Item(String itemId, String itemName, double itemPrice,String itemPeopleString){
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPeopleString = itemPeopleString;
    }


    protected Item(Parcel in) {
        itemId = in.readString();
        itemName = in.readString();
        itemPrice = in.readDouble();
        itemPeopleString = in.readString();
        itemIndividualPrice = in.readDouble();
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


    public void setItemIndividualPrice(double itemIndividualPrice) {
        this.itemIndividualPrice = itemIndividualPrice;
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
}
