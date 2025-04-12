package com.example.paisehpay.databaseHandler;

import android.util.Log;
import android.webkit.ValueCallback;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class itemAdapter extends BaseDatabase{

    private DatabaseReference databaseRef;
    private  static final String ITEMS_TABLE = "Items";

    public itemAdapter(){
        super(ITEMS_TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(ITEMS_TABLE);
    }

    @Override
    public <T> void create(T itemObj, OperationCallback callback) {
        Log.d("itemAdapter","create"+itemObj);

        if (!(itemObj instanceof Item)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Invalid data type - expected Item")));
            return;
        }

        Item item = (Item) itemObj;
        String itemId = databaseRef.push().getKey();

        if (itemId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate item ID")));
            return;
        }

        item.setItemId(itemId);

        // Convert the Item to a Map and store it
        databaseRef.child(itemId).setValue(item.ToMap())
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
    public void get(ListCallback callback) {
        //todo 2. get all items associated to a selected group
        // get items from the database with a specific group Id in the field
    }

    @Override
    public <T> void update(String ExpenseId, T object, OperationCallback callback) {
        if (!(object instanceof Item)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }
        if (ExpenseId == null || ExpenseId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot be null or empty")));
            return;
        }
        User user = (User) object;
        databaseRef.child(ExpenseId).setValue(user.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override @NonNull
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
    public void delete(String Id, OperationCallback callback) {
        // todo. 4 delete an item associated to a group
    }

    // get based on groupid
    public void getItemByExpense(String expenseId, ListCallback<Item> callback) {
        databaseRef.orderByChild("expenseId").equalTo(expenseId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Item> Items = new ArrayList<>();
                        for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
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


    public void addMultipleItems(ArrayList<Item> items, ListCallback<Item> callback){
        // call the create for the number of items in arraylist
        for(Item item : items){
            // loop through the item list here,
            update(item.getItemId(), item, new OperationCallback() {
                @Override
                public void onSuccess() {
                    Log.d("item updated", item.getItemId()+ "was updated");
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        }
    }
    public void UpdateItemList(String expenseId,List<Item> itemList){
        for(Item item: itemList){
            update(expenseId,item, new OperationCallback(){
                @Override
                public void onSuccess() {
                    Log.d("update items", "items successfully updated");
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.d("update items", "error with updating items" + error.toString());
                }
            });
        }
    }

    public void updateSettledUser(String userid, String expenseId, OperationCallback callback) {
        if (userid == null || userid.isEmpty() || expenseId == null || expenseId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID or Expense ID cannot be null or empty")));
            return;
        }

        //get all items with the specified expenseId
        databaseRef.orderByChild("expenseId").equalTo(expenseId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean anyUpdates = false;

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            // Get the debtpeople map for each item
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

                        if (anyUpdates) {
                            callback.onSuccess();
                        } else {
                            callback.onError(DatabaseError.fromException(new Exception("No items found with specified expenseId or user not in debtpeople")));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error);
                    }
                });
    }
    // how to use
//    itemAdapter.updateSettledUser("haW03Hy3PYRPQgsg5BUddghbzx22", "ONZsSMuU6yFOC_9kdWu", new OperationCallback() {
//        @Override
//        public void onSuccess() {
//            Log.d("UpdateSettled", "User debt updated successfully");
//        }
//
//        @Override
//        public void onError(DatabaseError error) {
//            Log.e("UpdateSettled", "Error updating user debt: " + error.getMessage());
//        }
//    });

    public void getTotalAmountOwedByUser(String userid, String expenseId, ValueCallback<Double> callback) {
        if (userid == null || userid.isEmpty() || expenseId == null || expenseId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID or Expense ID cannot be null or empty")));
            return;
        }
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double totalOwed = 0.0;

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            // Get the debtpeople map for each item
                            DataSnapshot debtPeopleSnapshot = itemSnapshot.child("debtpeople");
                            if (debtPeopleSnapshot.exists() && debtPeopleSnapshot.hasChild(userid)) {
                                Double amount = debtPeopleSnapshot.child(userid).getValue(Double.class);
                                if (amount != null) {
                                    totalOwed += amount;
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

    // how to use
//    itemAdapter.getTotalAmountOwedByUser("haW03Hy3PYRPQgsg5BUddghbzx22", "ONZsSMuU6yFOC_9kdWu", new ValueCallback<Double>() {
//        @Override
//        public void onValueLoaded(Double total) {
//            Log.d("TotalOwed", "User owes: " + total);
//            // Update UI with the total amount
//        }
//
//        @Override
//        public void onError(DatabaseError error) {
//            Log.e("TotalOwed", "Error getting total: " + error.getMessage());
//        }
//    });


    public void updateSettledUser(String payeriD, String userid, String expenseId, OperationCallback callback) {
        if (userid == null || userid.isEmpty() || expenseId == null || expenseId.isEmpty()) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID or Expense ID cannot be null or empty")));
            return;
        }

        // First filter by creator_id (payeriD)
        databaseRef.orderByChild("creator_id").equalTo(payeriD)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean anyUpdates = false;
                        int totalItems = 0;

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            // Then filter by expenseId in code
                            String itemExpenseId = itemSnapshot.child("expenseId").getValue(String.class);
                            if (expenseId.equals(itemExpenseId)) {
                                totalItems++;
                                // Check if user exists in debtpeople
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
                        }

                        if (totalItems == 0) {
                            callback.onError(DatabaseError.fromException(new Exception("No items found with specified payer and expenseId")));
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




}