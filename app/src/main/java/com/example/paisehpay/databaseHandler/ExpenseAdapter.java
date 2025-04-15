package com.example.paisehpay.databaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Expense;
import com.example.paisehpay.blueprints.ExpenseSingleton;
import com.example.paisehpay.databaseHandler.Interfaces.FirebaseDatabaseAdapter;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExpenseAdapter extends FirebaseDatabaseAdapter<Expense> {
    ItemAdapter itmAdapter; // item adapter instance

    public ExpenseAdapter() {
        super("expenses"); // set the table name to "expenses"
        this.itmAdapter = new ItemAdapter(); // initialize the item adapter
    }

    @Override
    public void create(Expense expense, OperationCallbacks.OperationCallback callback) { // create a new expense in the database
        if (expense == null) { // the expense item cannot be null
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Expense cannot be null")));
            return;
        }

        String expenseId = databaseRef.push().getKey();
        if (expenseId == null) {// the expense id cannot be, means something went wrong with the generate key from firebase
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate expense ID")));
            return;
        }

        expense.setExpenseId(expenseId); // set the expense id into the expense object
        databaseRef.child(expenseId).setValue(expense.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(Objects.requireNonNull(task.getException())));
                    }
                });
    }

    @Override
    public void get(OperationCallbacks.ListCallback<Expense> callback) { // get all expenses from database
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Expense> expenses = new ArrayList<>();
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = mapSnapshotToExpense(expenseSnapshot);
                    if (expense != null) {
                        expense.setExpenseId(expenseSnapshot.getKey());
                        expenses.add(expense);
                    }
                }
                callback.onListLoaded(expenses);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
    // get all expenses related to a single group
    public void getByGroupId(String groupId, OperationCallbacks.ListCallback<Expense> callback) {
        if (databaseRef == null) {
            Log.e("ExpenseAdapter", "Database reference is null");
            callback.onError(DatabaseError.fromException(new NullPointerException("Database reference is null")));
            return;
        }

        databaseRef.orderByChild("group_id").equalTo(groupId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Expense> expenses = new ArrayList<>();
                        for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                            Expense expense = mapSnapshotToExpense(expenseSnapshot);
                            if (expense != null) {
                                expenses.add(expense);
                            }
                        }
                        callback.onListLoaded(expenses);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error);
                    }
                });
    }

    //update expense based on expense id
    @Override
    public void update(String id, Expense object, OperationCallbacks.OperationCallback callback) {
        validateObjectType(object, Expense.class, callback);
        databaseRef.child(id).setValue(((Expense) object).toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(Objects.requireNonNull(task.getException())));
                    }
                });
    }
    //delete expense based on expense id
    @Override
    public void delete(String id, OperationCallbacks.OperationCallback callback) {
        databaseRef.child(id).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ExpenseSingleton singleton = ExpenseSingleton.getInstance(); // Update the singleton so future loads are correct
                        List<Expense> currentList = singleton.getExpenseArrayList();

                        Iterator<Expense> iterator = currentList.iterator();
                        while (iterator.hasNext()) {
                            Expense e = iterator.next();
                            if (e.getExpenseId().equals(id)) {
                                iterator.remove();
                                break;
                            }
                        }

                        callback.onSuccess();  // Notify UI
                    } else {
                        callback.onError(DatabaseError.fromException(Objects.requireNonNull(task.getException())));
                    }
                });
    }
    //When a group is deleted, all expesenses associated to the group would be deleted as well
    public void deleteExpenseByGroupID(String groupId, OperationCallbacks.OperationCallback callback){
        databaseRef.orderByChild("group_id").equalTo(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // get all the expenses associated with the group
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String ExpenseId = itemSnapshot.getKey();
                    itmAdapter.deleteByExpenseId(ExpenseId, new OperationCallbacks.OperationCallback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError(DatabaseError error) {
                        }
                    });
                    itemSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                callback.onError(DatabaseError.fromException(Objects.requireNonNull(task.getException())));
                            }
                        }
                    });
                }
                callback.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }
    // to support getter functions to map the data snapshot variables to the variables in expense object
    private Expense mapSnapshotToExpense(DataSnapshot snapshot) {
        try {
            Expense expense = new Expense();
            expense.setExpenseId(snapshot.getKey());
            expense.setDescription(snapshot.child("description").getValue(String.class));
            expense.setExpenseDate(snapshot.child("created_at").getValue(String.class));
            expense.setExpensePaidBy(snapshot.child("creator_id").getValue(String.class));
            expense.setAssociatedGroup(snapshot.child("group_id").getValue(String.class));
            expense.setExpenseAmount(snapshot.child("totalAmount").getValue(String.class));
            expense.setExpenseCategory(snapshot.child("category").getValue(String.class));

            // Debug logging
            Log.d("ExpenseMapping", "Mapped expense: " + expense.toString());
            return expense;
        } catch (Exception e) {
            Log.e("ExpenseMapping", "Error mapping expense", e);
            
        }
        return null;
    }

}