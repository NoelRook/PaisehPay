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

    public DebtCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void calculateTotalDebt(BaseDatabase.DebtCallback callback) {
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new BaseDatabase.ListCallback<Expense>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Expense> expenses) {
                HashMap<String, Double> debtMap = new HashMap<>();
                final int[] pendingOperations = {expenses.size()};

                if (expenses.isEmpty()) {
                    callback.onDebtCalculated(debtMap);
                    return null;
                }

                for (Expense expense : expenses) {
                    checkForDebtInExpense(expense, debtMap, new BaseDatabase.OperationComplete() {
                        @Override
                        public void onComplete() {
                            pendingOperations[0]--;
                            if (pendingOperations[0] == 0) {
                                callback.onDebtCalculated(debtMap);
                            }
                        }
                    });
                }
                return null;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load expenses", error.toException());
                callback.onError(error);
            }
        });
    }

    private void checkForDebtInExpense(Expense expense, HashMap<String, Double> debtMap,
                                       BaseDatabase.OperationComplete completeCallback) {
        itemAdapter itemAdapter = new itemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new BaseDatabase.ListCallback<Item>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Item> items) {
                for (Item item : items) {
                    if (item.getDebtPeople().containsKey(currentUserId)) {
                        Double amount = item.getDebtPeople().get(currentUserId);
                        if (amount != null && amount != 0) {
                            String payerId = expense.getExpensePaidBy();
                            debtMap.put(payerId, debtMap.getOrDefault(payerId, 0.0) + amount);
                        }
                    }
                }
                completeCallback.onComplete();
                return null;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load items", error.toException());
                completeCallback.onComplete(); // Continue even if one item fails
            }
        });
    }
}