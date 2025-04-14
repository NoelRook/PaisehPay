package com.example.paisehpay.databaseHandler;

import com.example.paisehpay.blueprints.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public abstract class BaseDatabase {
    DatabaseReference databaseRef;

    public BaseDatabase(String reference) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(reference);
    }

    public abstract<T> void create(T object, final OperationCallback callback) throws IllegalArgumentException;

    public abstract void get(final ListCallback callback);

    public abstract<T>void update(String Id, T object, OperationCallback callback);

    public abstract void delete(String Id, final OperationCallback callback);

    public interface ListCallback<T> {
        HashMap<String, Date> onListLoaded(List<T> object);

        void onError(DatabaseError error);
    }



    public interface OperationCallback {
        void onSuccess();

        void onError(DatabaseError error);
    }

    public interface SingleUserCallback {
        void onUserLoaded(User user);
        void onError(DatabaseError error);
    }

    public interface ValueCallback<T> {
        void onValueLoaded(T value);
        void onError(DatabaseError error);
    }


    public interface DateCallback{
        void onDateLoaded(HashMap<String, Date> userIdToDateMap);
        void onError(DatabaseError error);
    }

    public interface CheckCallback{
        void onCheckLoaded(boolean check);
        void onError(DatabaseError error);
    }

    //todo possible implementation of valid checker for each create and update call



}
