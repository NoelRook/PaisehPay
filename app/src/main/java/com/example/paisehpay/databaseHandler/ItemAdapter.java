package com.example.paisehpay.databaseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Group;
import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.databaseHandler.Interfaces.FirebaseDatabaseAdapter;
import com.example.paisehpay.databaseHandler.Interfaces.OperationCallbacks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemAdapter extends FirebaseDatabaseAdapter<Item> {

    public ItemAdapter() {
        super("Items");
    }

    @Override
    public void create(Item object, OperationCallbacks.OperationCallback callback) throws IllegalArgumentException {
        validateObjectType(object, Item.class, callback);

        String itemId = databaseRef.push().getKey();

        if (itemId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate item ID")));
            return;
        }

        ((Item) object).setItemId(itemId);

        // Convert the Item to a Map and store it
        databaseRef
                .child(itemId)
                .setValue(((Item) object).ToMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        if (task.getException() != null) {
                            callback.onError(DatabaseError.fromException(task.getException()));
                        } else {
                            callback.onError(DatabaseError.fromException(new Exception("Unknown error occurred")));
                        }
                    }
                });
    }

    @Override
    public void get(OperationCallbacks.ListCallback<Item> callback) {}

    @Override
    public void update(String id, Item object, OperationCallbacks.OperationCallback callback) {
        validateObjectType(object, Item.class, callback);
        if (id == null || id.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot be null or empty")));
            return;
        }
        databaseRef
                .child(id)
                .setValue(((Item) object).ToMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    @NonNull
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError(DatabaseError.fromException(task.getException()));
                        }
                    }
                });
    }

    @Override
    public void delete(String id, OperationCallbacks.OperationCallback callback) {
    }
    // get based on groupid
    public void getItemByExpense(String expenseId, OperationCallbacks.ListCallback<Item> callback) {
        databaseRef
                .orderByChild("expenseId")
                .equalTo(expenseId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Item> Items = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            Item item = Item.fromDataSnapshot(itemSnapshot);
                            Items.add(item);
                        }
                        callback.onListLoaded(Items);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error);
                    }
                });
    }


    public void storeExpenseItems(ArrayList<Item> items, String expenseId,OperationCallbacks.OperationCallback callback) {
        int length = items.size();
        ExecutorService itemExecutorService = Executors.newFixedThreadPool(length);// creates a new thread for each create button for the database
        for (Item item : items) {
            item.calculateDebts();
            item.setExpenseId(expenseId); // Set the expense ID for each item
            item.setSettled(false);

            itemExecutorService.submit(() -> {
                create(item, new OperationCallbacks.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Saved item", item.getItemName() + " for expense " + expenseId);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Save item error", error.getMessage());
                    }
                });
            });
        }
    }

    // get the total amount owed by the user in an expense where they are paying
    public void getTotalAmountOwedByUser(String userid, String expenseId, OperationCallbacks.ValueCallback<Double> callback) {
        if (userid == null || userid.isEmpty() || expenseId == null || expenseId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID or Expense ID cannot be null or empty")));
            return;
        }
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalOwed = 0.0; // instantiate the total variable

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String thisExpenseId = itemSnapshot.child("expenseId").getValue(String.class);
                    if (!expenseId.equals(thisExpenseId)) continue; // if not part of the expense

                    DataSnapshot debtPeopleSnapshot = itemSnapshot.child("debtpeople");
                    if (debtPeopleSnapshot.exists() && debtPeopleSnapshot.hasChild(userid)) { // if the user is part of debtPeople, means they owe money
                        Double amount = debtPeopleSnapshot.child(userid).getValue(Double.class);
                        if (amount != null) {
                            totalOwed += amount; // add the money to the total
                        }
                    }
                }

                callback.onValueLoaded(totalOwed);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    // if you have settled payment with the payer, set all the debt that you owe to the payer to 0
    public void updateSettledUser(String userid, String expenseId, OperationCallbacks.OperationCallback callback) {
        if (userid == null || userid.isEmpty() || expenseId == null || expenseId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID or Expense ID cannot be null or empty")));
            return;
        }

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean anyUpdates = false;
                int totalItems = 0;

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String itemExpenseId = itemSnapshot.child("expenseId").getValue(String.class);
                    if (!expenseId.equals(itemExpenseId)) continue;

                    totalItems++;

                    DataSnapshot debtPeopleSnapshot = itemSnapshot.child("debtpeople");
                    if (debtPeopleSnapshot.exists() && debtPeopleSnapshot.hasChild(userid)) {
                        // Update the user's debt to 0
                        databaseRef.child(itemSnapshot.getKey())
                                .child("debtpeople")
                                .child(userid)
                                .setValue(0)
                                .addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {
                                        Log.e("updateSettledUser", "Failed to update user debt for item: " + itemSnapshot.getKey());
                                    }
                                });
                        anyUpdates = true;
                    }
                }

                if (totalItems == 0) {
                    callback.onError(DatabaseError.fromException(new Exception("No items found with specified expenseId")));
                } else if (!anyUpdates) {
                    callback.onError(DatabaseError.fromException(new Exception("User not in debtpeople for any matching items")));
                } else {
                    callback.onSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    //delete all items that contain the given expenseId
    public void deleteByExpenseId(String expenseId, OperationCallbacks.OperationCallback callback){
        databaseRef.orderByChild("expenseId").equalTo(expenseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    itemSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (!task.isSuccessful()) {
                                 callback.onError(DatabaseError.fromException(task.getException()));
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


}
