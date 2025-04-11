package com.example.paisehpay.blueprints;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class Expense implements Parcelable {
    private String expenseId;
    private String description;
    private String expenseDate;
    private String expensePaidBy;
    private String associatedGroup;
    private String expenseAction; // Optional field
    private String expenseAmount;
    private String expenseCategory;

    public Expense() {
        // Default constructor required for Firebase
    }

    public Expense(String expenseId, String description, String expenseDate,
                   String expensePaidBy, String associatedGroup, String expenseAction,
                   String expenseAmount, String expenseCategory) {
        this.expenseId = expenseId;
        this.description = description;
        this.expenseDate = expenseDate;
        this.expensePaidBy = expensePaidBy;
        this.associatedGroup = associatedGroup;
        this.expenseAction = expenseAction;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
    }

    protected Expense(Parcel in) {
        expenseId = in.readString();
        description = in.readString();
        expenseDate = in.readString();
        expensePaidBy = in.readString();
        associatedGroup = in.readString();
        expenseAction = in.readString();
        expenseAmount = in.readString();
        expenseCategory = in.readString();
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

    // Getters
    public String getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public String getExpensePaidBy() {
        return expensePaidBy;
    }

    public String getAssociatedGroup() {
        return associatedGroup;
    }

    public String getExpenseAction() {
        return expenseAction;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseCategory() {
        return expenseCategory != null ? expenseCategory : "Uncategorized";
    }

    // Setters
    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setExpensePaidBy(String expensePaidBy) {
        this.expensePaidBy = expensePaidBy;
    }

    public void setAssociatedGroup(String associatedGroup) {
        this.associatedGroup = associatedGroup;
    }

    public void setExpenseAction(String expenseAction) {
        this.expenseAction = expenseAction;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
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
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(expenseId);
        dest.writeString(description);
        dest.writeString(expenseDate);
        dest.writeString(expensePaidBy);
        dest.writeString(associatedGroup);
        dest.writeString(expenseAction);
        dest.writeString(expenseAmount);
        dest.writeString(expenseCategory);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId='" + expenseId + '\'' +
                ", description='" + description + '\'' +
                ", expenseDate='" + expenseDate + '\'' +
                ", expensePaidBy='" + expensePaidBy + '\'' +
                ", associatedGroup='" + associatedGroup + '\'' +
                ", expenseAction='" + expenseAction + '\'' +
                ", expenseAmount='" + expenseAmount + '\'' +
                ", expenseCategory='" + expenseCategory + '\'' +
                '}';
    }
}

