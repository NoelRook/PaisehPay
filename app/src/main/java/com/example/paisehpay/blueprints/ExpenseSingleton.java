package com.example.paisehpay.blueprints;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

public class ExpenseSingleton {
    private static ExpenseSingleton instance = null;
    private ArrayList<Expense> expenseArrayList;

    // constructor
    private ExpenseSingleton() {
        expenseArrayList = new ArrayList<>();
    }

    public static ExpenseSingleton getInstance() {
        if (instance == null) {
            instance = new ExpenseSingleton();
        }
        return instance;
    }

    // setter
    public ArrayList<Expense> getExpenseArrayList() {
        return expenseArrayList;
    }

    // getter
    public void setExpenses(List<Expense> expenses) {
        this.expenseArrayList.clear();
        this.expenseArrayList.addAll(expenses);
    }

    public void getExpensesByGroupId(String groupId, OperationCallbacks.ListCallback<Expense> callback) {
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.getByGroupId(groupId, new OperationCallbacks.ListCallback<>() {
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

    // for faust
    public ArrayList<String> returnExpId(){
        ArrayList<String> expId = new ArrayList<>();
        for (Expense expense: expenseArrayList){
            expId.add(expense.getExpenseId());
        }
        return expId;
    }
}
