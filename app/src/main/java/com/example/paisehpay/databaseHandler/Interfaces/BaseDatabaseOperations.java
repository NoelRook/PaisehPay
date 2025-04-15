package com.example.paisehpay.databaseHandler.Interfaces;

// BaseDatabaseOperations.java
public interface BaseDatabaseOperations<T> {
    void create(T object, OperationCallbacks.OperationCallback callback) throws IllegalArgumentException;
    void get(OperationCallbacks.ListCallback<T> callback);
    void update(String id, T object, OperationCallbacks.OperationCallback callback);
    void delete(String id, OperationCallbacks.OperationCallback callback);
}
