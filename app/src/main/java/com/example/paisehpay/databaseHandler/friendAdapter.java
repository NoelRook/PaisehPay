package com.example.paisehpay.databaseHandler;

import com.example.paisehpay.blueprints.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class friendAdapter extends BaseDatabase {
    private final DatabaseReference databaseRef;
    private  static final String FRIENDS_TABLE = "Friendships";
    public friendAdapter(String reference) {
        super(FRIENDS_TABLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(reference);
    }

    @Override
    public <T> void create(T object, BaseDatabase.OperationCallback callback) {
        //todo 1. add in create group
        if (!(object instanceof User)){
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("Unsupported object type")));
            return;
        }
        if (object == null) {
            callback.onError(DatabaseError.fromException(new IllegalArgumentException("User ID cannot empty")));
            return;
        }
    }

    @Override
    public void get(BaseDatabase.ListCallback callback) {
        //todo 1. add in get group based on user
    }

    @Override
    public <User> void update(String Id, User user, BaseDatabase.OperationCallback callback) {
        //todo 1. add in update group based on user
    }

    @Override
    public void delete(String Id, BaseDatabase.OperationCallback callback) {
        //todo 1. add in delete group
    }
}
