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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//function: is to get a list of latest expense date with creator id being you for each user who owes you

public class DateOwed {
    HashMap<String, Date> useridDate = new HashMap<>();

    //the String usesid is the current user's id

    public void peopleOweYouLatest(String userid, BaseDatabase.DateCallback callback) {
        Log.d("DateOwed", "Start " + userid);
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();

        expenseAdapter.get(new BaseDatabase.ListCallback<Expense>() {
            int pending = 0;
            boolean errorOccurred = false;

            @Override
            public HashMap<String, Date> onListLoaded(List<Expense> expenses) {
                if (expenses.isEmpty()) {
                    callback.onDateLoaded(useridDate); // Nothing to process
                    return null;
                }

                for (Expense expense : expenses) {
                    if (expense.getExpensePaidBy().equals(userid)) {
                        pending++;
                        itemlogikingLATEST(expense, new BaseDatabase.ListCallback<Item>() {
                            @Override
                            public HashMap<String, Date> onListLoaded(List<Item> items) {
                                for (Item item : items) {
                                    for (HashMap.Entry<String, Double> entry : item.getDebtPeople().entrySet()) {
                                        if (entry.getValue() != 0) {
                                            String debtorId = entry.getKey();
                                            Date expenseDate = dateformatting(expense.getExpenseDate());

                                            if (!useridDate.containsKey(debtorId) ||
                                                    useridDate.get(debtorId).before(expenseDate)) {
                                                useridDate.put(debtorId, expenseDate);
                                                Log.d("DateOwed", "Updated " + debtorId + ": " + expenseDate);
                                            }
                                        }
                                    }
                                }

                                pending--;
                                if (pending == 0 && !errorOccurred) {
                                    callback.onDateLoaded(useridDate); // All done
                                }
                                return null;
                            }

                            @Override
                            public void onError(DatabaseError error) {
                                errorOccurred = true;
                                callback.onError(error);
                            }
                        });
                    }
                }

                // In case no matching expenses were found
                if (pending == 0) {
                    callback.onDateLoaded(useridDate);
                }

                return null;
            }

            @Override
            public void onError(DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    //itemlogiking is for LATEST expense date
    public void itemlogikingLATEST(Expense expense, BaseDatabase.ListCallback<Item> callback){
        itemAdapter itemadapter = new itemAdapter();
        itemadapter.getItemByExpense(expense.getExpenseId(), new BaseDatabase.ListCallback<Item>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Item> items) {
                Log.d("DateOwed", "ListItemLoaded"+items);

                //Log.d("done processing", useridDate.toString());
                callback.onListLoaded(items);
                return null;
            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load items");
            }
        });
    }



    public Date dateformatting(String strdate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date date = formatter.parse(strdate);
            return date;
        } catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }


}
