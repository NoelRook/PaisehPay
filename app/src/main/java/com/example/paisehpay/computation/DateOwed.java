package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.ItemAdapter;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateOwed {
    public void peopleOweYouLatest(String userId, OperationCallbacks.DateCallback callback) {
        Log.d("DateOwed", "Starting for user: " + userId);

        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new OperationCallbacks.ListCallback<Expense>() {
            @Override
            public  void onListLoaded(List<Expense> expenses) {
                HashMap<String, Date> useridDate = new HashMap<>();
                final int[] pendingOperations = {0};

                if (expenses.isEmpty()) {
                    callback.onDateLoaded(useridDate);
                }

                for (Expense expense : expenses) {
                    if (expense.getExpensePaidBy().equals(userId)) {
                        pendingOperations[0]++;
                        processExpenseItems(expense, useridDate, new OperationCallbacks.OperationComplete() {
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

                // Handle case where no matching expenses were found
                if (pendingOperations[0] == 0) {
                    callback.onDateLoaded(useridDate);
                }

            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("DateOwed", "Error loading expenses", error.toException());
                callback.onError(error);
            }
        });
    }

    private void processExpenseItems(Expense expense, HashMap<String, Date> useridDate,
                                     OperationCallbacks.OperationComplete completeCallback) {
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new OperationCallbacks.ListCallback<Item>() {
            @Override
            public void onListLoaded(List<Item> items) {
                Date expenseDate = dateformatting(expense.getExpenseDate());

                for (Item item : items) {
                    for (String debtorId : item.getDebtPeople().keySet()) {
                        if (item.getDebtPeople().get(debtorId) != 0) {
                            if (!useridDate.containsKey(debtorId) ||
                                    useridDate.get(debtorId).before(expenseDate)) {
                                useridDate.put(debtorId, expenseDate);
                            }
                        }
                    }
                }
                completeCallback.onComplete();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("DateOwed", "Error loading items", error.toException());
                completeCallback.onComplete(); // Continue processing other items
            }
        });
    }

    private Date dateformatting(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(strdate);
        } catch (ParseException e) {
            Log.e("DateOwed", "Error parsing date: " + strdate, e);
            return new Date(); // Return current date as fallback
        }
    }
}