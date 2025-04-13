package com.example.paisehpay.computation;

import android.util.Pair;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//how much others owed to you
public class OwedCalculator {
    private String currentUserId;
    public OwedCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public HashMap<String, Double> calculateTotalOwed(List<Pair<Expense, List<Item>>> expenseItemPairs){
        HashMap<String, Double> owedMap = new HashMap<>();


        for (Pair<Expense, List<Item>> pair: expenseItemPairs) {
            Expense expense = pair.first;
            List<Item> items = pair.second;
            //only consider expenses paid by current user
            if (expense.getExpensePaidBy().equals(currentUserId)){
                //No longer have expense.getExpenseItems()
                for (Item item : items){
                    /*HashMap<String, Double> debtPeople = item.getDebtPeople();
                    for (Map.Entry<String, Double> entry: debtPeople.entrySet()){
                        String userId = entry.getKey();
                        double amount = entry.getValue();
                        owedMap.put(userId, owedMap.getOrDefault(userId, 0.0) + amount);*/
                    addDebtsToMap(owedMap, item.getDebtPeople());

                }
            }
        }
        return owedMap;
    }



    public HashMap<String, Double> calculateGroupOwed(List<Pair<Expense, List<Item>>> expenseItemPairs, String groupId){
        HashMap<String, Double> owedMap = new HashMap<>();
        for (Pair<Expense, List<Item>> pair: expenseItemPairs) {
            Expense expense = pair.first;
            List<Item> items = pair.second;
            if (expense.getAssociatedGroup().equals(groupId) && expense.getExpensePaidBy().equals(currentUserId)){
                for (Item item : items){
                    /*HashMap<String, Double> debtPeople = item.getDebtPeople();
                    for (Map.Entry<String, Double> entry: debtPeople.entrySet()){
                        String userId = entry.getKey();
                        double amount = entry.getValue();

                        owedMap.put(userId, owedMap.getOrDefault(userId, 0.0) + amount);
                    }*/
                    addDebtsToMap(owedMap, item.getDebtPeople());
                }
            }
        }
        return owedMap;
    }

    public double getTotalOwedAmount(HashMap<String, Double> owedMap){
        double total = 0.0;
        for (double amount: owedMap.values()){
            total += amount;
        }
        return total;
    }


    private void addDebtsToMap(HashMap<String, Double> owedMap, HashMap<String, Double> debtPeople) {
        if (debtPeople != null) {
            return;
        }
        for (Map.Entry<String, Double> entry : debtPeople.entrySet()) {
            String userId = entry.getKey();
            double amount = entry.getValue();
            owedMap.put(userId, owedMap.getOrDefault(userId, 0.0) + amount);
        }
    }





}
