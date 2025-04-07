package com.example.paisehpay.blueprints;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Expense implements Parcelable {
    //expense object used in recycleview of grouphomepage

    String expenseTitle;
    String expenseDate;
    String expensePaidBy;
    String expenseAction;
    String expenseAmount;
    String expenseCategory;
    ArrayList<Item> expenseItems;

    public Expense(String expenseTitle,String expenseDate, String expensePaidBy, String expenseAction, String expenseAmount,String expenseCategory,ArrayList<Item> expenseItems){
        this.expenseTitle = expenseTitle;
        this.expenseDate = expenseDate;
        this.expensePaidBy = expensePaidBy;
        this.expenseAction = expenseAction;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        this.expenseItems = expenseItems;
    }

    protected Expense(Parcel in) {
        expenseTitle = in.readString();
        expenseDate = in.readString();
        expensePaidBy = in.readString();
        expenseAction = in.readString();
        expenseAmount = in.readString();
        expenseCategory = in.readString();
        expenseItems = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
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

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public String getExpenseCategory() {return expenseCategory;}

    public ArrayList<Item> getExpenseItems() {
        return expenseItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(expenseTitle);
        parcel.writeString(expenseDate);
        parcel.writeString(expensePaidBy);
        parcel.writeString(expenseAction);
        parcel.writeString(expenseAmount);
        parcel.writeString(expenseCategory);
        parcel.writeTypedList(expenseItems);
    }
}

