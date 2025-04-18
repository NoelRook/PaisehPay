package com.example.paisehpay.databaseHandler.Interfaces;

// interface for all database adapters to enforce Base functionality in base database
public interface BaseDatabaseOperations<T> {
    // create may have an illegal argument, functions that override it need to account for it
    void create(T object, OperationCallbacks.OperationCallback callback) throws IllegalArgumentException;
    void get(OperationCallbacks.ListCallback<T> callback);
    void update(String id, T object, OperationCallbacks.OperationCallback callback) throws IllegalArgumentException;
    void delete(String id, OperationCallbacks.OperationCallback callback);
}
