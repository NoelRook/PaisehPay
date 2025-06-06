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

public class DateOwed extends DateBase{
    public void peopleOweYouLatest(String userId, OperationCallbacks.DateCallback callback) {
        //callback to handle asynchronous calls
        Log.d("DateOwed", "Starting for user: " + userId);
        // initialise expense adapter to get all expenses (use get method)
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new OperationCallbacks.ListCallback<Expense>() {
            @Override
            public  void onListLoaded(List<Expense> expenses) {
                HashMap<String, Date> useridDate = new HashMap<>();
                // initialise pending operations to 0
                final int[] pendingOperations = {0};

                if (expenses.isEmpty()) {
                    callback.onDateLoaded(useridDate);
                }

                for (Expense expense : expenses) {
                    //if expense is paid by user, add to pending operations and call method process expense items to do mapping
                    if (expense.getExpensePaidBy().equals(userId)) {
                        pendingOperations[0]++;
                        processExpenseItems(expense, useridDate, new OperationCallbacks.OperationComplete() {
                            @Override
                            public void onComplete() {
                                //only callback when there are no pending operations
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
        //initialise item adapter to get all items (use getItemByExpense method)
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.getItemByExpense(expense.getExpenseId(), new OperationCallbacks.ListCallback<Item>() {
            @Override
            public void onListLoaded(List<Item> items) {
                Date expenseDate = dateformatting(expense.getExpenseDate());

                for (Item item : items) {
                    //if amount owed by person is not 0 then,
                    // if the useridDate does not already contains personid as key or date in useridDate is earlier than expense date, put personid: expense date in useridDate
                    for (String debtorId : item.getDebtPeople().keySet()) {
                        if (item.getDebtPeople().get(debtorId) != 0) {
                            if (!useridDate.containsKey(debtorId) ||
                                    useridDate.get(debtorId).before(expenseDate)) {
                                useridDate.put(debtorId, expenseDate);
                            }
                        }
                    }
                }
                //check for pending operations and callback
                completeCallback.onComplete();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("DateOwed", "Error loading items", error.toException());
                completeCallback.onComplete(); // Continue processing other items
            }
        });
    }

    /*private Date dateformatting(String strdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(strdate);
        } catch (ParseException e) {
            Log.e("DateOwed", "Error parsing date: " + strdate, e);
            return new Date(); // Return current date as fallback
        }
    }*/
}