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

//function2: is to get a list of expense date of each user who you owe with creator id being
//them and debtpeople involving you
public class DateDebt {
    /* initialise hashmap<String, Date> useridDateDEBT
    get all expenses -> ExpenseAdapter.get()
    for expense in expenses:
        checkforYou() -> check whether you are a debtperson
        if you are a debtperson:
            check whether useridDateDEBT.containsKey(expense.getExpensePaidBy())
            if yes:
                if useridDateDEBT.get(expense.getExpensePaidBy()) < expense.getExpenseDate():
                    useridDateDEBT.put(expense.getExpensePaidBy(), expense.getExpenseDate())
                else: continue
            if not:
                useridDateDEBT.put(expense.getExpensePaidBy(), expense.getExpenseDate())
    */

    HashMap<String, Date> useridDate = new HashMap<>();
    private boolean check = false;

    public HashMap<String, Date> peopleYouOwe(String userid){
        ExpenseAdapter expenseAdapter = new ExpenseAdapter();
        expenseAdapter.get(new BaseDatabase.ListCallback<Expense>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Expense> expenses) {
                for (Expense expense : expenses) {
                    if (checkforYou(expense, userid)){
                        //do item logic
                        itemlogiking(expense);
                    }
                }
                return null;
            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load expenses");
            }
        });

        return useridDate;
    }

    public void itemlogiking(Expense expense){
        //check whether your debtor is in useridDate
        //expense.getPaidBy() is in useridDate -> if is, compare the dates
        //if not, add to useridDate
        if (useridDate.containsKey(expense.getExpensePaidBy())){
            if (useridDate.get(expense.getExpensePaidBy()).before(dateformatting(expense.getExpenseDate()))){
                useridDate.put(expense.getExpensePaidBy(), dateformatting(expense.getExpenseDate()));
            }
        }
        else{
            useridDate.put(expense.getExpensePaidBy(), dateformatting(expense.getExpenseDate()));

        }
    }



    public boolean checkforYou(Expense expense, String userid){
        itemAdapter itemadapter = new itemAdapter();
        itemadapter.getItemByExpense(expense.getExpenseId(), new BaseDatabase.ListCallback<Item>() {
            @Override
            public HashMap<String, Date> onListLoaded(List<Item> items) {
                for (Item item: items){
                    if (item.getDebtPeople().containsKey(userid)){
                        if (item.getDebtPeople().get(userid) != 0){
                            check = true;
                            break;
                        }
                    }
                }
                return null;
            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", "Failed to load items");
            }
        });
        return check;
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
