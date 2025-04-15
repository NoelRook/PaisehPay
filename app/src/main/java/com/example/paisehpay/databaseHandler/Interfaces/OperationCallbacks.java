package com.example.paisehpay.databaseHandler.Interfaces;

import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.HashMap;
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

    public interface DateCallback {
        void onDateLoaded(HashMap<String, Date> userIdToDateMap);
        void onError(DatabaseError error);
    }

    public interface OwedCallback {
        void onOwedCalculated(HashMap<String, Double> owedHashmap);
        void onError(DatabaseError error);
    }

    public interface DebtCallback {
        void onDebtCalculated(HashMap<String, Double> debtHashmap);
        void onError(DatabaseError error);
    }

    public interface CheckCallback {
        void onCheckLoaded(boolean check);
        void onError(DatabaseError error);
    }

    public interface OperationComplete {
        void onComplete();
    }
}