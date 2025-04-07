package com.example.paisehpay.databaseHandler;

import com.example.paisehpay.blueprints.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class itemAdapter extends BaseDatabase{

    private DatabaseReference databaseRef;
    private  static final String ITEMS_TABLE = "Users";

    public itemAdapter(){
        super(ITEMS_TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(ITEMS_TABLE);
    }

    @Override
    public <item> void create(item item, OperationCallback callback) {
        //todo 1. add in create item and find some way to tie it to group
    }

    @Override
    public void get(ListCallback callback) {
        //todo 2. get all items associated to a selected group
    }

    @Override
    public <item> void update(String Id, item user, OperationCallback callback) {
        //todo. 3 update an item associated to a group
    }

    @Override
    public void delete(String Id, OperationCallback callback) {
        // todo. 4 delete an item associated to a group
    }
}
