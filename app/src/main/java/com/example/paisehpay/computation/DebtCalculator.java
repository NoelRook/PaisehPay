package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.ItemAdapter;
import com.example.paisehpay.sessionHandler.PreferenceManager;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.List;

public class DebtCalculator {
    private String currentUserId;

    //constructor, set currentUserId
    public DebtCalculator(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    //method to calculate total debt and returns hashmap userid:  total amount user owes them
    public void calculateTotalDebt(OperationCallbacks.DebtCallback callback) {
        //initialise expense adapter to get all expenses (use get method)
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new OperationCallbacks.ListCallback<Expense>() {
            @Override
            public void onListLoaded(List<Expense> expenses) {
                HashMap<String, Double> debtMap = new HashMap<>();
                final int[] pendingOperations = {expenses.size()};

                if (expenses.isEmpty()) {
                    callback.onDebtCalculated(debtMap);
                }

                for (Expense expense : expenses) {
                    // check whether user has debt in the specific expense using checkForDebtInExpense method
                    checkForDebtInExpense(expense, debtMap, new OperationCallbacks.OperationComplete() {
                        @Override
                        public void onComplete() {
                            pendingOperations[0]--;
                            if (pendingOperations[0] == 0) {
                                callback.onDebtCalculated(debtMap);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load expenses", error.toException());
                callback.onError(error);
            }
        });
    }

    private void checkForDebtInExpense(Expense expense, HashMap<String, Double> debtMap,
                                       OperationCallbacks.OperationComplete completeCallback) {
        //initialise item adapter to get all items (use getItemByExpense method)
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new OperationCallbacks.ListCallback<Item>() {
            @Override
            public void onListLoaded(List<Item> items) {
                for (Item item : items) {
                    //if user is in debtPeople hashmap
                    // if amount is not 0
                    // add amount to debtMap with corresponding userid key
                    Log.d("hey",item.getDebtPeople().toString());

                    if (item.getDebtPeople().containsKey(currentUserId)) {
                        Double amount = item.getDebtPeople().get(currentUserId);
                        if (amount != null && amount != 0) {
                            String payerId = expense.getExpensePaidBy();
                            debtMap.put(payerId, debtMap.getOrDefault(payerId, 0.0) + amount);
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