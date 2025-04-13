package com.example.paisehpay.blueprints;

import android.util.Log;

import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ExpenseSingleton {
    private static ExpenseSingleton instance = null;
    private ArrayList<Expense> expenseArrayList;

    private ExpenseSingleton() {
        expenseArrayList = new ArrayList<>();
    }

    public static ExpenseSingleton getInstance() {
        if (instance == null) {
            instance = new ExpenseSingleton();
        }
        return instance;
    }

    public ArrayList<Expense> getExpenseArrayList() {
        return expenseArrayList;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenseArrayList.clear();
        this.expenseArrayList.addAll(expenses);
    }

    public void getExpensesByGroupId(String groupId, BaseDatabase.ListCallback<Expense> callback) {
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.getByGroupId(groupId, new BaseDatabase.ListCallback<Expense>() {
            @Override
            public void onListLoaded(List<Expense> expenses) {
                setExpenses(expenses);
                callback.onListLoaded(expenses);
            }

            @Override
            public void onError(DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    public ArrayList<String> returnExpId(){
        ArrayList<String> expId = new ArrayList<>();
        for (Expense expense: expenseArrayList){
            expId.add(expense.getExpenseId());
        }
        return expId;
    }
}
