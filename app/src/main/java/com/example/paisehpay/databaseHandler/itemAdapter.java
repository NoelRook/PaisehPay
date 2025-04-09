package com.example.paisehpay.databaseHandler;

import androidx.annotation.NonNull;

import com.example.paisehpay.blueprints.Item;
import com.example.paisehpay.blueprints.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
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
        //todo 1. add in create item and find some way to tie it to group
        //
        if (!(itemObj instanceof Item)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("this is not a valid datatype")));
            return;
        }
        Item item = (Item) itemObj;
        String itemId = databaseRef.push().getKey();
        if (itemId == null) {
            callback.onError(DatabaseError.fromException(new Exception("Failed to generate group ID")));
            return;
        }
        item.setItemId(itemId);
        //todo make the tomap function in item object
//        databaseRef.child(itemId).setValue(item.toMap()).addOnCompleteListener(task->{
//            if (task.isSuccessful()) {
//                callback.onSuccess();
//            } else {
//                callback.onError(DatabaseError.fromException(task.getException()));
//            }
//        });
    }

    @Override
    public void get(ListCallback callback) {
        //todo 2. get all items associated to a selected group
        // get items from the database with a specific group Id in the field
    }

    @Override
    public <T> void update(String Id, T userObj, OperationCallback callback) {
        //todo. 3 update an item associated to a group

    }

    @Override
    public void delete(String Id, OperationCallback callback) {
        // todo. 4 delete an item associated to a group
    }

    // get based on groupid
    public void getItemByExpense(String expenseId, ListCallback<Item> callback){
        // item people indicates the people who who money to the person
        databaseRef.orderByChild("expenseId").equalTo(expenseId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Item> Items = new ArrayList<>();
                        for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                            Item item = itemSnapshot.getValue(Item.class); //add in item constructor when doine
                            if(item != null){
                                item.setItemId(itemSnapshot.getKey());
                                Items.add(item);
                            }
                        }
                        callback.onListLoaded(Items);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error);
                    }
                });
    }


    //todo mix up create object to create an array of multiple objects
    public void addMultipleItems(ArrayList<Item> items, ListCallback<Item> callback){
        // call the create for the number of items in arraylist
        for(Item item : items){
            // loop through the item list here,
        }
    }


}
