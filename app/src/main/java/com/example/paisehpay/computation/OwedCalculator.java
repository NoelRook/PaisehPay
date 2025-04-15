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

    //constructor, set currentUserId
    public OwedCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void calculateTotalOwed(OperationCallbacks.OwedCallback callback) {
        //initialise expense adapter to get all expenses (use get method)
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
                    //check whether expense is paid by current user
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
        //initialise item adapter to get all items (use getItemByExpense method)
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new OperationCallbacks.ListCallback<Item>() {
            @Override
            public void onListLoaded(List<Item> items) {
                for (Item item : items) {
                    //check for each user who owes you, whether amount is NOT 0 and put them into hashmap
                    for (HashMap.Entry<String, Double> entry : item.getDebtPeople().entrySet()) {
                        if (entry.getValue() != 0) {
                            String userId = entry.getKey();
                            double amount = entry.getValue();
                            owedMap.put(userId, owedMap.getOrDefault(userId, 0.0) + amount);
                        }
                    }
                }
                //check for pending operations and callback
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