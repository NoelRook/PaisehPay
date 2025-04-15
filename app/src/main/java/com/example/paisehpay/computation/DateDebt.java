package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.ItemAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateDebt extends DateBase {
    //callback to handle asynchronous calls
    public void peopleYouOwe(String userId, OperationCallbacks.DateCallback callback) {
        Log.d("DateDebt", "Starting for user: " + userId);
        // initialise expense adapter to get all expenses (use get method)
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new OperationCallbacks.ListCallback<Expense>() {
            @Override
            public void onListLoaded(List<Expense> expenses) {
                HashMap<String, Date> useridDate = new HashMap<>();
                final int[] pendingOperations = {0};

                if (expenses.isEmpty()) {
                    callback.onDateLoaded(useridDate);
                }

                for (Expense expense : expenses) {
                    //increment pending operations and call method to check whether user is a debtor in this expense
                    pendingOperations[0]++;
                    checkForDebtor(expense, userId, useridDate, new OperationCallbacks.OperationComplete() {
                        @Override
                        public void onComplete() {
                            pendingOperations[0]--;
                            if (pendingOperations[0] == 0) {
                                callback.onDateLoaded(useridDate);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("DateDebt", "Error loading expenses", error.toException());
                callback.onError(error);
            }
        });
    }

    private void checkForDebtor(Expense expense, String userId, HashMap<String, Date> useridDate,
                                OperationCallbacks.OperationComplete completeCallback) {
        //initialise item adapter to get all items (use getItemByExpense method)
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new OperationCallbacks.ListCallback<Item>() {
            @Override
            public void onListLoaded(List<Item> items) {
                boolean isDebtor = false;
                Log.d("expensedate",expense.toString());
                Date expenseDate = dateformatting(expense.getExpenseDate());
                String payerId = expense.getExpensePaidBy();

                for (Item item : items) {
                    //for all items in expense, check whether user is in debtPeople hashmap and change isDebtor appropriately
                    if (item.getDebtPeople().containsKey(userId) &&
                            item.getDebtPeople().get(userId) != 0) {
                        isDebtor = true;
                        break;
                    }
                }

                if (isDebtor) {
                    //if isDebtor is true, check whether useridDate does not already contains payerId
                    // as key or date in useridDate is earlier than expense date, put payerId: expense date in useridDate
                    if (!useridDate.containsKey(payerId) ||
                            useridDate.get(payerId).before(expenseDate)) {
                        useridDate.put(payerId, expenseDate);
                    }
                }

                completeCallback.onComplete();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("DateDebt", "Error loading items", error.toException());
                completeCallback.onComplete(); // Continue processing other items
            }
        });
    }

}