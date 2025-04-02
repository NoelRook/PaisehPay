package com.example.paisehpay.databaseHandler;

import com.example.paisehpay.blueprints.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public abstract class BaseDatabase {
    private final DatabaseReference databaseRef;

    public BaseDatabase(String reference) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(reference);
    }

    public abstract void create(User user, final OperationCallback callback);

    public abstract void get(final ListCallback callback);

    public abstract void update(String Id, User user, OperationCallback callback);

    public abstract void delete(String Id, final OperationCallback callback);

    public interface ListCallback<T> {
        void onListLoaded(List<T> object);

        void onError(DatabaseError error);
    }

    public interface OperationCallback {
        void onSuccess();

        void onError(DatabaseError error);
    }


}
