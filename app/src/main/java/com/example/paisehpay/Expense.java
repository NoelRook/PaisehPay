package com.example.paisehpay;

public class Expense {
    //expense object used in recycleview of grouphomepage

    String expenseTitle;
    String expenseDate;
    String expensePaidBy;
    String expenseAction;
    String expenseAmount;

    public Expense(String expenseTitle,String expenseDate, String expensePaidBy, String expenseAction, String expenseAmount){
        this.expenseTitle = expenseTitle;
        this.expenseDate = expenseDate;
        this.expensePaidBy = expensePaidBy;
        this.expenseAction = expenseAction;
        this.expenseAmount = expenseAmount;
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
}
// <!-- TODO: 1. currently never include category image seen in the layout -->
