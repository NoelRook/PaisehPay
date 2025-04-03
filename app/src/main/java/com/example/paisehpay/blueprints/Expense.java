package com.example.paisehpay.blueprints;

public class Expense {
    //expense object used in recycleview of grouphomepage

    String expenseTitle;
    String expenseDate;
    String expensePaidBy;
    String expenseAction;
    String expenseAmount;
    String expenseCategory;
    int expenseIcon;

    public Expense(String expenseTitle,String expenseDate, String expensePaidBy, String expenseAction, String expenseAmount,String expenseCategory, int expenseIcon){
        this.expenseTitle = expenseTitle;
        this.expenseDate = expenseDate;
        this.expensePaidBy = expensePaidBy;
        this.expenseAction = expenseAction;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        this.expenseIcon = expenseIcon;
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
}

