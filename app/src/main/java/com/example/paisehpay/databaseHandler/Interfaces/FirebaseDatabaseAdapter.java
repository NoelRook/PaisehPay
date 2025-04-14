package com.example.paisehpay.databaseHandler.Interfaces;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// FirebaseDatabaseAdapter.java
public abstract class FirebaseDatabaseAdapter<T> implements BaseDatabaseOperations<T> {
    protected final DatabaseReference databaseRef;
    protected final String tableName;

    public FirebaseDatabaseAdapter(String tableName) {
        this.tableName = tableName;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseRef = database.getReference(tableName);
    }

    protected void validateObjectType(Object object, Class<T> expectedClass, OperationCallbacks.OperationCallback callback) {
        if (!expectedClass.isInstance(object)) {
            callback.onError(DatabaseError.fromException(
                    new IllegalArgumentException(
                            "Unsupported object type. Expected: " + expectedClass.getSimpleName())));
            throw new IllegalArgumentException("Unsupported object type");
        }
    }
}
