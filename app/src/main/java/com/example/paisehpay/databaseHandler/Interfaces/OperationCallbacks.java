package com.example.paisehpay.databaseHandler.Interfaces;

import com.google.firebase.database.DatabaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OperationCallbacks {
    interface ListCallback<T> { // for getter functions
        void onListLoaded(List<T> object);
        void onError(DatabaseError error);
    }

    interface OperationCallback { // for update/create/delete functions
        void onSuccess();
        void onError(DatabaseError error);
    }

    interface ValueCallback<T> {
        void onValueLoaded(T value);
        void onError(DatabaseError error);
    }

    interface DateCallback {
        void onDateLoaded(HashMap<String, Date> userIdToDateMap);
        void onError(DatabaseError error);
    }

    interface OwedCallback {
        void onOwedCalculated(HashMap<String, Double> owedHashmap);
        void onError(DatabaseError error);
    }

    interface DebtCallback {
        void onDebtCalculated(HashMap<String, Double> debtHashmap);
        void onError(DatabaseError error);
    }

    interface OperationComplete {
        void onComplete();
    }
}