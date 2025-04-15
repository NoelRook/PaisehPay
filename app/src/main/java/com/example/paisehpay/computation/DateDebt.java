package com.example.paisehpay.computation;

import android.util.Log;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.BaseDatabase;
import com.example.paisehpay.databaseHandler.ExpenseAdapter;
import com.example.paisehpay.databaseHandler.itemAdapter;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateDebt {
    public void peopleYouOwe(String userId, BaseDatabase.DateCallback callback) {
        Log.d("DateDebt", "Starting for user: " + userId);

        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new BaseDatabase.ListCallback<Expense>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Expense> expenses) {
                HashMap<String, Date> useridDate = new HashMap<>();
                final int[] pendingOperations = {0};

                if (expenses.isEmpty()) {
                    callback.onDateLoaded(useridDate);
                    return null;
                }

                for (Expense expense : expenses) {
                    pendingOperations[0]++;
                    checkForDebtor(expense, userId, useridDate, new BaseDatabase.OperationComplete() {
                        @Override
                        public void onComplete() {
                            pendingOperations[0]--;
                            if (pendingOperations[0] == 0) {
                                callback.onDateLoaded(useridDate);
                            }
                        }
                    });
                }
                return null;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("DateDebt", "Error loading expenses", error.toException());
                callback.onError(error);
            }
        });
    }

    private void checkForDebtor(Expense expense, String userId, HashMap<String, Date> useridDate,
                                BaseDatabase.OperationComplete completeCallback) {
        itemAdapter itemAdapter = new itemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new BaseDatabase.ListCallback<Item>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Item> items) {
                boolean isDebtor = false;
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
                return null;
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