package com.example.paisehpay.blueprints;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Expense implements Parcelable {
    //expense object used in recycleview of grouphomepage

    String expenseId;
    String description;
    String expenseDate;
    String expensePaidBy;
    String associatedGroup;
    String expenseAction;
    String expenseAmount;
    String expenseCategory;
    //ArrayList<Item> expenseItems; // can we get rid of this?

    public Expense(){

    }

    public Expense(String description,String expenseDate, String expensePaidBy, String expenseAction, String expenseAmount,String expenseCategory, String GroupId ){
        this.description = description;
        this.expenseDate = expenseDate;
        this.expensePaidBy = expensePaidBy;
        this.expenseAction = expenseAction;
        this.associatedGroup = GroupId;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        //this.expenseItems = expenseItems;
    }

    protected Expense(Parcel in) {
        description = in.readString();
        expenseDate = in.readString();
        expensePaidBy = in.readString();
        expenseAction = in.readString();
        expenseAmount = in.readString();
        expenseCategory = in.readString();
        //expenseItems = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    public String getExpenseAction() {
        return expenseAction;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public String getExpensePaidBy() {
        return expensePaidBy;
    }

    public String getDescription() {
        return description;
    }

    public String getExpenseCategory() {return expenseCategory;}


    public void setExpenseId(String key) {
        this.expenseId = key;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("created_at", expenseDate);
        result.put("creator_id", expensePaidBy);
        result.put("currency", "SGD");
        result.put("category", expenseCategory);
        result.put("group_id", associatedGroup);
        result.put("totalAmount", expenseAmount);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(expenseDate);
        parcel.writeString(expensePaidBy);
        parcel.writeString(expenseAction);
        parcel.writeString(expenseAmount);
        parcel.writeString(expenseCategory);
    }

    public String getexpenseId() {
        return expenseId;
    }
}


