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

public class DateDebt {
    public void peopleYouOwe(String userId, OperationCallbacks.DateCallback callback) {
        Log.d("DateDebt", "Starting for user: " + userId);

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
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new OperationCallbacks.ListCallback<Item>() {
            @Override
            public void onListLoaded(List<Item> items) {
                boolean isDebtor = false;
                Log.d("expensedate",expense.toString());
                Date expenseDate = dateformatting(expense.getExpenseDate());
                String payerId = expense.getExpensePaidBy();

                for (Item item : items) {
                    if (item.getDebtPeople().containsKey(userId) &&
                            item.getDebtPeople().get(userId) != 0) {
                        isDebtor = true;
                        break;
                    }
                }

                if (isDebtor) {
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

    private Date dateformatting(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(strdate);
        } catch (ParseException e) {
            Log.e("DateDebt", "Error parsing date: " + strdate, e);
            return new Date(); // Return current date as fallback
        }
    }
}