package com.example.paisehpay.databaseHandler.Interfaces;

import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface OperationCallbacks {
    interface ListCallback<T> {
        void onListLoaded(List<T> object);
        void onError(DatabaseError error);
    }

    interface OperationCallback {
        void onSuccess();
        void onError(DatabaseError error);
    }

    interface SingleObjectCallback<T> {
        void onObjectLoaded(T object);
        void onError(DatabaseError error);
    }

    interface ValueCallback<T> {
        void onValueLoaded(T value);
        void onError(DatabaseError error);
    }
}