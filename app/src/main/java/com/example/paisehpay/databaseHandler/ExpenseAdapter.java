package com.example.paisehpay.databaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Expense;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends BaseDatabase{
    private DatabaseReference databaseRef;
    private static final String TABLE = "expenses";

    public ExpenseAdapter() {
        super(TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(TABLE);
    }

    @Override
    public <T> void create(T object, OperationCallback callback) throws IllegalArgumentException {
        if (!(object instanceof Expense)) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }
        if (object == null) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("expense ID cannot empty")));
            return;
        }
        Expense expense = (Expense) object;
        String expenseId = databaseRef.push().getKey();
        if (expenseId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate expense ID")));
            return;
        }
        expense.setExpenseId(expenseId);
        databaseRef.child(expenseId).setValue(expense.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    @Override
    public void get(ListCallback callback) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Expense> expenses = new ArrayList<>();
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
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
    public void getByGroupId(String groupId, BaseDatabase.ListCallback<Expense> callback) {
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
            return null;
        }
    }

    @Override
    public <T> void update(String expenseId, T object, OperationCallback callback) {
        if (!(object instanceof Expense)) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }

        Expense expense = (Expense) object;
        databaseRef.child(expenseId).setValue(expense.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

    @Override
    public void delete(String expenseId, OperationCallback callback) {
        databaseRef.child(expenseId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(DatabaseError.fromException(task.getException()));
                    }
                });
    }

}
