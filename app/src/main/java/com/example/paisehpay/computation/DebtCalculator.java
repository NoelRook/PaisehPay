package com.example.paisehpay.computation;

import android.util.Pair;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;

import java.util.HashMap;
import java.util.List;

public class DebtCalculator {
    private String currentUserId;

    public DebtCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public HashMap<String, Double> calculateTotalDebt(List<Pair<Expense, List<Item>>> expenseItemPairs) {
        HashMap<String, Double> debtMap = new HashMap<>();

        for (Pair<Expense, List<Item>> pair : expenseItemPairs){
            Expense expense = pair.first;
            List<Item> items = pair.second;
            processItemsForDebt(debtMap, expense.getExpensePaidBy(), items);
        }
        return debtMap;
    }

    public HashMap<String, Double> calculateGroupDebt(List<Pair<Expense, List<Item>>> expenseItemPairs, String groupId) {
        HashMap<String, Double> debtMap = new HashMap<>();
        for (Pair<Expense, List<Item>> pair : expenseItemPairs){
            Expense expense = pair.first;
            List<Item> items = pair.second;
            if (expense.getAssociatedGroup().equals(groupId)){
                processItemsForDebt(debtMap, expense.getExpensePaidBy(), items);
            }
        }
        return debtMap;
    }

    private void processItemsForDebt(HashMap<String, Double> debtMap, String payerId, List<Item> items) {
        for (Item item : items) {
            Double amountOwed = item.getDebtPeople().get(currentUserId);
            if (amountOwed != null && amountOwed >0) {
                debtMap.put(payerId, debtMap.getOrDefault(payerId, 0.0) + amountOwed);
            }
        }
    }

    public double getTotalDebtAmount(HashMap<String, Double> debtMap) {
        double total = 0.0;
        for (double amount : debtMap.values()) {
            total += amount;
        }
        return total;
    }




}

