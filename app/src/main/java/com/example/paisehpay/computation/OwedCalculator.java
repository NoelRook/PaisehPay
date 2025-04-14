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

//how much others owed to you
public class OwedCalculator {
    private String currentUserId;
    HashMap<String, Double> owedMap = new HashMap<>();
    public OwedCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public HashMap<String, Double> calculateTotalOwed(){
        //HashMap<String, Double> owedMap = new HashMap<>();
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new BaseDatabase.ListCallback<Expense>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Expense> expenses) {
                for (Expense expense : expenses) {
                    if (expense.getExpensePaidBy() == currentUserId){
                        //do item logic
                        itemlogiking(expense);
                    }
                }

                return null;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load expenses");
            }
        });

        return owedMap;
    }



    /*public HashMap<String, Double> calculateGroupOwed(List<Pair<Expense, List<Item>>> expenseItemPairs, String groupId){
        HashMap<String, Double> owedMap = new HashMap<>();
        return owedMap;
    }*/

    public double getTotalOwedAmount(HashMap<String, Double> owedMap){
        double total = 0.0;
        for (double amount: owedMap.values()){
            total += amount;
        }
        return total;
    }


    /*private void addDebtsToMap(HashMap<String, Double> owedMap, HashMap<String, Double> debtPeople) {
        if (debtPeople != null) {
            return;
        }
        for (Map.Entry<String, Double> entry : debtPeople.entrySet()) {
            String userId = entry.getKey();
            double amount = entry.getValue();
            owedMap.put(userId, owedMap.getOrDefault(userId, 0.0) + amount);
        }
    }*/

    public void itemlogiking(Expense expense){
        itemAdapter itemadapter = new itemAdapter();
        itemadapter.getItemByExpense(expense.getExpenseId(), new BaseDatabase.ListCallback<Item>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Item> items) {
                for (Item item: items){
                   for (HashMap.Entry<String, Double> entry: item.getDebtPeople().entrySet()){
                       if (!owedMap.containsKey(entry.getKey())&& entry.getValue()!=0){
                           owedMap.put(entry.getKey(), entry.getValue());
                       }
                       else{
                           owedMap.put(entry.getKey(), owedMap.get(entry.getKey())+entry.getValue());
                       }
                   }
                }
                return null;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load expenses");
            }
        });
    }





}
