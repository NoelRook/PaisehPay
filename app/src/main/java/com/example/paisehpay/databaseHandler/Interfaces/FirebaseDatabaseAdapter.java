package com.example.paisehpay.databaseHandler.Interfaces;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FirebaseDatabaseAdapter<T> implements BaseDatabaseOperations<T> {
    // abstract class that instantiates the database reference and the table name
    protected final DatabaseReference databaseRef;
    protected final String tableName;
    // instantiate the specific database ref to the specific table
    public FirebaseDatabaseAdapter(String tableName) {
        this.tableName = tableName; // set the table name
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // get the database instance
        this.databaseRef = database.getReference(tableName); // set the database reference to the table name
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
