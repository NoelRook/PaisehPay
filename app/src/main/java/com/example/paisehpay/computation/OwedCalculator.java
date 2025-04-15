package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.ItemAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OwedCalculator {
    private String currentUserId;

    public OwedCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void calculateTotalOwed(OperationCallbacks.OwedCallback callback) {
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new OperationCallbacks.ListCallback<Expense>() {
            @Override
            public void onListLoaded(List<Expense> expenses) {
                HashMap<String, Double> owedMap = new HashMap<>();
                final int[] pendingOperations = {expenses.size()};

                if (expenses.isEmpty()) {
                    callback.onOwedCalculated(owedMap);
                }

                for (Expense expense : expenses) {
                    if (expense.getExpensePaidBy().equals(currentUserId)) {
                        processItemsForExpense(expense, owedMap, new OperationCallbacks.OperationComplete() {
                            @Override
                            public void onComplete() {
                                pendingOperations[0]--;
                                if (pendingOperations[0] == 0) {
                                    callback.onOwedCalculated(owedMap);
                                }
                            }
                        });
                    } else {
                        pendingOperations[0]--;
                        if (pendingOperations[0] == 0) {
                            callback.onOwedCalculated(owedMap);
                        }
                    }
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load expenses", error.toException());
                callback.onError(error);
            }
        });
    }

    private void processItemsForExpense(Expense expense, HashMap<String, Double> owedMap,
                                        OperationCallbacks.OperationComplete completeCallback) {
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new OperationCallbacks.ListCallback<Item>() {
            @Override
            public void onListLoaded(List<Item> items) {
                for (Item item : items) {
                    for (HashMap.Entry<String, Double> entry : item.getDebtPeople().entrySet()) {
                        if (entry.getValue() != 0) {
                            String userId = entry.getKey();
                            double amount = entry.getValue();
                            owedMap.put(userId, owedMap.getOrDefault(userId, 0.0) + amount);
                        }
                    }
                }
                completeCallback.onComplete();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load items", error.toException());
                completeCallback.onComplete(); // Continue even if one item fails
            }
        });
    }
}