package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.itemAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DebtCalculator {
    private String currentUserId;
    private boolean check = false;

    private Double tempIndivprice;
    HashMap<String, Double> debtMap = new HashMap<>();

    public DebtCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public HashMap<String, Double> calculateTotalDebt() {
        //HashMap<String, Double> debtMap = new HashMap<>();
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new BaseDatabase.ListCallback<Expense>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Expense> expenses) {
                for (Expense expense : expenses) {
                    if (checkforYou(expense, currentUserId)){
                        //do item logic
                        itemlogiking(expense);
                        tempIndivprice = 0.0;
                    }
                }
                return null;
            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load expenses");
            }
        });

        return debtMap;
    }

    /*public HashMap<String, Double> calculateGroupDebt(String groupId) {
        //HashMap<String, Double> debtMap = new HashMap<>();
        return debtMap;
    }*/

    public double getTotalDebtAmount(HashMap<String, Double> debtMap) {
        double total = 0.0;
        for (double amount : debtMap.values()) {
            total += amount;
        }
        return total;
    }

    private boolean checkforYou(Expense expense, String userid){
        itemAdapter itemadapter = new itemAdapter();
        itemadapter.getItemByExpense(expense.getExpenseId(), new BaseDatabase.ListCallback<Item>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Item> items) {
                for (Item item: items){
                    if (item.getDebtPeople().containsKey(userid)){
                        if (item.getDebtPeople().get(userid) != 0){
                            check = true;
                            tempIndivprice = item.getDebtPeople().get(userid);
                            break;
                        }
                    }
                }
                return null;
            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load items");
            }
        });
        return check;
    }

    public void itemlogiking(Expense expense){
        if (debtMap.containsKey(expense.getExpensePaidBy())){
            debtMap.put(expense.getExpensePaidBy(), debtMap.get(expense.getExpensePaidBy()) + tempIndivprice);
        }
        else{
            debtMap.put(expense.getExpensePaidBy(), tempIndivprice);
        }
    }




}

