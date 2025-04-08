package com.example.paisehpay.databaseHandler;

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
    public void getByGroupId(String group_id, ListCallback callback) {
        databaseRef.orderByChild("group_id").equalTo(group_id)
                .addValueEventListener(new ValueEventListener() {
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
