package com.example.paisehpay.blueprints;

import java.util.ArrayList;

public class Expense {
    //expense object used in recycleview of grouphomepage

    String expenseTitle;
    String expenseDate;
    String expensePaidBy;
    String expenseAction;
    String expenseAmount;
    String expenseCategory;
    int expenseIcon;
    ArrayList<Item> expenseItems;

    public Expense(String expenseTitle,String expenseDate, String expensePaidBy, String expenseAction, String expenseAmount,String expenseCategory, int expenseIcon,ArrayList<Item> expenseItems){
        this.expenseTitle = expenseTitle;
        this.expenseDate = expenseDate;
        this.expensePaidBy = expensePaidBy;
        this.expenseAction = expenseAction;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        this.expenseIcon = expenseIcon;
        this.expenseItems = expenseItems;
    }

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

    public int getExpenseIcon() {
        return expenseIcon;
    }

    public ArrayList<Item> getExpenseItems() {
        return expenseItems;
    }
}

